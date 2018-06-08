package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.elevatorTalonMaster;

/**
 * Controls the elevators position with in which ever direction you move the POV controls on the copilot controller
 * Also holds commonly used positions (scale & switch)
 */
public class Elevator extends Subsystem {
    /**
     * Minimum amount of safe ticks the bot is really bad at reaching zero so keep it a bit above
     */
    public static final int minTicks = 50;

    /**
     * Maximum amount of safe ticks
     */
    public static final int maxTicks = 32800;

    /**
     * Amount of rotations to get to height of switch with a cube
     */
    public static final int switchTicks = 10000;

    /**
     * Amount of rotations to get to height of switch with a cube
     */
    public static final int scaleTicks = 22 * 1440;

    public enum states {
        FLOOR,
        SWITCH,
        SCALE
    }

    public states currentState = states.FLOOR;

    /**
     * Initialize Elevator subsystem with default name
     */
    public Elevator(){
        super();
        init();
    }

    /**
     * Initialize Elevator subsystem with chosen name
     */
    public Elevator(String name){
        super(name);
        init();
    }

    /**
     * Starts a command on init of subsystem, defining commands in robot and OI is preferred
     */
    @Override
    protected void initDefaultCommand() {

    }

    // ---- init ---- //
    /**
     * Initialize SmartDashboard and other local variables
     */
    public void init() {
        // Set the elevator to be in Position mode
        elevatorTalonMaster.set(ControlMode.Position.value);

        SmartDashboard.putNumber("Elevator kP", 2.0);
        SmartDashboard.putNumber("Elevator kI", 0);
        SmartDashboard.putNumber("Elevator kD", 20);
        SmartDashboard.putNumber("Elevator kF", 0.076);

        SmartDashboard.putNumber("Elevator Speed", 300);

        // CTRE soft limits, don't work well with current controls
        setSoftLimitThresholds(minTicks, maxTicks);
        // Keep false
        enableSoftLimits(false);

        initPID();
    }

    /**
     * Initialize PID values to be ready for action
     */
    public void initPID() {
        // Reset the PID controls
        elevatorTalonMaster.config_kP(0, getElevator_kP(), 10);
        elevatorTalonMaster.config_kI(0, getElevator_kI(), 10);
        elevatorTalonMaster.config_kD(0, getElevator_kD(), 10);
        elevatorTalonMaster.config_kF(0, getElevator_kF(), 10);

        // Reset PID control
        // Set the elevator to be in position mode again to be safe
        elevatorTalonMaster.set(ControlMode.Position.value);

        // Reset encoder
        elevatorTalonMaster.setSelectedSensorPosition(0, 0, 10);

        // Tell the pid control to stay where it is
        elevatorTalonMaster.pidWrite(0);
    }

    // ---- Drive --- //
    /**
     * Drive the elevator with controller input
     */
    public void manualDrive() {
        double speed = 100;
        // Check POV is any of the up angles
        if (Robot.oi.driveControllerLeftBumper.get()) {
            addPosition(-speed);
        } else if(Robot.oi.driveControllerRightBumper.get()) {
            addPosition(speed);
        }
    }

    /**
     * Stops elevator motors
     */
    public void stop() {
        elevatorTalonMaster.stopMotor();
    }

    // ---- Auto reach targets ---- //

    /**
     * Sets elevator to target up a level
     */
    public void incrementState() {
        // If it's at the floor goto the switch
        if (currentState.equals(states.FLOOR)) {
            setSwitch();
        // if it's at the switch goto the scale
        } else if (currentState.equals(states.SWITCH)) {
            setScale();
        }
    }

    /**
     * Sets the elevator to target down a level
     */
    public void decreaseState() {
        // if it's at the switch go down to the floor
        if (currentState.equals(states.SWITCH)) {
            setFloor();
        // if it's at the scale go down to the switch
        } else if (currentState.equals(states.SCALE)) {
            setSwitch();
        }
    }

    /**
     * Set elevator setpoint to scale
     */
    public void setScale() {
        currentState = states.SCALE;
        setSetpoint(scaleTicks);
    }

    /**
     * Set elevator setpoint to switch
     */
    public void setSwitch() {
        currentState = states.SWITCH;
        setSetpoint(switchTicks);
    }

    /**
     * Set elevator setpoint to min
     */
    public void setFloor() {
        currentState = states.FLOOR;
        setSetpoint(minTicks);
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
        elevatorTalonMaster.set(ControlMode.Position, setpoint);
    }

    // Soft limits don't work well with current elevator drive
    public void setSoftLimitThresholds(int reverseLimit, int forwardLimit) {
        elevatorTalonMaster.configForwardSoftLimitThreshold(forwardLimit, 10);
        elevatorTalonMaster.configReverseSoftLimitThreshold(reverseLimit, 10);
    }

    public void enableSoftLimits(boolean enable) {
        elevatorTalonMaster.configForwardSoftLimitEnable(enable, 10);
        elevatorTalonMaster.configReverseSoftLimitEnable(enable, 10);
    }

    // ---- Getters for PID ---- //
    public double getElevator_kP() {
        return SmartDashboard.getNumber("Elevator kP", 2.0);
    }

    public double getElevator_kI() {
        return SmartDashboard.getNumber("Elevator kI", 0);
    }

    public double getElevator_kD(){
        return SmartDashboard.getNumber("Elevator kD", 20);
    }

    public double getElevator_kF() {
        return SmartDashboard.getNumber("Elevator kF", 0.076);
    }

    public double getSetpoint() {
        return elevatorTalonMaster.getClosedLoopTarget(0);
    }

    public double getError() {
        return elevatorTalonMaster.getClosedLoopError(0);
    }

    public double getHeight() {
        return elevatorTalonMaster.getSelectedSensorPosition(0);
    }
}
