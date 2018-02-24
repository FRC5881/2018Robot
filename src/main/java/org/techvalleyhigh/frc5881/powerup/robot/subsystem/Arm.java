package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.armTalon;

/**
 * Controls robot's arm motors
 */
public class Arm extends Subsystem {
    // Joystick dead zone
    private static final double deadZone = 0.1;

    /**
     * The number of ticks the arm can travel downwards
     */
    private static final int extendedTicks = 2880;

    // ---- Subsystem control ---- //
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

        initPID();
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

    // ---- Drive ---- //
    public void driveControllerInput() {
        // We want to map -1 -> 1 range on joystick inputs to 0 -> extendTicks for pid control
        // Make forward positive and can range 0 -> 1
        double y = (1 - Robot.oi.pilotController.getRawAxis(OI.PILOT_Y_AXIS)) / 2;

        // Scale by extend ticks and set setpoint
        setSetpoint(y * extendedTicks);
    }

    public void stop() {
        armTalon.stopMotor();
    }

    // ---- Setters for PID ---- //
    public void setSetpoint(double setpoint) {
        if (setpoint < 0) setpoint = 0;
        if (setpoint > extendedTicks) setpoint = extendedTicks;
        armTalon.set(ControlMode.Position, setpoint);
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
    public double getSetpoint() {
        return armTalon.getClosedLoopTarget(0);
    }

    public double getError() {
        return armTalon.getClosedLoopError(0);
    }

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