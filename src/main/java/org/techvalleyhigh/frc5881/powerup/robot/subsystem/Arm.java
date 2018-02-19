package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.armTalon;

public class Arm extends Subsystem {
    /**
     * Dead zone for controllers
     */
    private static final double deadZone = 1 / 5;

    /**
     * Percentage to run arm motors
     */
    private static final double armSpeed = 0.5;

    private static final int extendedTicks = 2880;

    public Arm() {
        super();
        init();
    }

    public Arm(String name) {
        super(name);
        init();
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void init() {
        SmartDashboard.putNumber("Arm kP", 2.0);
        SmartDashboard.putNumber("Arm kI", 0);
        SmartDashboard.putNumber("Arm kD", 20);
        SmartDashboard.putNumber("Arm kF", 0.076);
    }

    public void initPID() {
        armTalon.config_kP(0, getArm_kP(), 10);
        armTalon.config_kI(0, getArm_kI(), 10);
        armTalon.config_kD(0, getArm_kD(), 10);
        armTalon.config_kF(0, getArm_kF(), 10);

        setSoftLimitThresholds(0, extendedTicks);
        enableSoftLimits(true);

        armTalon.set(ControlMode.Position.value);
        armTalon.setSelectedSensorPosition(0, 0, 10);
        armTalon.pidWrite(0);
    }

    public void driveControllerInput() {
        double y = (1 + Robot.oi.pilotController.getRawAxis(OI.XBOX_RIGHT_Y_AXIS)) / 2;
        setSetpoint(y);
    }

    public void stop() {
        armTalon.stopMotor();
    }

    // ---- Setters for PID ---- //
    public void setSetpoint(double setpoint) {
        armTalon.pidWrite(setpoint);
    }

    public void setSoftLimitThresholds(int forwardLimit, int reverseLimit) {
        armTalon.configForwardSoftLimitThreshold(forwardLimit, 10);
        armTalon.configReverseSoftLimitThreshold(reverseLimit, 10);
    }

    public void enableSoftLimits(boolean enable) {
        armTalon.configForwardSoftLimitEnable(enable, 10);
        armTalon.configReverseSoftLimitEnable(enable, 10);
    }

    // ----- Getters for PID ----- //
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

    public double getSetpoint() {
        return armTalon.getClosedLoopTarget(0);
    }
}