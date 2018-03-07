package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.ratchetSolenoid;

public class Ratchet extends Subsystem {
    /**
     * Create the subsystem with default name
     */
    public Ratchet() {
        super();
        init();
    }

    /**
     * Create the subsystem with the given name.
     */
    public Ratchet(String name) {
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
        // We want ratchet to be enabled at the start of the match
        enableRatchet();
    }

    /**
     * Enable the elevator ratchet
     */
    public void enableRatchet(){
        ratchetSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Disable the elevator ratchet
     */
    public void disableRatchet(){
        ratchetSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Set the ratchet to Off
     */
    public void stop(){
        ratchetSolenoid.set(DoubleSolenoid.Value.kOff);
    }

    /**
     * Get the current state of the ratchet
     * @return boolean true enabled - false disabled
     */
    public boolean getRatchetEnabled() {
        return ratchetSolenoid.get() == DoubleSolenoid.Value.kForward;
    }
}
