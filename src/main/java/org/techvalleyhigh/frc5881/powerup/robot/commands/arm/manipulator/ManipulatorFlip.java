package org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class ManipulatorFlip extends Command {
    public ManipulatorFlip() {
        requires(Robot.manipulator);
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
    }

    /**
     * Called repeatedly when this Command is scheduled to run (just once)
     */
    @Override
    protected void execute() {
        if (Robot.manipulator.getGrabberEnabled()) {
            Robot.manipulator.closeGrabbers();
        } else {
            Robot.manipulator.openGrabbers();
        }
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * This is a pneumatic command so needs to end instantly
     */
    @Override
    protected boolean isFinished() {
        return true;
    }

    /**
     * Called once after isFinished returns true OR the command is interrupted
     */
    @Override
    protected void end() {
        //Robot.manipulator.stop();
    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {
        System.out.println("Manipulator flip command was interrupted... That shouldn't happen");
        end();
    }
}

