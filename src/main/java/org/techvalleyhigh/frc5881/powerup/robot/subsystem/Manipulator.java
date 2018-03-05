package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorOpen;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.grabDoubleSolenoid;

public class Manipulator extends Subsystem {
    public Manipulator() {
        super();
    }

    public Manipulator(String name) {
        super(name);
    }

    @Override
    protected void initDefaultCommand() {
        new ManipulatorOpen();
    }

    public void openGrabbers(){
        grabDoubleSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void closeGrabbers(){
        grabDoubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void stop(){
        grabDoubleSolenoid.set(DoubleSolenoid.Value.kOff);
    }

    public boolean getGrabberEnabled() {
        return grabDoubleSolenoid.get() == DoubleSolenoid.Value.kForward;
    }
}
