package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.profiles;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.MotionUtil;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

public class PositionProfile extends Command {
    private double[] leftPoints;
    private double[] rightPoints;
    private int current;

    private static final int target = 30;

    public PositionProfile(Autonomous auto) {
        System.out.println("Position Profile Constructing...");

        // Get pid values
        Robot.driveControl.initPID();

        // Lets see if we can load a stored trajectory....
        Trajectory trajectory = TrajectoryUtil.loadTrajectoryFromFile(auto.getAutoNumber());

        // If we were unable to load the trajectory then it'll be null.
        if (trajectory == null) {
            // Generate trajectory
            System.err.println("Unable to load Auto " + auto.getAutoNumber() + " Generating Path...");
            trajectory = Pathfinder.generate(auto.getPath(), auto.getConfig());
        } else {
            System.out.println("Loaded stored path...");
        }

        // Change trajectory into a tank drive
        // Wheelbase Width = 2.3226166667 feet
        TankModifier modifier = new TankModifier(trajectory).modify(2.3226166667);

        // Separate trajectories for left and right
        Trajectory leftTrajectory = modifier.getLeftTrajectory();
        Trajectory rightTrajectory = modifier.getRightTrajectory();

        // Load trajectories into arrays
        leftPoints = MotionUtil.positions(leftTrajectory);
        rightPoints = MotionUtil.positions(rightTrajectory);

        System.out.println("Set points found " + leftPoints.length);
    }

    @Override
    protected void initialize() {
        // We start at the beginning
        current = 0;

        // Make sure the encoders and PID is reset
        RobotMap.driveFrontLeft.set(ControlMode.Position, 0);
        RobotMap.driveFrontRight.set(ControlMode.Position, 0);

        Robot.driveControl.initPID();
    }

    @Override
    protected void execute() {
        // If we have more points to run
        if (current < leftPoints.length) {
            // Put targets on dashboard for debug purposes
            SmartDashboard.putNumber("left target", leftPoints[current]);
            SmartDashboard.putNumber("right target", rightPoints[current]);

            // Feet to travel = Wheel circumference * rotations
            // 1 Rotation = 1440 Ticks
            // 1 foot = 2 * PI * Wheel radius (feet) * 1440 Ticks
            RobotMap.driveFrontLeft.set(ControlMode.Position, leftPoints[current] * 2 * Math.PI * 0.5 * 1440);
            RobotMap.driveFrontRight.set(ControlMode.Position, rightPoints[current] * 2 * Math.PI * 0.5 * 1440);

            // Only continue if we're on target
            if (RobotMap.driveFrontLeft.getClosedLoopError(0) < target && RobotMap.driveFrontRight.getClosedLoopError(0) < target) {
                current++;
            }
        }
    }

    @Override
    protected void end() {
        System.out.println("Auto finished");
    }

    @Override
    protected void interrupted() {
        System.out.println("Auto routine interrupted, that shouldn't happen");
    }

    @Override
    protected boolean isFinished() {
        return current >= leftPoints.length;
    }
}
