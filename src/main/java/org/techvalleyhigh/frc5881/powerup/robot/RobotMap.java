package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class RobotMap {
    // Gyro
    public static ADXRS450_Gyro digitalGyro;

    // Talons
    public static WPI_TalonSRX driveFrontLeft;
    public static WPI_TalonSRX driveFrontRight;
    public static WPI_TalonSRX driveBackLeft;
    public static WPI_TalonSRX driveBackRight;

    public static WPI_TalonSRX elevatorTalonMaster;
    public static WPI_TalonSRX elevatorTalonFollower;

    // Talon for the arm
    public static WPI_TalonSRX armTalon;

    // Pneumatic Solenoids for the and the grabber
    public static DoubleSolenoid leftGrabDoubleSolenoid;
    public static DoubleSolenoid rightGrabDoubleSolenoid;

    // Pneumatic Solenoids for the elevator
    public static DoubleSolenoid leftElevatorPancakeDoubleSolenoid;
    public static DoubleSolenoid rightElevatorPancakeDoubleSolenoid;

    // Pneumatic Compressor for giving everything air
    public static Compressor compressor;

    public static void init() {
        //Talons
        driveFrontLeft = new WPI_TalonSRX(1);
        driveFrontLeft.setName("Drive", "Talon Front Left");
        LiveWindow.add(driveFrontLeft);
        driveFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        driveFrontLeft.setSensorPhase(true);

        driveBackLeft = new WPI_TalonSRX(2);
        driveBackLeft.setName("Drive", "Back Left");
        LiveWindow.add(driveBackLeft);
        driveBackLeft.set(ControlMode.Follower, 1);

        driveFrontRight = new WPI_TalonSRX(3);
        driveFrontRight.setName("Drive", "Front Right");
        LiveWindow.add(driveFrontRight);
        driveFrontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 , 10);

        driveBackRight = new WPI_TalonSRX(4);
        driveBackRight.setName("Drive", "Back Right");
        LiveWindow.add(driveBackRight);
        driveBackRight.set(ControlMode.Follower, 3);

        // Gyro
        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        digitalGyro.setName("Drive", "Gyro");
        LiveWindow.add(digitalGyro);

        // Talons for the Elevator
        elevatorTalonMaster = new WPI_TalonSRX(5);
        elevatorTalonMaster.setName("Elevator", "Master");
        elevatorTalonMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        elevatorTalonMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorTalonMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        LiveWindow.add(elevatorTalonMaster);

        elevatorTalonFollower = new WPI_TalonSRX(6);
        elevatorTalonFollower.setName("Elevator", "Follow");
        elevatorTalonFollower.set(ControlMode.Follower, 5);
        LiveWindow.add(elevatorTalonFollower);

        // Talon for the arm
        armTalon = new WPI_TalonSRX(7);
        armTalon.setName("Arm", "Master");
        armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        armTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        armTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        LiveWindow.add(armTalon);

        // Pneumatic Compressor
        compressor = new Compressor(20);
        compressor.setName("Compressor", "Compressor");
        LiveWindow.add(compressor);

        // Pneumatic Solenoids for the grabber
        rightGrabDoubleSolenoid = new DoubleSolenoid(20,0, 1);
        rightGrabDoubleSolenoid.setName("Grabber", "Right Solenoid");
        LiveWindow.add(rightGrabDoubleSolenoid);

        leftGrabDoubleSolenoid = new DoubleSolenoid(20,2, 3);
        leftGrabDoubleSolenoid.setName("Grabber", "Left Solenoid");
        LiveWindow.add(leftGrabDoubleSolenoid);

        // Pneumatic Solenoid for elevator
        leftElevatorPancakeDoubleSolenoid = new DoubleSolenoid(20, 4, 5);
        leftElevatorPancakeDoubleSolenoid.setName("Elevator", "Left Ratchet");
        LiveWindow.add(leftElevatorPancakeDoubleSolenoid);

        rightElevatorPancakeDoubleSolenoid = new DoubleSolenoid(20, 6, 7);
        rightElevatorPancakeDoubleSolenoid.setName("Elevator", "Right Ratchet");
        LiveWindow.add(rightElevatorPancakeDoubleSolenoid);
    }
}
