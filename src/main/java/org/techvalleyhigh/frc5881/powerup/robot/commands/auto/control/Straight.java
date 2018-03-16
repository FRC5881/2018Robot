package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.DriveControl;

public class Straight extends Command {
    private double distance;

    public Straight(double feet) {
        requires(Robot.driveControl);
        this.distance = feet * DriveControl.ticksPerFoot;
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        Robot.driveControl.initPID();

        RobotMap.driveFrontLeft.setSelectedSensorPosition(0, 0, 10);
        RobotMap.driveFrontRight.setSelectedSensorPosition(0, 0, 10);

        RobotMap.driveFrontLeft.set(ControlMode.Position, distance);
        RobotMap.driveFrontRight.set(ControlMode.Position, distance);
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */

    @Override
    protected void execute() {
        double leftRemain = distance - RobotMap.driveFrontLeft.getSelectedSensorPosition(0);
        double rightRemain = distance - RobotMap.driveFrontLeft.getSelectedSensorPosition(0);

        if (leftRemain > 200 || rightRemain > 200) {
            RobotMap.driveFrontLeft.set(ControlMode.Position, distance);
            RobotMap.driveFrontRight.set(ControlMode.Position, distance);
        } else {
            RobotMap.driveFrontRight.set(ControlMode.Disabled, 0);
            RobotMap.driveFrontLeft.set(ControlMode.Disabled, 0);
        }
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     */
    @Override
    protected boolean isFinished() {
        return RobotMap.driveFrontLeft.getClosedLoopTarget(0) < 50;
    }

    /**
     * Called once after isFinished returns true OR the command is interrupted
     */
    @Override
    protected void end() {
        Robot.driveControl.stopDrive();
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
