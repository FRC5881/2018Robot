package org.techvalleyhigh.frc5881.powerup.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

/**
 * Implements arcadeDrive feature
 */
public class ArcadeDrive extends Command {
    public ArcadeDrive() {
        requires(Robot.driveControl);
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
    }

    /**
     * Called repeatedly when this Command is scheduled to run
      */
    @Override
    protected void execute() {
        Robot.driveControl.arcadeJoystickInputs();
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * Since this is a drive command we never want it to end
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
        System.out.println("Arcade Drive command ended... That shouldn't happen");
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
