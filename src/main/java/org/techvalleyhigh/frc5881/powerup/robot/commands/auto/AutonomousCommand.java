package org.techvalleyhigh.frc5881.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import openrio.powerup.MatchData;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorOpen;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.SetArm;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.SetElevator;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.profiles.motion_profile.MotionProfile;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.profiles.PositionProfile;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.profiles.VelocityProfile;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.tuning.VelocityTest;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Elevator;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

import java.util.ArrayList;
import java.util.Map;

public class AutonomousCommand extends CommandGroup {


    /**
     * Milliseconds to allow waiting for Match Data since it might not send instantly
     */
    private static final long matchDataTime = 2000;

    /**
     * Map storing all our autos
     */
    private Map<Integer, Autonomous> autos;

    /**
     * Chosen auto to profile
     */
    private Autonomous auto;


    /**
     * Logic for choosing and running an autonomous command
     * @param chosen array list of integers
     * @param timeToWait milliseconds to wait before running the commands
     */
    public AutonomousCommand(ArrayList<Integer> chosen, long timeToWait) {
        if (Robot.testChooser.getSelected() == TestMode.VELOCITY) {
            addSequential(new VelocityTest(RobotMap.driveFrontLeft, 175, 2.0));

        } else {
            currentState = states.NONE;

            startCommands = new ArrayList<>();
            endCommands = new ArrayList<>();

            // Add all the autonomous paths into one big Map
            this.autos = TrajectoryUtil.getAutos();

            // Keep track if we find an auto routine so if we don't we'll default to crossing the auto line
            boolean found = false;

            // Track how long it takes to get match data
            long start = System.currentTimeMillis();

            // Wait for match data or until time passed
            while (!hasData() && System.currentTimeMillis() - start > matchDataTime) {

            }

            if (hasData()) {
                // Tell the drive team how long it took to get match data
                System.out.format("It took %d milliseconds to get match data\n", System.currentTimeMillis() - start);
            } else {
                System.out.println("We didn't get match data in under 2 seconds!!!");
            }

            // Wait for our time to wait has expired
            while (System.currentTimeMillis() - start < timeToWait) {
            }

            // Tell the drive team how long the robot waited to start (they should already have this information)
            System.out.format("It took %d milliseconds to start autonomous\n", System.currentTimeMillis() - start);

            // If we have the data continue as normal
            if (hasData()) {
                // Let the Smart dashboard know that we got match data in time
                SmartDashboard.putBoolean("Match Data", true);

                // Filter and choose auto
                for (Integer i : chosen) {
                    Autonomous auto = autos.get(i);
                    System.out.println("Check auto: " + i);

                    // If statement checks to see it the MatchData's "owned side"
                    // is the same as the Autonomous command's copy of "owned side"

                    // Set feature to null to tell the bot to overwrite normal checks and run that specific auto
                    if (auto.getFeature() == null) {
                        System.out.println("Overwriting auto choosing");
                        System.out.println("Added Auto " + i);
                        addSequential(profile(auto));
                        found = true;
                        break;
                    }

                    // Check that the feature's owned side is the same as auto
                    System.out.println("Auto feature " + MatchData.getOwnedSide(auto.getFeature()));
                    if (MatchData.getOwnedSide(auto.getFeature()) == auto.getSide()) {
                        System.out.println("Added Auto " + i);

                        //addSequential(new MotionProfile(auto));

                        // Score a cube with chosen auto
                        score(auto);

                        // Let the record that we found an autonomous routine and don't default to the outline
                        found = true;
                        break;
                    }
                }

                // If we don't find a routine default to crossing the auto line
                if (!found) {
                    // Add auto 100 (auto line)
                    //autoline();
                }

                // If we don't get the data in time flip out and just run the auto line
            } else {
                System.out.println("Defaulting to auto line");
                SmartDashboard.putBoolean("Match Data", false);

                // Default to auto line
                autoline();
            }
        }
    }

    private enum commands {
        CLOSE_GRABBER,
        OPEN_GRABBER,
        SWITCH,
        SCALE,
        MOVE_ARM
    }

    private enum states {
        INIT,
        RUNNING,
        END,
        DONE,
        NONE
    }

    private states currentState;

    /**
     * Logic for sending moveArm and elevator commands if we want to score a cube
     */
    private void score(Autonomous autoToRun) {
        auto = autoToRun;

        // Start with just sending the profile to a command
        positionProfile = profile(autoToRun);
        addParallel(positionProfile);

        startCommands.add(commands.CLOSE_GRABBER);
        startCommands.add(commands.MOVE_ARM);

        // Set the moveArm to correct height to score in owned structure and tell the manipulator to score slightly after
        if (autoToRun.getFeature() == MatchData.GameFeature.SWITCH_NEAR) {
            startCommands.add(commands.SWITCH);

        } else if (autoToRun.getFeature() == MatchData.GameFeature.SCALE) {
            startCommands.add(commands.SCALE);
        }

        endCommands.add(commands.OPEN_GRABBER);

        currentState = states.INIT;
    }

    // Groups of commands to run during execute
    private ArrayList<commands> startCommands;
    private ArrayList<commands> endCommands;

    // Instance variables storing each command we'll run so we can easily switch states
    private ManipulatorClose close;
    private SetArm moveArm;
    private SetElevator elevator;
    private Command positionProfile;
    private ManipulatorOpen open;

    /**
     * Called repeatedly when this Command is scheduled to run
     * Sort of thrown together finite state machine to control all the autonomous
     */
    @Override
    protected void execute() {
        if (currentState == states.NONE || currentState == states.DONE) {
            // Do nothing

        } else if (currentState == states.INIT) {
            runCommands(startCommands);

            // Change states
            currentState = states.RUNNING;

        } else if (currentState == states.RUNNING) {
            if (positionProfile.isCompleted()) {
                currentState = states.END;
            }

        } else if (currentState == states.END) {
            runCommands(endCommands);
            currentState = states.DONE;
        }
    }

    /**
     * Logic for converting command enum to a running command
     * @param list ArrayList of commands to run
     */
    private void runCommands(ArrayList<commands> list) {
        for (commands c: list) {
            if (c.equals(commands.CLOSE_GRABBER)) {
                System.out.println("Close Grabber");
                close = new ManipulatorClose();
                close.start();

            } else if (c.equals(commands.OPEN_GRABBER)) {
                System.out.println("Open Grabber");
                open = new ManipulatorOpen();
                open.start();

            } else if (c.equals(commands.SWITCH)) {
                System.out.println("Switch");
                elevator = new SetElevator(Elevator.switchTicks, auto.getTimeToWait());
                elevator.start();

            } else if (c.equals(commands.SCALE)) {
                System.out.println("Scale");
                elevator = new SetElevator(Elevator.scaleTicks, auto.getTimeToWait());
                elevator.start();

            } else if (c.equals(commands.MOVE_ARM)) {
                System.out.println("Arm");
                moveArm = new SetArm(800, 0);
                moveArm.start();
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return currentState == states.DONE;
    }

    /**
     * Just cross the auto line
     */
    private void autoline() {
        addSequential(profile(autos.get(100)));
    }


    /**
     * Returns whether or not we've gotten the match data
     * @return boolean true we have data - false we don't
     */
    private boolean hasData() {
        return MatchData.getOwnedSide(MatchData.GameFeature.SCALE) != MatchData.OwnedSide.UNKNOWN;
    }

    /**
     * Enum for supporting different profile generation methods
     */
    public enum ProfileMode {
        MOTION,
        VELOCITY,
        POSITION
    }

    /**
     * Return command running a profile in chosen states
     * @param auto Autonomous object to generate profile from
     * @return command controlling a profile
     */
    private Command profile(Autonomous auto) {
        ProfileMode mode = Robot.profileChooser.getSelected();

        if (mode == ProfileMode.POSITION) {
            return new PositionProfile(auto);

        } else if(mode == ProfileMode.VELOCITY) {
            return new VelocityProfile(auto);

        } else {
            return new MotionProfile(auto);
        }
    }

    /**
     * Enum for supporting different test modes
     */
    public enum TestMode {
        NONE,
        VELOCITY
    }
}
