package org.techvalleyhigh.frc5881.powerup.robot.commands;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.DriveControl;


/**
 * Command to handle autonomous motion profiling
 * Takes in array of waypoints, converts them to trajectories, floods drive talons
 * motion profile buffers with trajectory segments, tells the talons to start driving
 */
public class MotionProfile extends Command {
    private static int kMinPointsTalon = 5;

    private Waypoint[] waypoints;
    private MotionProfileStatus status = new MotionProfileStatus();

    // TODO: Once motion profiling works add Trajectory.Config to constructor to handle different speeds / accelerations / etc

    /**
     * Makes the drive talons drive a profile described by waypoints
     * @param waypoints
     */
    public MotionProfile(Waypoint[] waypoints) {
        this.waypoints = waypoints;
        Robot.driveControl.robotDrive.setSafetyEnabled(false);
        requires(Robot.driveControl);
    }

    /**
     * Periodically processes motion profile buffers separate from RoboRio allowing us to get
     * best performance
     */
    class PeriodicRunnable implements java.lang.Runnable {
        public void run() {
            RobotMap.driveFrontRight.processMotionProfileBuffer();
            RobotMap.driveFrontLeft.processMotionProfileBuffer();
        }
    }
    private Notifier notifier = new Notifier(new PeriodicRunnable());

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    protected void initialize() {
        System.out.println("INIT");
        // Change control modes
        RobotMap.driveFrontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
        RobotMap.driveFrontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);

        // Clear the buffers just in case there's something left over
        RobotMap.driveBackRight.clearMotionProfileTrajectories();
        RobotMap.driveBackLeft.clearMotionProfileTrajectories();

        // Start processing buffers
        notifier.startPeriodic(0.005);

        // Set up configs
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
                Trajectory.Config.SAMPLES_HIGH, 0.05, 1, 4, 60);

        // Generate trajectory
        Trajectory trajectory = Pathfinder.generate(this.waypoints, config);

        // Change trajectory into a tank drive
        // Wheelbase Width = 2.3226166667 feet
        TankModifier modifier = new TankModifier(trajectory).modify(2.3226166667);

        // Separate trajectories for left and right
        Trajectory leftTrajectory = modifier.getLeftTrajectory();
        Trajectory rightTrajectory = modifier.getRightTrajectory();

        // Push trajectories into the talons
        pushTrajectory(leftTrajectory, RobotMap.driveFrontLeft);
        pushTrajectory(rightTrajectory, RobotMap.driveFrontRight);

        // Make sure there's some points in the buffer before going at it
        RobotMap.driveFrontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
        RobotMap.driveFrontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
    }

    protected void execute() {
        //System.out.println("Execute");
        // Update the status

        /*
        if (status.btmBufferCnt > kMinPointsTalon) {

        }
        */
    }

    protected void end() {
        // Stop the processing buffers
        notifier.stop();

        System.out.println("end");
        RobotMap.driveFrontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
        RobotMap.driveFrontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
        System.out.println("Motion profile finished");
    }

    protected void interrupted() {
        end();
    }

    protected boolean isFinished() {
        RobotMap.driveFrontRight.getMotionProfileStatus(status);
        System.out.println("Error: " + RobotMap.driveFrontLeft.getClosedLoopError(0));
        //System.out.println("status: " + status.activePointValid);
        //System.out.println("isLast: " + status.isLast);
        return RobotMap.driveFrontLeft.getClosedLoopError(0) == 0;
    }

    /**
     * Loops through each segment in trajectory and pushes it into talon's buffer
     * @param trajectory input trajectory to loop through
     * @param talon the talon to trajectory into
     */
    private void pushTrajectory(Trajectory trajectory, WPI_TalonSRX talon) {
        TrajectoryPoint point = new TrajectoryPoint();

        for (int i = 0; i < trajectory.length(); i++) {
            //System.out.println(i);

            Trajectory.Segment segment = trajectory.get(i);

            // Configure point for talon
            double positionF = segment.position;
            double velocityFPS = segment.velocity;
            point.position = positionF * DriveControl.ticksPerFoot * DriveControl.getScaleTicksPerFoot(); // Convert feet to ticks
            point.velocity = velocityFPS * DriveControl.ticksPerFoot * DriveControl.getScaleTicksPerFoot()  / 100d; // Convert Feet per Second to ticks/100ms

            // Not functional yet (reference dumpster fire)
            point.headingDeg = 0;
            point.profileSlotSelect0 = 0;
            point.profileSlotSelect1 = 0;
            point.timeDur = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_0ms;

            // Record whether a segment is first or last
            point.zeroPos = i == 0;
            point.isLastPoint = (i + 1) == trajectory.length();


            System.out.print("pos: " + point.position);
            System.out.print( " velo: " + point.velocity);
            System.out.print( " heading: " + point.headingDeg);
            System.out.print(" profile0: " + point.profileSlotSelect0);
            System.out.print(" profile1: " + point.profileSlotSelect1);
            System.out.print(" zero: " + point.zeroPos);
            System.out.println(" is last point: " + point.isLastPoint);


            // Finally push into the talon
            talon.pushMotionProfileTrajectory(point);
        }
    }
}
