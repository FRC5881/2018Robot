package org.techvalleyhigh.frc5881.powerup.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;

public class ElevatorLift extends Command {
    public ElevatorLift() {
        super();
    }

    public ElevatorLift(String name) {
        super(name);
    }

    @Override
    protected void execute() {
        RobotMap.elevatorTalon1.set(1);
        RobotMap.elevatorTalon2.set(ControlMode.Follower, 1);
    }
    @Override
    protected boolean isFinished() {
        return false;
    }
}
