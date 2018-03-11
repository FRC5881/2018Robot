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
    /**
     * The number of ticks the arm can travel downwards ~95 degrees from vertical
     */
    private static final int maxTicks = 1800 + 360;

    /**
     * The return ticks (how close the arm can get to the elevator)
     */
    private static final int minTicks = 0 - 360;

    /**
     * The deadzone on the joystick inputs
     */
    private static final double deadzone = 0.25;


    // ---- Subsystem control ---- //

    /**
     * Create the subsystem with a default name
     */
    public Arm() {
        super();
        init();
    }

    /**
     * Create the subsystem with a given name
     */
    public Arm(String name) {
        super(name);
        init();
    }

    /**
     * Starts a command on init of subsystem, defining commands in robot and OI is preferred
     */
    @Override
    protected void initDefaultCommand() {

    }

    /**
     * Initialize SmartDashboard and other local variables
     */
    public void init() {
        // Set the arm to be in Position mode
        armTalon.set(ControlMode.Position.value);

        SmartDashboard.putNumber("Arm kP", 2.0);
        SmartDashboard.putNumber("Arm kI", 0);
        SmartDashboard.putNumber("Arm kD", 20);
        SmartDashboard.putNumber("Arm kF", 0.076);

        SmartDashboard.putNumber("Arm Speed", 25);
        SmartDashboard.putNumber("Arm Allowed Error", 50);

        // CTRE soft limits, don't work well with current controls
        //setSoftLimitThresholds(0, maxTicks);
        //enableSoftLimits(false);

        initPID();
    }

    /**
     * Initialize PID values to be ready for action
     */
    public void initPID() {
        // Reset PID controls
        armTalon.config_kP(0, getArm_kP(), 10);
        armTalon.config_kI(0, getArm_kI(), 10);
        armTalon.config_kD(0, getArm_kD(), 10);
        armTalon.config_kF(0, getArm_kF(), 10);

        armTalon.configAllowableClosedloopError(0, (int)getAllowedError(),0);

        // Reset PID control
        // Set the arm to be in position mode again to be safe
        armTalon.set(ControlMode.Position.value);

        // Reset encoder
        armTalon.setSelectedSensorPosition(0, 0, 10);

        // Tell the pid control to stay where it is
        armTalon.pidWrite(0);
    }

    // ---- Drive ---- //

    /**
     * Drive the arm with controller input
     */
    public void driveControllerInput() {
        // Negate the inputs so pushing stick forward is negative
        double speed = -Robot.oi.coPilotController.getRawAxis(OI.PILOT_X_AXIS);

        // The deadzone we apply on the controller input
        speed = OI.applyDeadband(speed, deadzone);

        // Scale by extend ticks and set setpoint
        addPosition(speed * getMaxSpeed());
    }

    /**
     * Stops elevator motors
     */
    public void stop() {
        armTalon.stopMotor();
    }

    /**
     * Adds ticks to the current setpoint
     * @param ticks encoder ticks to add
     */
    private void addPosition(double ticks) {
        double setpoint = getSetpoint() + ticks;
        setSetpoint(setpoint);
    }

    // ---- Setters for PID ---- //

    public void setSetpoint(double setpoint) {
        // Soft limit control
        if (setpoint < minTicks) setpoint = minTicks;
        if (setpoint > maxTicks) setpoint = maxTicks;
        armTalon.set(ControlMode.Position, (int)setpoint);
    }

    // Soft limits don't work so well with current drive method
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

    public double getMaxSpeed() {
        return SmartDashboard.getNumber("Arm Speed", 25);
    }

    public double getAllowedError() {
        return SmartDashboard.getNumber("Arm Allowed Error", 50);
    }
}