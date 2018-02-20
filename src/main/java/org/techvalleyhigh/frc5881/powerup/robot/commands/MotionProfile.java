package org.techvalleyhigh.frc5881.powerup.robot.commands;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.commands.motion.Constants;
import org.techvalleyhigh.frc5881.powerup.robot.commands.motion.MotionProfileExample;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.JaciToTalon;

public class MotionProfile extends Command {
    private WPI_TalonSRX rightMotor;
    private WPI_TalonSRX leftMotor;
    private MotionProfileExample rightProfile;
    private MotionProfileExample leftProfile;
    private Autonomous auto;

    /**
     * gyrpPID outputs a value that needs to be applied to the power of drive motors to adjust heading
     * -1 * output to left and output to right
     */
    private PIDController gyroPID;

    public MotionProfile(Autonomous auto) {
        System.out.println("MotionProfile Constructed");
        requires(Robot.driveControl);
        this.auto = auto;
    }

    @Override
    protected void initialize() {
        // Get pid values
        Robot.driveControl.initPID();

        // Define leading motor controllers
        leftMotor = RobotMap.driveFrontLeft;
        rightMotor = RobotMap.driveFrontRight;

        // ---- Create Trajectories ---- //
        // Generate trajectory
        System.out.println("Generating Path...");
        long startTime = System.currentTimeMillis();

        Trajectory trajectory = Pathfinder.generate(auto.getPath(), auto.getConfig());

        // Change trajectory into a tank drive
        // Wheelbase Width = 2.3226166667 feet
        TankModifier modifier = new TankModifier(trajectory).modify(2.3226166667);

        // Separate trajectories for left and right
        Trajectory leftTrajectory = modifier.getLeftTrajectory();
        Trajectory rightTrajectory = modifier.getRightTrajectory();

        // Convert to points
        double[][] leftPoints = JaciToTalon.makeProfile(leftTrajectory);
        double[][] rightPoints = JaciToTalon.makeProfile(rightTrajectory);

        long endTime = System.currentTimeMillis();
        System.out.println("It took " + (startTime - endTime) + " Milliseconds");

        // Init profiles
        leftProfile = new MotionProfileExample(leftMotor, leftPoints, true);
        rightProfile = new MotionProfileExample(rightMotor, rightPoints, false);

        // Convert seconds to milliseconds
        int time = Double.valueOf(auto.getConfig().dt * 1000).intValue();

        // Set timing for profile
        rightMotor.configMotionProfileTrajectoryPeriod(time, Constants.kTimeoutMs);
        leftMotor.configMotionProfileTrajectoryPeriod(time, Constants.kTimeoutMs);

        /*
         * status 10 provides the trajectory target for motion profile AND
         * motion magic
         */
        rightMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, time, Constants.kTimeoutMs);
        leftMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, time, Constants.kTimeoutMs);

        leftProfile.startMotionProfile();
        rightProfile.startMotionProfile();
    }

    @Override
    protected void execute() {
        //call this periodically, and catch the output.
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

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        System.out.println("Motion profile ended that shouldn't happen...");
    }

    @Override
    protected void interrupted() {
        end();
    }
}