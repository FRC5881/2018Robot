package org.techvalleyhigh.frc5881.powerup.robot.utils;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class TalonSRX_PID_Controller implements Sendable {
    private String name;
    private String subsystem;

    private WPI_TalonSRX talon;
    private double kP;
    private double kI;
    private double kD;
    private double kF;

    public TalonSRX_PID_Controller(WPI_TalonSRX talon, double kP, double kI, double kD, double kF) {
        this.talon = talon;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSubsystem() {
        return subsystem;
    }

    @Override
    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    @Override
    public void setName(String subsystem, String name) {
        setName(name);
        setSubsystem(subsystem);
    }

    public double getkP() {
        return kP;
    }

    public void setkP(double kP) {
        this.kP = kP;
        talon.config_kP(0, kP, 10);
    }

    public double getkI() {
        return kI;
    }

    public void setkI(double kI) {
        this.kI = kI;
        talon.config_kI(0, kI, 10);
    }

    public double getkD() {
        return kD;
    }

    public void setkD(double kD) {
        this.kD = kD;
        talon.config_kD(0, kD, 10);
    }

    public double getkF() {
        return kF;
    }

    public void setkF(double kF) {
        this.kF = kF;
        talon.config_kF(0, kF, 10);
    }

    public void reset() {

    }

    public double getSetpoint() {
        return talon.getClosedLoopTarget(0);
    }

    public void setSetpoint(double setpoint) {
        talon.set(setpoint);
    }

    public double getError() {
        return talon.getClosedLoopError(0);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        //builder.setSmartDashboardType("PIDController");

        //builder.addDoubleProperty("p", this::getkP, this::setkP);
        //builder.addDoubleProperty("i", this::getkI, this::setkI);
        //builder.addDoubleProperty("d", this::getkD, this::setkD);
        //builder.addDoubleProperty("f", this::getkF, this::setkF);
        //builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint);
    }
}
