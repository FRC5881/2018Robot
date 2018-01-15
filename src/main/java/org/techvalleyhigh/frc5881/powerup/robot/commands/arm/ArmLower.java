package org.techvalleyhigh.frc5881.powerup.robot.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Arm;


public class ArmLower extends Command {
    public ArmLower() {
        requires(Robot.arm);
    }

    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    // Drive code to run during
    @Override
    protected void execute() {
        Robot.arm.move(-Arm.armSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.arm.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
