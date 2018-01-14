package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.commands.Elevator.ElevatorLift;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.init;

public class Elevator extends Subsystem {
    /**
     * Max height of linear rail
     */
    // TODO: Find a real max height
    private static final double maxSafeRotations = 10.0;

    // Percent to run motors
    public static final double lowerSpeed = -0.5;
    public static final double raiseSpeed = 0.5;

    public Elevator(){
        super();
        init();
    }

    public Elevator(String name){
        super(name);
        init();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ElevatorLift());
    }

    public void drive(double speed) {
        if (speed > 0 && getHeight() < maxSafeRotations) {
            RobotMap.elevatorTalonMaster.set(speed);
        } else if(speed < 0 && getHeight() < 0) {
            RobotMap.elevatorTalonMaster.set(speed);
        }
    }

    public void stop() {
        RobotMap.elevatorTalonMaster.set(0);
    }

    public double getHeight() {
        return RobotMap.elevatorTalonMaster.getSelectedSensorPosition(0);
    }
}
