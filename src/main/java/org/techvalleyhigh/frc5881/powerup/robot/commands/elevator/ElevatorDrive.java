package org.techvalleyhigh.frc5881.powerup.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class ElevatorDrive extends Command {
    public ElevatorDrive() {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize() {
        System.out.println("Initializing elevator command");
    }

    @Override
    protected void execute(){
        Robot.elevator.driveControllerInput();

        if (Robot.oi.coPilotTopBackLeft.get()) {
            Robot.elevator.setSwitch();
        } else if (Robot.oi.coPilotTopBackRight.get()) {
            Robot.elevator.setScale();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end(){
        System.out.println("Elevator drive command ended... That shouldn't happen");
        Robot.elevator.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }
}

