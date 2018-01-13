package org.techvalleyhigh.frc5881.powerup.robot.commands.Elevator;

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
        RobotMap.elevatorTalon2.set(1);
    }
    @Override
    protected boolean isFinished() {
        return false;
    }
    @Override
    protected void end() {
        RobotMap.elevatorTalon1.set(0);
        RobotMap.elevatorTalon2.set(0);
    }
}
