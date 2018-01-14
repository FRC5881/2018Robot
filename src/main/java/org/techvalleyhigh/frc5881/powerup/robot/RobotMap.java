package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
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
        //Talons
        driveFrontLeft = new WPI_TalonSRX(0);
        driveFrontLeft.setName("Drive Talon Left", "Talon Front Left");
        LiveWindow.add(driveFrontLeft);

        driveFrontRight = new WPI_TalonSRX(1);
        driveFrontRight.setName("Drive Talon Right", "Talon Front Right");
        LiveWindow.add(driveFrontRight);

        driveBackLeft = new WPI_TalonSRX(2);
        driveBackLeft.setName("Drive Talon Left", "Talon Back Left");
        LiveWindow.add(driveBackLeft);

        driveBackRight = new WPI_TalonSRX(3);
        driveBackRight.setName("Drive Talon Right", "Talon Back Right");
        LiveWindow.add(driveBackRight);


        //Elevator Talons
        elevatorTalonMaster = new WPI_TalonSRX(4);
        elevatorTalonMaster.setName("Elevator", "Master");
        elevatorTalonMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        elevatorTalonMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        LiveWindow.add(elevatorTalonMaster);

        elevatorTalonFollower = new WPI_TalonSRX(5);
        elevatorTalonFollower.setName("Elevator", "Follow");
        //elevatorTalonFollower.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        elevatorTalonFollower.set(ControlMode.Follower, 4);
        LiveWindow.add(elevatorTalonFollower);

        //Arm Talon
        armTalon = new WPI_TalonSRX(6);
        armTalon.setName("Arm", "Master");
        LiveWindow.add(armTalon);


        //Gyro
        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        digitalGyro.setName("Gyro", "Gyro");
        LiveWindow.add(digitalGyro);


        //Pneumatic things
        compressor = new Compressor(0);
        compressor.setName("Compressor", "Compressor");
        LiveWindow.add(compressor);

        doubleSolenoid = new DoubleSolenoid(0, 1);
        doubleSolenoid.setName("DoubleSolenoid", "Double Solenoid");
        LiveWindow.add(doubleSolenoid);
    }
}
