package org.techvalleyhigh.frc5881.powerup.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class ElevatorDisableRatchet extends Command {

    public ElevatorDisableRatchet(){
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void execute() {
        Robot.elevator.disableRatchet();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected void interrupted() {
        super.interrupted();
    }

    @Override
    protected synchronized boolean isTimedOut() {
        return super.isTimedOut();
    }
}
