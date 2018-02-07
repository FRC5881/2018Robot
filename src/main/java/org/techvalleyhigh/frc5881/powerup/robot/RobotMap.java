package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Command;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
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

    public static WPI_TalonSRX armTalon;

    //Pneumatics
    public static DoubleSolenoid doubleSolenoid;
    public static Compressor compressor;

    public static void init() {
        // Define talons with WPI_TalonSRX
        driveFrontLeft = new WPI_TalonSRX(10);
        driveFrontLeft.setName("Drive", "Front Left");
        LiveWindow.add(driveFrontLeft);
        driveFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

        driveBackLeft = new WPI_TalonSRX(11);
        driveBackLeft.setName("Drive", "Back Left");
        LiveWindow.add(driveBackLeft);
        driveBackLeft.set(ControlMode.Follower, 0);

        driveFrontRight = new WPI_TalonSRX(12);
        driveFrontRight.setName("Drive", "Front Right");
        LiveWindow.add(driveFrontRight);
        driveFrontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 , 10);

        driveBackRight = new WPI_TalonSRX(13);
        driveBackRight.setName("Drive", "Back Right");
        LiveWindow.add(driveBackRight);
        driveBackRight.set(ControlMode.Follower, 1);

        //elevator Talons
        elevatorTalonMaster = new WPI_TalonSRX(4);
        elevatorTalonMaster.setName("elevator", "Master");
        elevatorTalonMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        elevatorTalonMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorTalonMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        LiveWindow.add(elevatorTalonMaster);

        elevatorTalonFollower = new WPI_TalonSRX(5);
        elevatorTalonFollower.setName("elevator", "Follow");
        elevatorTalonFollower.set(ControlMode.Follower, 4);
        LiveWindow.add(elevatorTalonFollower);

        //arm Talon
        armTalon = new WPI_TalonSRX(6);
        armTalon.setName("arm", "Master");
        armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        armTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        armTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        LiveWindow.add(armTalon);

        //Gyro
        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        digitalGyro.setName("Gyro", "Gyro");
        LiveWindow.add(digitalGyro);

        //Pneumatic Compressor
        compressor = new Compressor(0);
        compressor.setName("Compressor", "Compressor");
        LiveWindow.add(compressor);

        //Pneumatic Solenoid
        doubleSolenoid = new DoubleSolenoid(0, 1);
        doubleSolenoid.setName("DoubleSolenoid", "Double Solenoid");
        LiveWindow.add(doubleSolenoid);
    }
}
