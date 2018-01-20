package org.techvalleyhigh.frc5881.powerup.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class ElevatorLowerAuto extends Command{

    public ElevatorLowerAuto() {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        Robot.elevator.nextBelowDescend();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
       Robot.elevator.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
