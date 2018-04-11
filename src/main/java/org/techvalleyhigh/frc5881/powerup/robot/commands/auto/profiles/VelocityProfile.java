package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.profiles;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.MotionUtil;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

public class VelocityProfile extends Command {
    private WPI_TalonSRX rightMotor;
    private WPI_TalonSRX leftMotor;

    private double[] leftProfile;
    private double[] rightProfile;

    private int current;

    public VelocityProfile(Autonomous auto) {
        System.out.println("Velocity Profile Constructed...");

        // Let everyone know we're using drive control and too back off
        requires(Robot.driveControl);

        leftMotor = RobotMap.driveFrontLeft;
        rightMotor = RobotMap.driveFrontRight;

        // Log the start time of the process....
        long startTime = System.currentTimeMillis();

        // Lets see if we can load a stored trajectory....
        Trajectory trajectory = TrajectoryUtil.loadTrajectoryFromFile(auto.getAutoNumber());

        // If we were unable to load the trajectory then it'll be null.
        if (trajectory == null) {
            // Generate trajectory
            System.err.println("Unable to load Auto " + auto.getAutoNumber() + " Generating Path...");
            // This is a bit slow but isn't so bad for shorter paths
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

        // Convert to points
        leftProfile = MotionUtil.velocities(leftTrajectory);
        rightProfile = MotionUtil.velocities(rightTrajectory);

        // Prints seconds rounded to 2 decimal places
        long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) + " Milliseconds");
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        current = 0;
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    @Override
    protected void execute() {
        // 1440 units per revolution, 600 "100 milliseconds" in a minute
        leftMotor.set(ControlMode.Velocity, leftProfile[current] * 1440.0 / 600.0);
        rightMotor.set(ControlMode.Velocity, rightProfile[current] * 1440.0 / 600.0);

        if (current < leftProfile.length) {
            current++;
        }
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     */
    @Override
    protected boolean isFinished() {
        return current >= leftProfile.length;
    }

    /**
     * Called once after isFinished returns true OR the command is interrupted
     */
    @Override
    protected void end() {
        System.out.println("Easy Motion finished");
    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {

    }
}
