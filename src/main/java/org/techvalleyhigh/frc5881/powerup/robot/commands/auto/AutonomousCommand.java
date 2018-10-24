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
     * Logic for choosing and running an autonomous command
     * @param chosen array list of integers
     * @param timeToWait milliseconds to wait before running the commands
     */
    public AutonomousCommand(ArrayList<Integer> chosen, long timeToWait) {
        if (Robot.testChooser.getSelected() == TestMode.VELOCITY) {
            addSequential(new VelocityTest(RobotMap.driveFrontLeft, 175, 2.0));

        } else {
            // Add all the autonomous paths into one big Map
            this.autos = TrajectoryUtil.getAutos();

            // Keep track if we find an auto routine so if we don't we'll default to crossing the auto line
            boolean found = false;

            // Track how long it takes to get match data
            long start = System.currentTimeMillis();

            // Wait for match data or until time passed
            while (!hasData() && System.currentTimeMillis() - start > matchDataTime) {
                // Do nothing we are just waiting
            }

            if (hasData()) {
                // Tell the drive team how long it took to get match data
                System.out.format("It took %d milliseconds to get match data\n", System.currentTimeMillis() - start);
            } else {
                System.out.println("We didn't get match data in under 2 seconds!!!");
            }

            // Wait for our time to wait has expired
            while (System.currentTimeMillis() - start < timeToWait) {
                // Do nothing we are just waiting
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
                    // Used for routines such as crossing the auto line
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

                        // Let the record know that we found an autonomous routine and don't default to the outline
                        found = true;
                        break;
                    }
                }

                // If we don't find a routine default to crossing the auto line
                if (!found) {
                    // Add auto 100 (auto line)
                    autoline();
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

    /**
     * Logic for sending moveArm and elevator commands if we want to score a cube
     */
    private void score(Autonomous autoToRun) {
        // Start by grabbing the cube (the pistons should be activated anyway)
        addSequential(new ManipulatorClose());
        // Move the arm into a safe orientation for the elevator
        addSequential(new SetArm(800, 0));
        // Begin the autonomous routine
        addSequential(profile(autoToRun));

        // Set the elevator to correct height to score in owned structure
        if (autoToRun.getFeature() == MatchData.GameFeature.SWITCH_NEAR) {
            addParallel(new SetElevator(Elevator.switchTicks, autoToRun.getElevatorTimeToWait()));
        } else if (autoToRun.getFeature() == MatchData.GameFeature.SCALE) {
            addParallel(new SetElevator(Elevator.scaleTicks, autoToRun.getElevatorTimeToWait()));
        }

        // After moving the elevator and profiling score
        addSequential(new ManipulatorOpen());
    }

    @Override
    protected boolean isFinished() {
        return false;
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
