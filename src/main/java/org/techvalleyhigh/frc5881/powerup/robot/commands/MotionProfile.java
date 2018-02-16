package org.techvalleyhigh.frc5881.powerup.robot.commands;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
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

    public MotionProfile(Autonomous auto) {
        requires(Robot.driveControl);

        // Define leading motor controllers
        leftMotor = RobotMap.driveFrontLeft;
        rightMotor = RobotMap.driveFrontRight;

        // ---- Create Trajectories ---- //

        // Generate trajectory
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