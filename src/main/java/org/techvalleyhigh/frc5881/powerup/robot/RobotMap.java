package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import sun.nio.cs.ext.DoubleByte;

public class RobotMap {
    // Gyro
    public static ADXRS450_Gyro digitalGyro;

    // Talons
    //TODO: add talons for arm and manipulator subsystems and maybe even for elevator if need be
    public static WPI_TalonSRX driveFrontLeft;
    public static WPI_TalonSRX driveFrontRight;
    public static WPI_TalonSRX driveBackLeft;
    public static WPI_TalonSRX driveBackRight;
    public static WPI_TalonSRX elevatorTalon1;
    public static WPI_TalonSRX elevatorTalon2;

    //Possibly Ultrasonics
    public static AnalogInput ultrasonic;
    // TODO: Drive talons
    public static void init() {
        // TODO: LiveWindow.add(Sendable)
        //Talons
        driveFrontLeft = new WPI_TalonSRX(0);
        driveFrontLeft.setName("Talon", "Talon Front Left");
        LiveWindow.add(driveFrontLeft);
        driveFrontRight = new WPI_TalonSRX(1);
        driveFrontRight.setName("Talon", "Talon Front Right");
        LiveWindow.add(driveFrontRight);
        driveBackLeft = new WPI_TalonSRX(2);
        driveBackLeft.setName("Talon", "Talon Back Left");
        LiveWindow.add(driveBackLeft);
        driveBackRight = new WPI_TalonSRX(3);
        driveBackRight.setName("Talon", "talon Back Right");
        LiveWindow.add(driveBackRight);
        elevatorTalon1 = new WPI_TalonSRX(4);
        elevatorTalon1.setName("Talon", "Elevator Talon 1");
        LiveWindow.add(elevatorTalon1);
        elevatorTalon2 = new WPI_TalonSRX(5);
        elevatorTalon2.setName("Talon", "Elevator Talon 2");
        LiveWindow.add(elevatorTalon2);
        //Gyro
        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        digitalGyro.setName("Gyro", "Gyro");
        LiveWindow.add(digitalGyro);
        //Ultrasonic
        ultrasonic = new AnalogInput(1);
        ultrasonic.setName("Ultrasonic", "Ultrasonic");
        LiveWindow.add(ultrasonic);
    }
}
