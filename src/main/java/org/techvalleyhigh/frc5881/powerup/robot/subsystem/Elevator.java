package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.elevatorTalonMaster;

/**
 * Runs elevator either up to next level(switch or scale), down to next level or in which ever direction you move the thumb stick (Y-axis)
 */
public class Elevator extends Subsystem {
    /**
     * Minimum amount of safe ticks
     */
    private static final int minTicks = 50;

    /**
     * Maximum amount of safe ticks
     */
    private static final double maxTicks = 22.5d * 1440;

    //TODO: Find how many rotations it takes to get to switch
    /**
     * Amount of rotations to get to height of switch with a cube
     */
    private static final int switchTicks = 2 * 1440;

    //TODO: Find how many rotations it takes to get elevator to scale
    /**
     * Amount of rotations to get to height of switch with a cube
     */
    private static final int scaleTicks = 22 * 1440;

    private static final int baseSpeed = 1;

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

        setSoftLimitThresholds(minTicks, (int)maxTicks);
        enableSoftLimits(true);

        initPID();
    }

    /**
     * Puts values on Smart Dashboard
     */
    public void initPID() {
        elevatorTalonMaster.config_kP(0, getElevator_kP(), 10);
        elevatorTalonMaster.config_kI(0, getElevator_kI(), 10);
        elevatorTalonMaster.config_kD(0, getElevator_kD(), 10);
        elevatorTalonMaster.config_kF(0, getElevator_kF(), 10);

        // Reset PID control
        elevatorTalonMaster.set(ControlMode.Position.value);
        elevatorTalonMaster.setSelectedSensorPosition(0, 0, 10);
        elevatorTalonMaster.pidWrite(0);
    }

    // ---- Drive --- //
    /**
     * Drive the elevator with joystick input
     */
    public void driveJoystickInputs() {
        // Get POV position from controller
        int pov = Robot.oi.coPilotController.getPOV();
        //System.out.println("\n" + pov);

        int speed = 0;
        if (pov == 315 || pov == 0 || pov == 45) {
            speed = baseSpeed;
        } else if(pov == 225 || pov == 180 || pov == 135){
            speed = -baseSpeed;
        }
        //System.out.println("Speed0 " + speed);
        speed *= (1 + Robot.oi.coPilotController.getRawAxis(OI.PILOT_SLIDER)) / 2;
        //System.out.println("Speed1 " + speed);

        addPosition(speed * 770);
        //elevatorTalonMaster.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Stops elevator motors
     */
    public void stop() {
        elevatorTalonMaster.stopMotor();
    }

    // ---- Auto reach targets ---- //

    /**
     * Set elevator setpoint to scale
     */
    public void setScale() {
        setSetpoint(scaleTicks);
    }

    /**
     * Set elevator setpoint to switch
     */
    public void setSwitch() {
        setSetpoint(switchTicks);
    }

    private void addPosition(double ticks) {
        double setpoint = getSetpoint() + ticks;
        if (setpoint > minTicks && setpoint < maxTicks) {
            setSetpoint(setpoint);
        }
    }



    // ---- Setters for PID ---- //
    public void setSetpoint(double setpoint) {
        elevatorTalonMaster.set(ControlMode.Position, setpoint);
    }

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
}
