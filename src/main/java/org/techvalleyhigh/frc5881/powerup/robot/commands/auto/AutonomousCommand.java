package org.techvalleyhigh.frc5881.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import openrio.powerup.MatchData;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorOpen;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.SetArm;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.SetElevator;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.Straight;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.motion.MotionProfile;
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
     * Logic for choosing and running an autonomous command
     * @param chosen array list of integers
     * @param timeToWait milliseconds to wait before running the commands
     */
    public AutonomousCommand(ArrayList<Integer> chosen, long timeToWait) {
        currentMode = mode.NONE;

        startCommands = new ArrayList<>();
        endCommands = new ArrayList<>();

        // Add all the autonomous paths into one big Map
        this.autos = TrajectoryUtil.getAutos();

        // Keep track if we find an auto routine so if we don't we'll default to crossing the auto line
        boolean found = false;

        // Track how long it takes to get match data
        long start = System.currentTimeMillis();

        // Wait for match data or until time passed
        while (!hasData() && System.currentTimeMillis() - start > matchDataTime) { }

        if (hasData()) {
            // Tell the drive team how long it took to get match data
            System.out.format("It took %d milliseconds to get match data\n", System.currentTimeMillis() - start);
        } else {
            System.out.println("We didn't get match data in under 2 seconds!!!");
        }

        // Wait for our time to wait has expired
        while(System.currentTimeMillis() - start < timeToWait) {}

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
                    addSequential(new MotionProfile(auto));
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

    private enum commands {
        CLOSE_GRABBER,
        OPEN_GRABBER,
        SWITCH,
        SCALE,
        MOVE_ARM
    }

    private enum mode {
        INIT,
        RUNNING,
        END,
        NONE
    }

    private mode currentMode;

    private Autonomous auto;

    /**
     * Logic for sending moveArm and elevator commands if we want to score a cube
     */
    private void score(Autonomous autoToRun) {
        this.auto = autoToRun;
        // Start with just sending the motion profile
        profile = new MotionProfile(autoToRun);
        addParallel(profile);

        startCommands.add(commands.CLOSE_GRABBER);
        startCommands.add(commands.MOVE_ARM);

        // Tip the moveArm forward just to be out of the elevator
        //addParallel(new SetArm(800, 0));

        // Set the moveArm to correct height to score in owned structure and tell the manipulator to score slightly after
        if (autoToRun.getFeature() == MatchData.GameFeature.SWITCH_NEAR) {
            startCommands.add(commands.SWITCH);
            //addParallel(new SetElevator(Elevator.switchTicks, autoToRun.getTimeToWait()));

        } else if (autoToRun.getFeature() == MatchData.GameFeature.SCALE) {
            startCommands.add(commands.SCALE);
            //addParallel(new SetElevator(Elevator.scaleTicks, autoToRun.getTimeToWait()));
        }

        endCommands.add(commands.OPEN_GRABBER);
        //addSequential(new ManipulatorOpen());

        currentMode = mode.INIT;
    }

    private ArrayList<commands> startCommands;
    private ArrayList<commands> endCommands;

    private ManipulatorClose close;
    private SetArm moveArm;
    private SetElevator elevator;
    private MotionProfile profile;
    private ManipulatorOpen open;

    @Override
    protected void execute() {
        //System.out.println(currentMode);

        if (currentMode == mode.NONE) {
            //System.out.println(currentMode);
            // Do nothing

        } else if (currentMode == mode.INIT) {
            startCommands(startCommands);

            // Change mode
            currentMode = mode.RUNNING;
            System.out.println(currentMode);

        } else if (currentMode == mode.RUNNING) {
            if (profile.isCompleted()) {
                currentMode = mode.END;
                System.out.println(currentMode);
            }

        } else if (currentMode == mode.END) {
            startCommands(endCommands);
            currentMode = mode.NONE;
            System.out.println(currentMode);
        }
    }

    private void startCommands(ArrayList<commands> list) {
        for (commands c: list) {
                /*
                private enum commands {
                    CLOSE_GRABBER,
                    OPEN_GRABBER,
                    SWITCH,
                    SCALE,
                    MOVE_ARM
                }
                 */
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
        return false;
    }

    /**
     * Just cross the auto line
     */
    private void autoline() {
        addSequential(new MotionProfile(autos.get(100)));
    }


    /**
     * Returns whether or not we've gotten the match data
     * @return boolean true we have data - false we don't
     */
    private boolean hasData() {
        return MatchData.getOwnedSide(MatchData.GameFeature.SCALE) != MatchData.OwnedSide.UNKNOWN;
    }

}
