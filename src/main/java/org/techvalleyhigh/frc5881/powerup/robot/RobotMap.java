package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class RobotMap {
    //Gyro
    public static ADXRS450_Gyro digitalGyro;
    //Talons for driving
    public static WPI_TalonSRX driveFrontLeft;
    public static WPI_TalonSRX driveFrontRight;
    public static WPI_TalonSRX driveBackLeft;
    public static WPI_TalonSRX driveBackRight;
    //Elevator Talons
    public static WPI_TalonSRX elevatorTalonMaster;
    public static WPI_TalonSRX elevatorTalonFollower;
    //Talon for the arm
    public static WPI_TalonSRX armTalon;
    //Pneumatic Solenoids for the buddy bar and the grabber
    public static DoubleSolenoid leftGrabDoubleSolenoid;
    public static DoubleSolenoid rightGrabDoubleSolenoid;
    public static DoubleSolenoid buddyBarDoubleSolenoid;
    //Pnuematic Compressor for giving everything air
    public static Compressor compressor;

    public static void init() {
        //Talons for Driving
        driveFrontLeft = new WPI_TalonSRX(0);
        driveFrontLeft.setName("Drive Talon Left", "Talon Front Left");
        LiveWindow.add(driveFrontLeft);

        driveBackLeft = new WPI_TalonSRX(2);
        driveBackLeft.setName("Drive Talon Left", "Talon Back Left");
        driveBackLeft.set(ControlMode.Follower, 0);
        LiveWindow.add(driveBackLeft);

        driveFrontRight = new WPI_TalonSRX(1);
        driveFrontRight.setName("Drive Talon Right", "Talon Front Right");
        LiveWindow.add(driveFrontRight);

        driveBackRight = new WPI_TalonSRX(3);
        driveBackRight.setName("Drive Talon Right", "Talon Back Right");
        driveBackRight.set(ControlMode.Follower, 1);
        LiveWindow.add(driveBackRight);

        //Talons for the Elevator
        //switch Talon number back to four
        elevatorTalonMaster = new WPI_TalonSRX(4);
        elevatorTalonMaster.setName("elevator", "Master");
        elevatorTalonMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        elevatorTalonMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorTalonMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorTalonMaster.config_kD(0, 0.012, 20);
        elevatorTalonMaster.config_kI(0, 0.001, 20);
        elevatorTalonMaster.config_kP(0, 0.013, 20);
        LiveWindow.add(elevatorTalonMaster);

        elevatorTalonFollower = new WPI_TalonSRX(5);
        elevatorTalonFollower.setName("elevator", "Follow");
        elevatorTalonFollower.set(ControlMode.Follower, 4);
        LiveWindow.add(elevatorTalonFollower);

        //Talon for the arm
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
        compressor = new Compressor(20);
        compressor.setName("Compressor", "Compressor");
        LiveWindow.add(compressor);

        //Pneumatic Solenoids for the grabber
        rightGrabDoubleSolenoid = new DoubleSolenoid(20,0, 1);
        rightGrabDoubleSolenoid.setName("Grabber Solenoid", "Right Solenoid");
        LiveWindow.add(rightGrabDoubleSolenoid);

        leftGrabDoubleSolenoid = new DoubleSolenoid(20,2, 3);
        leftGrabDoubleSolenoid.setName("Grabber Solenoid", "Left Solenoid");
        LiveWindow.add(leftGrabDoubleSolenoid);
        //Pneumatic Solenoid for the buddy bar
        buddyBarDoubleSolenoid = new DoubleSolenoid(20,4, 5);
        buddyBarDoubleSolenoid.setName("Buddy Bar Solenoid", "Buddy Bar Solenoid One");
        LiveWindow.add(buddyBarDoubleSolenoid);
    }
}
