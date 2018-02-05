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

public class MotionProfile extends Command {
    private static int kMinPointsTalon = 5;

    private Waypoint[] waypoints;
    private MotionProfileStatus status = new MotionProfileStatus();

    public MotionProfile(Waypoint[] waypoints) {
        this.waypoints = waypoints;
        requires(Robot.driveControl);
    }

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
                Trajectory.Config.SAMPLES_HIGH, 0.05, 5, 4, 60);

        // Generate trajectory
        Trajectory trajectory = Pathfinder.generate(this.waypoints, config);

        // Change trajectory into a tank drive
        // Wheelbase Width = 2.3226166667 feet
        TankModifier modifier = new TankModifier(trajectory).modify(2.3226166667);

        // Separate trajectories for left and right
        Trajectory leftTrajectory = modifier.getLeftTrajectory();
        Trajectory rightTrajectory = modifier.getRightTrajectory();

        pushTrajectory(leftTrajectory, RobotMap.driveFrontLeft);
        pushTrajectory(rightTrajectory, RobotMap.driveFrontRight);
    }

    protected void execute() {
        if (status.btmBufferCnt > kMinPointsTalon) {
            RobotMap.driveFrontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
            RobotMap.driveFrontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
        }
    }

    protected void end() {
        RobotMap.driveFrontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
        RobotMap.driveFrontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
        System.out.println("Figure 8 finished");
    }

    protected void interrupted() {
        end();
    }

    protected boolean isFinished() {
        return status.activePointValid && status.isLast;
    }

    private void pushTrajectory(Trajectory trajectory, WPI_TalonSRX talon) {
        TrajectoryPoint point = new TrajectoryPoint();

        for (int i = 0; i < trajectory.length(); i++) {
            Trajectory.Segment segment = trajectory.get(i);

            // Configure point for talon
            double positionRot = segment.position;
            double velocityRPM = segment.velocity;
            point.position = positionRot * DriveControl.distancePerTick;
            point.velocity = velocityRPM * 4096d / 600d;

            // Not functional yet (reference dumpster fire)
            point.headingDeg = 0;
            point.profileSlotSelect = 0;

            // Record weather segment is first or last
            point.zeroPos = i == 0;
            point.isLastPoint = (i + 1) == this.waypoints.length;

            // Finally push into the talon
            talon.pushMotionProfileTrajectory(point);
        }
    }
}
