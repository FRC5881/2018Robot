package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ratchet.ElevatorEnableRatchet;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.elevatorTalonMaster;
import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.elevatorPancakeDoubleSolenoid;

/**
 * Runs elevator either up to next level(switch or scale), down to next level or in which ever direction you move the thumb stick (Y-axis)
 */
public class Elevator extends Subsystem {
    /**
     * Minimum amount of safe rotations
     */
    private static final double minSafeRotations = 0;

    /**
     * Maximum amount of safe rotations
     */
    private static final double maxSafeRotations = 10.0;

    //TODO: Find how many rotations it takes to get to switch
    /**
     * Amount of rotations to get to height of switch with a cube
     */
    private static final double switchTicks = 2 * 1440;

    //TODO: Find how many rotations it takes to get elevator to scale
    /**
     * Amount of rotations to get to height of switch with a cube
     */
    private static final double scaleTicks = 9 * 1440;

    private static final int baseSpeed = 1;

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
        new ElevatorEnableRatchet();
    }

    // ---- init ---- //
    public void init() {
        enableRatchet();
        elevatorTalonMaster.setSelectedSensorPosition(0, 0, 20);
        elevatorTalonMaster.set(ControlMode.Position.value);

        SmartDashboard.putNumber("Elevator kP", 2.0);
        SmartDashboard.putNumber("Elevator kI", 0);
        SmartDashboard.putNumber("Elevator kD", 20);
        SmartDashboard.putNumber("Elevator kF", 0.076);
    }

    public void initPID() {
        elevatorTalonMaster.config_kP(0, getElevator_kP(), 10);
        elevatorTalonMaster.config_kI(0, getElevator_kI(), 10);
        elevatorTalonMaster.config_kD(0, getElevator_kD(), 10);
        elevatorTalonMaster.config_kF(0, getElevator_kF(), 10);

        elevatorTalonMaster.set(ControlMode.Position.value);
        elevatorTalonMaster.setSelectedSensorPosition(0, 0, 10);
        elevatorTalonMaster.pidWrite(0);
    }

    // ---- Drive --- //
    public void driveControllerInput() {
        int pov = Robot.oi.pilotController.getPOV();
        int speed = 0;
        if (pov == 315 || pov == 0 || pov == 45) {
            speed = baseSpeed;
        } else if(pov == 225 || pov == 180 || pov == 135){
            speed = baseSpeed;
        }
        speed *= (1 + Robot.oi.pilotController.getRawAxis(OI.PILOT_SLIDER)) / 2;

        addPosition(speed * 770);
        //elevatorTalonMaster.set(ControlMode.PercentOutput, speed);
    }

    // Stops motor
    public void stop() {
        elevatorTalonMaster.stopMotor();
    }

    // ---- Auto targets ---- //
    public void setScale() {
        setSetpoint(scaleTicks);
    }

    public void setSwitch() {
        setSetpoint(switchTicks);
    }

    private void addPosition(double ticks) {
        double setpoint = getSetpoint() + ticks;
        setSetpoint(setpoint);
    }

    // ---- ratchet ---- //
    public void enableRatchet(){
        elevatorPancakeDoubleSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void disableRatchet(){
        elevatorPancakeDoubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public boolean getRatchetEnabled() {
        return elevatorPancakeDoubleSolenoid.get() == DoubleSolenoid.Value.kForward;
    }

    // ---- Setters for PID ---- //
    public void setSetpoint(double setpoint) {
        elevatorTalonMaster.pidWrite(setpoint);
    }

    public void setSoftLimitThresholds(int forwardLimit, int reverseLimit) {
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

    public double getHeight() {
        return elevatorTalonMaster.getSelectedSensorPosition(0);
    }
}
