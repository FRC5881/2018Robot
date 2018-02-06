package org.techvalleyhigh.frc5881.powerup.robot.commands;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.DriveControl;

public class FigureEight extends Command {

    private MotionProfileStatus status = new MotionProfileStatus();
    private int kMinPointsTalon = 5;


    public FigureEight() {
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
        // Change control mode
        RobotMap.driveFrontRight.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
        RobotMap.driveFrontLeft.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);

        notifier.startPeriodic(0.005);

        // Set up configs
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 5, 4, 60);

        // Create waypoints
        Waypoint[] points = new Waypoint[] {
                new Waypoint(10, 5, Math.toRadians(0)),
                new Waypoint(15, 10, Math.toRadians(90)),
                new Waypoint(10, 15, Math.toRadians(135)),
                new Waypoint(5, 20, Math.toRadians(90)),
                new Waypoint(10, 25, Math.toRadians(0)),
                new Waypoint(15, 20, Math.toRadians(270)),
                new Waypoint(10, 15, Math.toRadians(-135)),
                new Waypoint(5, 10, Math.toRadians(-90)),
                new Waypoint(10, 5, Math.toRadians(0))
        };

        // Generate trajectory
        Trajectory trajectory = Pathfinder.generate(points, config);

        // Wheelbase Width = 2.3226166667 feet
        TankModifier modifier = new TankModifier(trajectory).modify(2.3226166667);
        Trajectory leftTrajectory = modifier.getLeftTrajectory();
        Trajectory rightTrajectory = modifier.getRightTrajectory();

        TrajectoryPoint pointR = new TrajectoryPoint();
        TrajectoryPoint pointL = new TrajectoryPoint();

        RobotMap.driveBackRight.clearMotionProfileTrajectories();
        RobotMap.driveBackLeft.clearMotionProfileTrajectories();

        for (int i = 0; i < trajectory.length(); i++) {
            // Trajectory
            Trajectory.Segment segmentR = leftTrajectory.get(i);

            double positionRotR = segmentR.position;
            double velocityRPMR = segmentR.velocity;
            pointR.position = positionRotR * DriveControl.distancePerTick;
            pointR.velocity = velocityRPMR * 4096d / 600d;
            pointR.headingDeg = 0;
            pointR.profileSlotSelect0 = 0;

            pointR.zeroPos = i == 0;
            pointR.isLastPoint = (i + 1) == points.length;
            RobotMap.driveFrontRight.pushMotionProfileTrajectory(pointR);

            // Trajectory
            Trajectory.Segment segmentL = rightTrajectory.get(i);

            double positionRotL = segmentL.position;
            double velocityRPML = segmentL.velocity;
            pointL.position = positionRotL * DriveControl.distancePerTick;
            pointL.velocity = velocityRPML * 4096d / 600d;
            pointL.headingDeg = 0;
            pointL.profileSlotSelect0 = 0;

            pointL.zeroPos = i == 0;
            pointL.isLastPoint = (i + 1) == points.length;
            RobotMap.driveFrontLeft.pushMotionProfileTrajectory(pointL);
        }


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
}
