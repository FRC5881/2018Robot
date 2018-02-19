package org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class ManipulatorClose extends Command {
    public ManipulatorClose() {
        requires(Robot.manipulator);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        Robot.manipulator.closeGrabbers();
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     */
    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
        //Robot.manipulator.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
