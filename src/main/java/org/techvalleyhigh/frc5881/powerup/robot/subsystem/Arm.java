package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;

public class Arm extends Subsystem {
    /**
     * Percentage to run arm motors
     */
    public static final double armSpeed = 0.5;

    public Arm() {
        super();
    }

    public Arm(String name) {
        super(name);
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void drive(double speed){
        RobotMap.armTalon.set(speed);
    }

    public void stop(){
        RobotMap.armTalon.stopMotor();
    }
}
