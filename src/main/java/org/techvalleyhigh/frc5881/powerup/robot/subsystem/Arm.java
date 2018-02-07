package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;

public class Arm extends Subsystem {
    /**
     * Percentage to run arm motors
     */
    private static final double deadZone = 1/5;

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

    private void move(double speed){
        RobotMap.armTalon.set(ControlMode.PercentOutput, speed);
    }

    public void driveJoystickInput() {
        double y = Robot.oi.xboxController2.getRawAxis(OI.RightYAxis);
        if (Math.abs(y) > deadZone) {
            move(y * armSpeed);
        }
    }
    public void stop(){
        RobotMap.armTalon.stopMotor();
    }
}
