package org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ratchet;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class DisableRatchet extends Command {

    public DisableRatchet(){
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        Robot.elevator.disableRatchet();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {

    }

    @Override
    protected void interrupted() {
        end();
    }
}
