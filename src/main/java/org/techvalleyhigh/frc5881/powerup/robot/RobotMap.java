package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.*;
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
    public static DoubleSolenoid grabSolenoid;

    // Pneumatic Solenoids for the elevator
    public static DoubleSolenoid elevatorSolenoid;

    // Pneumatic Compressor for giving everything air
    public static Compressor compressor;

    public static void init() {
        //Talons
        driveFrontLeft = new WPI_TalonSRX(1);
        driveFrontLeft.setName("Drive", "Front Left Motor");
        driveFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        driveFrontLeft.setSensorPhase(true);
        LiveWindow.add(driveFrontLeft);

        driveBackLeft = new WPI_TalonSRX(2);
        driveBackLeft.setName("Drive", "Back Left Motor");
        driveBackLeft.set(ControlMode.Follower, 1);
        LiveWindow.add(driveBackLeft);

        driveFrontRight = new WPI_TalonSRX(3);
        driveFrontRight.setName("Drive", "Front Right Motor");
        //driveFrontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 , 10);
        LiveWindow.add(driveFrontRight);

        driveBackRight = new WPI_TalonSRX(4);
        driveBackRight.setName("Drive", "Back Right Motor");
        driveBackRight.set(ControlMode.Follower, 3);
        LiveWindow.add(driveBackRight);

        // Gyro
        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        digitalGyro.setName("Drive", "Gyro");
        LiveWindow.add(digitalGyro);

        // Talons for the Elevator
        elevatorTalonMaster = new WPI_TalonSRX(5);
        elevatorTalonMaster.setName("Elevator", "Master Motor");
        elevatorTalonMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        elevatorTalonMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorTalonMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorTalonMaster.configPeakCurrentLimit(32, 10);
        elevatorTalonMaster.configPeakCurrentDuration(4000, 10);
        elevatorTalonMaster.setNeutralMode(NeutralMode.Coast);
        elevatorTalonMaster.setSensorPhase(false);
        elevatorTalonMaster.setInverted(true);
        LiveWindow.add(elevatorTalonMaster);

        elevatorTalonFollower = new WPI_TalonSRX(6);
        elevatorTalonFollower.setName("Elevator", "Follow Motor");
        elevatorTalonFollower.set(ControlMode.Follower, 5);
        LiveWindow.add(elevatorTalonFollower);

        // Talon for the arm
        armTalon = new WPI_TalonSRX(7);
        armTalon.setName("Arm", "Master Motor");
        armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        armTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        armTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        armTalon.configPeakCurrentLimit(32, 10);
        armTalon.configPeakCurrentDuration(3000, 10);
        LiveWindow.add(armTalon);

        // Pneumatic Compressor
        compressor = new Compressor(20);
        compressor.setName("Compressor", "Compressor");
        LiveWindow.add(compressor);

        // Pneumatic Solenoids for the grabber
        grabSolenoid = new DoubleSolenoid(20,1, 0);
        grabSolenoid.setName("Grabber", "Solenoid");
        LiveWindow.add(grabSolenoid);

        // Pneumatic Solenoid for elevator
        elevatorSolenoid = new DoubleSolenoid(20, 4, 5);
        elevatorSolenoid.setName("Elevator", "Ratchet");
        LiveWindow.add(elevatorSolenoid);
    }
}
