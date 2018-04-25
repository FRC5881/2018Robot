package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.profiles.motion_profile.MotionConstants;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.profiles.motion_profile.MotionProfileExample;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.MotionUtil;

// TODO: Work in progress
public class StraightProfile extends Command {
    private WPI_TalonSRX rightMotor;
    private WPI_TalonSRX leftMotor;

    private MotionProfileExample rightProfile;
    private MotionProfileExample leftProfile;

    private double distance;
    private double velocity;
    private double acceleration;
    private double dt;

    public StraightProfile(double distance, double velocity, double acceleration, double dt) {
        requires(Robot.driveControl);
        this.distance = distance;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.dt = dt;

         rightMotor = RobotMap.driveFrontRight;
         leftMotor = RobotMap.driveFrontLeft;
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        System.out.println("Driving " + distance + " feet");

        double[][] points = MotionUtil.straightPath(distance, velocity, acceleration, dt);

        double startAngle = Robot.driveControl.getGyroAngle();

        leftProfile = new MotionProfileExample(leftMotor, points, true, startAngle);
        rightProfile = new MotionProfileExample(rightMotor, points, false, startAngle);

        // Set timing for profile
        int time = Double.valueOf(dt * 1000).intValue();

        rightMotor.configMotionProfileTrajectoryPeriod(time, MotionConstants.kTimeoutMs);
        leftMotor.configMotionProfileTrajectoryPeriod(time, MotionConstants.kTimeoutMs);

        /*
         * status 10 provides the trajectory target for auto profile AND
         * profiles magic
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
     * End command once we are within tolerance
     */
    @Override
    protected boolean isFinished() {
        return false;
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