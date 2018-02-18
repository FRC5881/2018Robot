package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.elevatorTalonMaster;

/**
 * Runs elevator either up to next level(switch or scale), down to next level or in which ever direction you move the thumb stick (Y-axis)
 */
public class Elevator extends Subsystem {
    /**
     * Minimum amount of safe rotations
     */
    private static final double minSafeRotations = 0;
    // TODO: Find a real max height
    /**
     * Maximum amount of safe rotations
     */
    private static final double maxSafeRotations = 10.0;
    /**
     * Dead zone
     */
    private static final double deadZone = 1/5;
    //TODO: Find how many rotations it takes to get to switch
    /**
     * Amount of rotations to get to height of switch with a cube
     */
    private static final double switchRotations = 2;
    //TODO: Find how many rotations it takes to get elevator to scale
    /**
     * Amount of rotations to get to height of switch with a cube
     */
    private static final double scaleRotations = 9;
    /**
     * Percent of power to run motors at.
     */
    private static final double raiseSpeed = 0.5;

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
    }

    public void init() {
        elevatorTalonMaster.setSelectedSensorPosition(0, 0, 20);
    }

    public void nextUpRise(){
        //if elevator is at top floor don't do anything, else keep going to next level
        /*
         * Supposed to loop until it reaches the next level.
         * But we will find out when we test it.
         */
        if(getHeight() >= maxSafeRotations) {
            elevatorTalonMaster.stopMotor();
        } else if( getHeight() < switchRotations ) {
            elevatorTalonMaster.set(ControlMode.Position, switchRotations);
        } else if(getHeight() < scaleRotations && getHeight() > switchRotations){
            elevatorTalonMaster.set(ControlMode.Position, scaleRotations);
        }
    }
//Meant to descend elevator to next lower level
    public void nextBelowDescend() {
        if (getHeight() <= minSafeRotations) {
            elevatorTalonMaster.stopMotor();
        } else if (getHeight() >= scaleRotations) {
            elevatorTalonMaster.set(ControlMode.Position, scaleRotations);
        } else if (getHeight() >= switchRotations && getHeight() < scaleRotations) {
            elevatorTalonMaster.set(ControlMode.Position, switchRotations);
        }
    }

    public void driveJoystickInput(){
        double y = Robot.oi.xboxController2.getRawAxis(OI.LeftYAxis);
        if (Math.abs(y) > deadZone) {
            drive(y * raiseSpeed);
        }
    }
    public void drive(double speed) {
        System.out.println(speed);
        System.out.println(getHeight());
        // If we're trying to go up, and we haven't passed max height -> it's okay
        if (speed > 0 && getHeight() < maxSafeRotations) {
            elevatorTalonMaster.set(ControlMode.PercentOutput, speed);
            System.out.println("Up");
            // If we're trying to go down, and haven't passed bottom -> it's okay
        } else if(speed < 0 && getHeight() > 0) {
            System.out.println("Down");
            elevatorTalonMaster.set(ControlMode.PercentOutput, speed);
        } else {
            elevatorTalonMaster.stopMotor();
        }
    }

    public void stop() {
        elevatorTalonMaster.stopMotor();
    }

    public double getHeight() {
        return elevatorTalonMaster.getSelectedSensorPosition(0);
    }
}
