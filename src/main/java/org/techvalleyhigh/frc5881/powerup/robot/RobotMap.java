package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class RobotMap {
    /**
     * Gyro
     */
    public static ADXRS450_Gyro digitalGyro;
    /**
     * Front Left Drive Talon(is master for left)
     */
    public static WPI_TalonSRX driveFrontLeft;
    /**
     * Front Right Drive Talon (is master for right)
     */
    public static WPI_TalonSRX driveFrontRight;
    /**
     * Rear Left Drive Talon(is slave)
     */
    public static WPI_TalonSRX driveBackLeft;
    /**
     * Rear Right Drive Talon(is slave)
     */
    public static WPI_TalonSRX driveBackRight;
    /**
     * Master Elevator Talon
     */
    public static WPI_TalonSRX elevatorTalonMaster;
    /**
     * Elevator Talon Follower
     */
    public static WPI_TalonSRX elevatorTalonFollower;
    /**
     * Arm Talon
     */
    public static WPI_TalonSRX armTalon;
    /**
     * Left Grabber Double Solenoid
     */
    public static DoubleSolenoid leftGrabDoubleSolenoid;
    /**
     * Right Grabber Double Solenoid
     */
    public static DoubleSolenoid rightGrabDoubleSolenoid;
    /**
     * Left Pancake Double Solenoid for Elevator
     */
    public static DoubleSolenoid leftElevatorPancakeDoubleSolenoid;
    /**
     * Right Pancake Double Solenoid for Elevator
     */
    public static DoubleSolenoid rightElevatorPancakeDoubleSolenoid;
    /**
     * Pneumatic Compressor
     */
    public static Compressor compressor;

    public static void init() {
        // Talons for Driving
        driveFrontLeft = new WPI_TalonSRX(103);
        driveFrontLeft.setName("Drive", "Talon Front Left");
        LiveWindow.add(driveFrontLeft);
        driveFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

        driveBackLeft = new WPI_TalonSRX(102);
        driveBackLeft.setName("Drive", "Talon Back Left");
        LiveWindow.add(driveBackLeft);
        driveBackLeft.set(ControlMode.Follower, 10);

        driveFrontRight = new WPI_TalonSRX(101);
        driveFrontRight.setName("Drive", "Talon Front Right");
        LiveWindow.add(driveFrontRight);
        driveFrontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 , 10);

        driveBackRight = new WPI_TalonSRX(100);
        driveBackRight.setName("Drive", "Talon Back Right");
        LiveWindow.add(driveBackRight);
        driveBackRight.set(ControlMode.Follower, 12);

        //Elevator Talons
        elevatorTalonMaster = new WPI_TalonSRX(4);
        elevatorTalonMaster.setName("Elevator", "Master");
        elevatorTalonMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        elevatorTalonMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorTalonMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        LiveWindow.add(elevatorTalonMaster);

        elevatorTalonFollower = new WPI_TalonSRX(5);
        elevatorTalonFollower.setName("Elevator", "Follow");
        elevatorTalonFollower.set(ControlMode.Follower, 4);
        LiveWindow.add(elevatorTalonFollower);

        // Arm talon
        armTalon = new WPI_TalonSRX(6);
        armTalon.setName("Arm", "Master");
        armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        LiveWindow.add(armTalon);

        //Gyro
        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        digitalGyro.setName("Gyro", "Gyro");
        LiveWindow.add(digitalGyro);

        //Pneumatic Compressor
        compressor = new Compressor(20);
        compressor.setName("Compressor", "Compressor");
        LiveWindow.add(compressor);

        //Pneumatic Solenoids for the Grabber
        rightGrabDoubleSolenoid = new DoubleSolenoid(20,0, 1);
        rightGrabDoubleSolenoid.setName("Manipulator", "Right Solenoid");
        LiveWindow.add(rightGrabDoubleSolenoid);

        leftGrabDoubleSolenoid = new DoubleSolenoid(20,2, 3);
        leftGrabDoubleSolenoid.setName("Manipulator", "Left Solenoid");
        LiveWindow.add(leftGrabDoubleSolenoid);

        //Pneumatic Solenoids for the Elevator Pancakes
        leftElevatorPancakeDoubleSolenoid = new DoubleSolenoid(20, 4, 5);
        leftElevatorPancakeDoubleSolenoid.setName("Elevator", "Left Pancake Double Solenoid");
        LiveWindow.add(leftElevatorPancakeDoubleSolenoid);

        rightElevatorPancakeDoubleSolenoid = new DoubleSolenoid(20, 6, 7);
        rightElevatorPancakeDoubleSolenoid.setName("Elevator", "Right Pancake Double Solenoid");
        LiveWindow.add(rightElevatorPancakeDoubleSolenoid);

    }
}
