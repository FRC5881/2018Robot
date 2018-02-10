/*package org.techvalleyhigh.frc5881.powerup.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Elevator;

public class ElevatorLift extends Command {
    public ElevatorLift() {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute(){
        Robot.elevator.drive(Elevator.raiseSpeed);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end(){
        Robot.elevator.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }
}*/
