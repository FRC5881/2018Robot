package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;

import static org.techvalleyhigh.frc5881.powerup.robot.RobotMap.elevatorTalonMaster;

public class Elevator extends Subsystem {
    /**
     * Max height of linear rail
     */
    // TODO: Find a real max height
    //TODO: Find how many rotations it takes to get to switch
    //TODO: Find how many rotations it takes to get elevator to scale
    private static final double minSafeRotations = 0;
    private static final double maxSafeRotations = 10.0;
    //8192 if needed to be known in the future
    private static final double switchRotations = 2;
    //36864 ticks if needed to be known in the future
    private static final double scaleRotations = 9;
    // Percent to run motors
    public static final double lowerSpeed = -0.5;
    public static final double raiseSpeed = 0.5;
    public static double currentSpeed = elevatorTalonMaster.getActiveTrajectoryVelocity();

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
        /**
         * Supposed to loop until it reaches the next level.
         * But we will find out when we test it.
         */
        if(currentSpeed >= 0 && getHeight() >= maxSafeRotations) {
            elevatorTalonMaster.stopMotor();
        } else if( getHeight() < switchRotations ) {
            elevatorTalonMaster.set(raiseSpeed);
        } else if(getHeight() < scaleRotations && getHeight() > switchRotations){
            elevatorTalonMaster.set(raiseSpeed);
        }
    }
//Meant to descend elevator to next lower level
    public void nextBelowDescend() {
        if (currentSpeed <= 0 && getHeight() <= minSafeRotations) {
            elevatorTalonMaster.stopMotor();
        }
        while (currentSpeed <= 0 && getHeight() > minSafeRotations) {
            if (getHeight() >= scaleRotations) {
                elevatorTalonMaster.set(lowerSpeed);
            } else if (getHeight() >= switchRotations && getHeight() < scaleRotations) {
            }
        }
    }
    public void drive(double speed) {
        // If we're trying to go up, and we haven't passed max height -> it's okay
        if (speed > 0 && getHeight() < maxSafeRotations) {
            elevatorTalonMaster.set(speed);
        // If we're trying to go down, and haven't passed bottom -> it's okay
        } else if(speed < 0 && getHeight() < 0) {
            elevatorTalonMaster.set(speed);
        }
    }

    public void stop() {
        elevatorTalonMaster.stopMotor();
    }

    public double getHeight() {
        return elevatorTalonMaster.getSelectedSensorPosition(0);
    }
}
