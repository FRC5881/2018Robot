package org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class ManipulatorOpen extends Command {
    public ManipulatorOpen() {
        requires(Robot.manipulator);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        Robot.manipulator.openGrabbers();
    }

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

