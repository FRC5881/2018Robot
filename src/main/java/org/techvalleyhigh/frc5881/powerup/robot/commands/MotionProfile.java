package org.techvalleyhigh.frc5881.powerup.robot.commands;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.commands.motion.Constants;
import org.techvalleyhigh.frc5881.powerup.robot.commands.motion.MotionProfileExample;

public class MotionProfile extends Command {
    private WPI_TalonSRX rightMotor;
    private WPI_TalonSRX leftMotor;
    private MotionProfileExample rightProfile;
    private MotionProfileExample leftProfile;

    public MotionProfile(double[][] leftPoints, double[][] rightPoints) {
        requires(Robot.driveControl);

        // Define follow controllers
        leftMotor = RobotMap.driveFrontLeft;
        rightMotor = RobotMap.driveFrontRight;

        // init profiles
        leftProfile = new MotionProfileExample(leftMotor, leftPoints);
        rightProfile = new MotionProfileExample(rightMotor, rightPoints);
    }

    @Override
    protected void initialize() {
        System.out.println("Init profile");
        // Get pid values
        Robot.driveControl.initPID();

        /* Our profile uses 10ms timing */
        rightMotor.configMotionProfileTrajectoryPeriod(10, Constants.kTimeoutMs);
        leftMotor.configMotionProfileTrajectoryPeriod(10, Constants.kTimeoutMs);

        /*
         * status 10 provides the trajectory target for motion profile AND
         * motion magic
         */
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);


        leftProfile.startMotionProfile();
        rightProfile.startMotionProfile();
    }

    @Override
    protected void execute() {
        /*
         * call this periodically, and catch the output. Only apply it if user
         * wants to run MP.
         */
        leftProfile.control();
        rightProfile.control();

        SetValueMotionProfile leftSetOutput = leftProfile.getSetValue();
        SetValueMotionProfile rightSetOutput = rightProfile.getSetValue();

        leftMotor.set(ControlMode.MotionProfile, leftSetOutput.value);
        rightMotor.set(ControlMode.MotionProfile, rightSetOutput.value);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {
        end();
    }
}