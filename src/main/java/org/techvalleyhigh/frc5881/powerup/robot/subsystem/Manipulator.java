package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.grabSolenoid;

public class Manipulator extends Subsystem {
    /**
     * Create the subsystem with default name
     */
    public Manipulator() {
        super();
    }

    /**
     * Create the subsystem with default name
     */
    public Manipulator(String name) {
        super(name);
    }

    /**
     * Initialize SmartDashboard and other local variables
     */
    public void init() {
        // We want to close the grabbers to grab the cube at the start of the match
        closeGrabbers();
    }

    /**
     * Starts a command on init of subsystem, defining commands in robot and OI is preferred
     */
    @Override
    protected void initDefaultCommand() {

    }

    /**
     * Open the manipulator grabbers
     */
    public void openGrabbers(){
        grabSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Close the manipulator grabbers
     */
    public void closeGrabbers(){
        grabSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Set the grabber to Off
     */
    public void stop(){
        grabSolenoid.set(DoubleSolenoid.Value.kOff);
    }

    /**
     * Get the current state of the ratchet
     * @return boolean true enabled - false disabled
     */
    public boolean getGrabberEnabled() {
        return grabSolenoid.get() == DoubleSolenoid.Value.kForward;
    }

}
