package org.techvalleyhigh.frc5881.powerup.robot.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class ManipulatorOpen extends Command {

    @Override
    protected synchronized void requires(Subsystem subsystem) {
        super.requires(Robot.manipulator);
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
        return false;
    }

    @Override
    protected void end() {
        Robot.manipulator.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
