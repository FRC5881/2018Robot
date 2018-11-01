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
    private int timeOut = 0;


    private static final int target = 100;


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
        leftPoints = MotionUtil.positions(leftTrajectory, 10);
        rightPoints = MotionUtil.positions(rightTrajectory, 10);

        System.out.println("Set points found " + leftPoints.length);
    }

    @Override
    protected void initialize() {
        // We start at the beginning
        current = 0;

        // Make sure the encoders and PID is reset
        RobotMap.driveFrontLeft.set(ControlMode.Position, 0);
        RobotMap.driveFrontRight.set(ControlMode.Position, 0);

        RobotMap.driveFrontLeft.configAllowableClosedloopError(0,0, 10);
        RobotMap.driveFrontRight.configAllowableClosedloopError(0,0, 10);

        Robot.driveControl.initPID();
    }

    @Override
    protected void execute() {
        // If we have more points to run
        if (current < leftPoints.length) {
            // Feet to travel = Wheel circumference * rotations
            // 1 Rotation = 1440 Ticks
            // 1 foot = 2 * PI * Wheel radius (feet) * 1440 ticks
            // 1 foot = 2 * PI (1/4) * 1440 ticks
            // 1 foot = PI/2 * 1440 ticks ticks

            RobotMap.driveFrontLeft.set(ControlMode.Position, 0);

            // TODO: uncomment
            //RobotMap.driveFrontLeft.set(ControlMode.Position, rightPoints[current] * 1440 / (Math.PI/2));
            RobotMap.driveFrontRight.set(ControlMode.Position, rightPoints[current] * 1440 / (Math.PI/2));

            // Only continue if we're on target or we've been stationary too long
            if (timeOut > 100 || onTarget()) {
                clearError();
                current++;

                timeOut = 0;
            } else {
                timeOut++;
            }
        }
    }

    /**
     * Sets the encoder position to the target points, effectively clearing the error
     */
    private void clearError() {
        RobotMap.driveFrontRight.setSelectedSensorPosition(0, (int) rightPoints[current], 10);
        RobotMap.driveFrontLeft.setSelectedSensorPosition(0, (int) leftPoints[current], 10);
    }

    /**
     * Returns true if left & right error are less than the target
     */
    public boolean onTarget() {
        return Math.abs(RobotMap.driveFrontLeft.getClosedLoopError(0)) < target
                && Math.abs(RobotMap.driveFrontRight.getClosedLoopError(0)) < target;
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
