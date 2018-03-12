package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.motion;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.JaciToTalon;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

/**
 * Command to call to interact with CTRE Motion Profile Example
 */
public class MotionProfile extends Command {
    private WPI_TalonSRX rightMotor;
    private WPI_TalonSRX leftMotor;
    private MotionProfileExample rightProfile;
    private MotionProfileExample leftProfile;
    private Autonomous auto;

    public MotionProfile(Autonomous auto) {
        System.out.println("MotionProfile Constructed");
        requires(Robot.driveControl);
        this.auto = auto;
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        // Get pid values
        Robot.driveControl.initPID();

        // Define leading motor controllers
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
        double[][] leftPoints = JaciToTalon.makeProfile(leftTrajectory);
        double[][] rightPoints = JaciToTalon.makeProfile(rightTrajectory);

        // Prints seconds rounded to 2 decimal places
        long endTime = System.currentTimeMillis();
        System.out.println("It took " + Math.round((endTime - startTime) * 100) / 100000 + " Seconds");

        double startAngle = Robot.driveControl.getGyroAngle();

        // Init profiles
        leftProfile = new MotionProfileExample(leftMotor, leftPoints, true, startAngle);
        rightProfile = new MotionProfileExample(rightMotor, rightPoints, false, startAngle);

        // Convert seconds to milliseconds
        int time = Double.valueOf(auto.getConfig().dt * 1000).intValue();

        // Set timing for profile
        rightMotor.configMotionProfileTrajectoryPeriod(time, MotionConstants.kTimeoutMs);
        leftMotor.configMotionProfileTrajectoryPeriod(time, MotionConstants.kTimeoutMs);

        /*
         * status 10 provides the trajectory target for auto profile AND
         * motion magic
         */
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, time, MotionConstants.kTimeoutMs);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, time, MotionConstants.kTimeoutMs);

        leftProfile.startMotionProfile();
        rightProfile.startMotionProfile();
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    @Override
    protected void execute() {
        // Call this periodically
        leftProfile.control();
        rightProfile.control();

        // Put SmartDashboard values for testing
        SmartDashboard.putNumber("target left", leftMotor.getActiveTrajectoryPosition());
        SmartDashboard.putNumber("target right", rightMotor.getActiveTrajectoryPosition());

        SmartDashboard.putNumber("Left Error", leftMotor.getClosedLoopError(0));
        SmartDashboard.putNumber("Right Error", rightMotor.getClosedLoopError(0));

        SmartDashboard.putNumber("left speed", leftMotor.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("right speed", rightMotor.getSelectedSensorVelocity(0));

        SmartDashboard.putNumber("Target left speed", leftMotor.getActiveTrajectoryVelocity());
        SmartDashboard.putNumber("Target right speed", rightMotor.getActiveTrajectoryVelocity());

        // Control motors
        SetValueMotionProfile leftSetOutput = leftProfile.getSetValue();
        SetValueMotionProfile rightSetOutput = rightProfile.getSetValue();

        leftMotor.set(ControlMode.MotionProfile, leftSetOutput.value);
        rightMotor.set(ControlMode.MotionProfile, rightSetOutput.value);
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * The command is finished when the profile set values are Hold
     */
    @Override
    protected boolean isFinished() {
        return leftProfile.getSetValue() == SetValueMotionProfile.Hold
                && rightProfile.getSetValue() == SetValueMotionProfile.Hold;
    }

    /**
     * Called once after isFinished returns true OR the command is interrupted
     */
    @Override
    protected void end() {
        System.out.println("Motion profile ended that shouldn't happen...");
    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {
        end();
    }
}