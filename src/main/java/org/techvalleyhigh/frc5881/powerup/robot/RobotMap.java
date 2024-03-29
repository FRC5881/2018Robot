package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class RobotMap {
    /**
     * Gyro
      */

    public static ADXRS450_Gyro digitalGyro;

    /**
     * Front left drive talon
     */
    public static WPI_TalonSRX driveFrontLeft;

    /**
     * Front right drive talon
     */
    public static WPI_TalonSRX driveFrontRight;

    /**
     * Back left drive talon
     */
    public static WPI_TalonSRX driveBackLeft;

    /**
     * Back right drive talon
     */
    public static WPI_TalonSRX driveBackRight;

    /**
     * Master elevator talon (left on the bot)
     */
    public static WPI_TalonSRX elevatorTalonMaster;

    /**
     * Following elevator talon (right on the bot)
     */
    public static WPI_TalonSRX elevatorTalonFollower;

    /**
     * Arm talon
     */
    public static WPI_TalonSRX armTalon;

    /**
      * Pneumatic Solenoid for the and the manipulator
      */
    public static DoubleSolenoid grabSolenoid;

    /**
     * Pneumatic Solenoid for the and the ratchet
     */
    public static DoubleSolenoid ratchetSolenoid;

    /**
     * Pneumatic Compressor for giving everything air
     */
    public static Compressor compressor;

    /**
     * Initialize all our countless hardware components on the bot
     */
    public static void init() {
        // Drive Talons Back talons follow the ones in front of them
        driveFrontLeft = new WPI_TalonSRX(1);
        driveFrontLeft.setName("Drive", "Front Left Motor");
        driveFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        driveFrontLeft.configAllowableClosedloopError(0, 50, 10);
        driveFrontLeft.setSensorPhase(true);
        LiveWindow.add(driveFrontLeft);

        driveBackLeft = new WPI_TalonSRX(2);
        driveBackLeft.setName("Drive", "Back Left Motor");
        LiveWindow.add(driveBackLeft);

        driveFrontRight = new WPI_TalonSRX(3);
        driveFrontRight.setName("Drive", "Front Right Motor");
        driveFrontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0 , 10);
        driveFrontLeft.configAllowableClosedloopError(0, 50, 10);
        LiveWindow.add(driveFrontRight);

        driveBackRight = new WPI_TalonSRX(4);
        driveBackRight.setName("Drive", "Back Right Motor");
        LiveWindow.add(driveBackRight);

        // Gyro
        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        digitalGyro.setName("Drive", "Gyro");
        LiveWindow.add(digitalGyro);

        // Talons for the Elevator - Master is left on the bot
        elevatorTalonMaster = new WPI_TalonSRX(5);
        elevatorTalonMaster.setName("Elevator", "Master Motor");
        elevatorTalonMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        elevatorTalonMaster.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorTalonMaster.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        elevatorTalonMaster.configPeakCurrentLimit(32, 10);
        elevatorTalonMaster.configPeakCurrentDuration(4000, 10);
        elevatorTalonMaster.setNeutralMode(NeutralMode.Brake);
        elevatorTalonMaster.setSensorPhase(true);
        elevatorTalonMaster.setInverted(true);
        LiveWindow.add(elevatorTalonMaster);

        elevatorTalonFollower = new WPI_TalonSRX(6);
        elevatorTalonFollower.setName("Elevator", "Follow Motor");
        LiveWindow.add(elevatorTalonFollower);

        // Talon for the arm
        armTalon = new WPI_TalonSRX(7);
        armTalon.setName("Arm", "Master Motor");
        armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 20);
        armTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        armTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        armTalon.configPeakCurrentLimit(24, 10);
        armTalon.configPeakCurrentDuration(3000, 10);
        armTalon.setSensorPhase(false);
        armTalon.setInverted(true);
        LiveWindow.add(armTalon);

        // Pneumatic Compressor
        compressor = new Compressor(20);
        compressor.setName("Compressor", "Compressor");
        LiveWindow.add(compressor);

        // Pneumatic Solenoids for the grabber
        grabSolenoid = new DoubleSolenoid(20,5, 4);
        grabSolenoid.setName("Grabber", "Solenoid");
        LiveWindow.add(grabSolenoid);

        // Pneumatic Solenoid for elevator
        ratchetSolenoid = new DoubleSolenoid(20, 0, 1);
        ratchetSolenoid.setName("Elevator", "Ratchet");
        LiveWindow.add(ratchetSolenoid);

        initMotorState();
    }

    /**
     * Set up the motor states in their own function so we could call it repeatably if things are getting interesting
     * (Talons decide to stop following)
     */
    public static void initMotorState() {
        // Set drive talons following
        driveBackLeft.set(ControlMode.Follower, 1);
        driveBackRight.set(ControlMode.Follower, 3);

        // Put the talons in coast mode, helps us stop tipping
        driveFrontLeft.setNeutralMode(NeutralMode.Coast);
        driveFrontRight.setNeutralMode(NeutralMode.Coast);

        // Set elevator talon following
        elevatorTalonFollower.set(ControlMode.Follower, 5);
    }
}
