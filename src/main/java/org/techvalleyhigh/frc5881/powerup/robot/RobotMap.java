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

    // Talon for the arm
    public static WPI_TalonSRX armTalon;

    // Talons for the Elevator
    public static WPI_TalonSRX elevatorTalonMaster;
    public static WPI_TalonSRX elevatorTalonFollower;

    // Pneumatic Solenoids for the and the grabber
    public static DoubleSolenoid grabDoubleSolenoid;

    // Pneumatic Solenoids for the elevator
    public static DoubleSolenoid elevatorPancakeDoubleSolenoid;

    // Pneumatic Compressor for giving everything air
    public static Compressor compressor;

    public static void init() {

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
        LiveWindow.add(armTalon);

        // Pneumatic Compressor
        compressor = new Compressor(20);
        compressor.setName("Compressor", "Compressor");
        LiveWindow.add(compressor);

        // Pneumatic Solenoid for the grabber
        grabDoubleSolenoid = new DoubleSolenoid(20,3, 2);
        grabDoubleSolenoid.setName("Grabber", "Left Solenoid");
        LiveWindow.add(grabDoubleSolenoid);

        // Pneumatic Solenoid for elevator
        elevatorPancakeDoubleSolenoid = new DoubleSolenoid(20, 7, 6);
        elevatorPancakeDoubleSolenoid.setName("Elevator", "Left Ratchet");
        LiveWindow.add(elevatorPancakeDoubleSolenoid);
    }
}
