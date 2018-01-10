package org.techvalleyhigh.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.powerup.robot.Robot;

public class Drive extends Command {
    public Drive() {
        requires(Robot.driveControl);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    // Drive code to run during
    protected void execute() {
        Robot.driveControl.driveJoystickInputs();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveControl.stopDrive();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
