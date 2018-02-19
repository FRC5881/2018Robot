package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;

public class Arm extends Subsystem {
    /**
     * Percentage to run arm motors
     */
    private static final double deadZone = 1 / 5;

    private static final double armSpeed = 0.5;

    public Arm() {
        super();
    }

    public Arm(String name) {
        super(name);
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void init() {
        SmartDashboard.getNumber("Arm kP", 2.0);
        SmartDashboard.getNumber("Arm kI", 0);
        SmartDashboard.getNumber("Arm kD", 20);
        SmartDashboard.getNumber("Arm kF", 0.076);
    }

    public void initPID() {
        RobotMap.armTalon.config_kP(0, getArm_kP(), 10);
        RobotMap.armTalon.config_kI(0, getArm_kI(), 10);
        RobotMap.armTalon.config_kD(0, getArm_kD(), 10);
        RobotMap.armTalon.config_kF(0, getArm_kF(), 10);
    }

    private void move(double speed) {
        RobotMap.armTalon.set(ControlMode.PercentOutput, speed);
    }

    public void driveJoystickInput() {
        double y = Robot.oi.xboxController2.getRawAxis(OI.RightYAxis);
        if (Math.abs(y) > deadZone) {
            move(y * armSpeed);
        }
    }

    public void stop() {
        RobotMap.armTalon.stopMotor();
    }

    //----- Getters for PID -----//
    public double getArm_kP() {
        return SmartDashboard.getNumber("Arm kP", 2);
    }

    public double getArm_kI() {
        return SmartDashboard.getNumber("Arm kI", 0);
    }

    public double getArm_kD() {
        return SmartDashboard.getNumber("Arm kD", 20);
    }

    public double getArm_kF() {
        return SmartDashboard.getNumber("Arm kF", 0.076);
    }
}