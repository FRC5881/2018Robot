package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.leftGrabDoubleSolenoid;
import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.rightGrabDoubleSolenoid;

public class Manipulator extends Subsystem {
    public Manipulator() {
        super();
    }

    public Manipulator(String name) {
        super(name);
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void openGrabbers(){
        rightGrabDoubleSolenoid.set(DoubleSolenoid.Value.kForward);
        leftGrabDoubleSolenoid.set(DoubleSolenoid.Value.kForward);

    }

    public void closeGrabbers(){
        rightGrabDoubleSolenoid.set(DoubleSolenoid.Value.kReverse);
        leftGrabDoubleSolenoid.set(DoubleSolenoid.Value.kReverse);

    }

    public void stop(){
        rightGrabDoubleSolenoid.set(DoubleSolenoid.Value.kOff);
        leftGrabDoubleSolenoid.set(DoubleSolenoid.Value.kOff);
    }
}
