package org.techvalleyhigh.frc5881.powerup.robot.commands;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.DriveControl;

import java.util.ArrayList;


/**
 * Command to handle autonomous motion profiling
 * Takes in array of waypoints, converts them to trajectories, floods drive talons
 * motion profile buffers with trajectory segments, tells the talons to start driving
 */
public class MotionProfile extends Command {
    private static int kMinPointsTalon = 5;

    private Waypoint[] waypoints;
    private MotionProfileStatus status = new MotionProfileStatus();
    private ArrayList<TrajectoryPoint> left = new ArrayList<>();
    private ArrayList<TrajectoryPoint> right = new ArrayList<>();
    private Integer bufferedLeft = 0;
    private Integer bufferedRight = 0;

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
        // Init PID controls
        Robot.driveControl.initPID();

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
                Trajectory.Config.SAMPLES_HIGH, 0.02, Robot.driveControl.getVelocity(), Robot.driveControl.getAcceleration(), 60);

        // Generate trajectory
        Trajectory trajectory = Pathfinder.generate(this.waypoints, config);

        // Change trajectory into a tank drive
        // Wheelbase Width = 2.3226166667 feet
        TankModifier modifier = new TankModifier(trajectory).modify(2.3226166667);

        // Separate trajectories for left and right
        Trajectory leftTrajectory = modifier.getLeftTrajectory();
        Trajectory rightTrajectory = modifier.getRightTrajectory();

        // Push trajectories into the talons
        pushTrajectory(leftTrajectory, left);
        pushTrajectory(rightTrajectory, right);

        // Make sure there's some points in the buffer before going at it
        RobotMap.driveFrontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
        RobotMap.driveFrontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
    }

    protected void execute() {
        MotionProfileStatus leftStatus = new MotionProfileStatus();
        RobotMap.driveFrontLeft.getMotionProfileStatus(leftStatus);
        MotionProfileStatus rightStatus = new MotionProfileStatus();
        RobotMap.driveFrontRight.getMotionProfileStatus(rightStatus);

        SmartDashboard.putNumber("Right Error", RobotMap.driveFrontRight.getClosedLoopError(0));
        SmartDashboard.putNumber("Left Error", RobotMap.driveFrontLeft.getClosedLoopError(0));

        for (int i = 0; i < leftStatus.topBufferRem && bufferedLeft < left.size(); i++) {
            //System.out.println(leftStatus.topBufferRem + " " + leftStatus.btmBufferCnt);
            //System.out.println("Time " + System.nanoTime());
            MotionProfileStatus test = new MotionProfileStatus();
            RobotMap.driveFrontLeft.getMotionProfileStatus(test);
            //System.out.println(test.isLast);

            RobotMap.driveFrontLeft.pushMotionProfileTrajectory(left.get(bufferedLeft));
            bufferedLeft++;
        }

        for (int i = 0; i < rightStatus.topBufferRem && bufferedRight < right.size(); i++) {
            RobotMap.driveFrontRight.pushMotionProfileTrajectory(right.get(bufferedRight));
            bufferedRight++;
        }
    }

    protected void end() {
        // Stop the processing buffers
        notifier.stop();

        System.out.println("end");
        //System.out.println(RobotMap.driveFrontRight.getSelectedSensorPosition(0));
        RobotMap.driveFrontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
        RobotMap.driveFrontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
        Robot.driveControl.stopDrive();
        System.out.println("Motion profile finished");
    }

    protected void interrupted() {
        end();
    }

    protected boolean isFinished() {
        MotionProfileStatus status = new MotionProfileStatus();
        RobotMap.driveFrontLeft.getMotionProfileStatus(status);

        //System.out.println(bufferedLeft);
        System.out.println(status.btmBufferCnt);
        System.out.println("isLast: " + status.isLast);
        System.out.println("activeValid: " + status.activePointValid);
        return status.btmBufferCnt == 0;
//        return status.isLast && status.activePointValid && bufferedLeft == left.size();

        //System.out.println("Error: " + RobotMap.driveFrontLeft.getClosedLoopError(0) + " , " + RobotMap.driveFrontRight.getClosedLoopError(0));

        //RobotMap.driveFrontRight.getMotionProfileStatus(status);

        //return Math.abs(RobotMap.driveFrontLeft.getClosedLoopError(0)) <= Robot.driveControl.getAllowed_Error()
        //        || Math.abs(RobotMap.driveFrontRight.getClosedLoopError(0)) < Robot.driveControl.getAllowed_Error();
    }

    /**
     * Loops through each segment in trajectory and pushes it into talon's buffer
     * @param trajectory input trajectory to loop through
     */
    private void pushTrajectory(Trajectory trajectory, ArrayList<TrajectoryPoint> list) {
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

            /*
            System.out.print("pos: " + point.position);
            System.out.print( " velo: " + point.velocity);
            System.out.print( " heading: " + point.headingDeg);
            System.out.print(" profile0: " + point.profileSlotSelect0);
            System.out.print(" profile1: " + point.profileSlotSelect1);
            System.out.print(" zero: " + point.zeroPos);
            System.out.println(" is last point: " + point.isLastPoint);
            */
            list.add(point);
        }

        System.out.println("list " + list.size());
    }
}
