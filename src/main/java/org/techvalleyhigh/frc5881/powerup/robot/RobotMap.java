package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class RobotMap {
    // Gyro
    public static ADXRS450_Gyro digitalGyro;

    // Drive Talons
    public static WPI_TalonSRX driveFrontLeft;
    public static WPI_TalonSRX driveFrontRight;
    public static WPI_TalonSRX driveBackLeft;
    public static WPI_TalonSRX driveBackRight;

    public static void init() {
        // Define talons with WPI_TalonSRX
        driveFrontLeft = new WPI_TalonSRX(0);
        driveFrontLeft.setName("Drive", "Front Left");
        LiveWindow.add(driveFrontLeft);

        driveFrontRight = new WPI_TalonSRX(1);
        driveFrontRight.setName("Drive", "Front Right");
        LiveWindow.add(driveFrontRight);

        driveBackLeft = new WPI_TalonSRX(2);
        driveBackLeft.setName("Drive", "Back Left");
        LiveWindow.add(driveBackLeft);

        driveBackRight = new WPI_TalonSRX(3);
        driveBackRight.setName("Drive", "Back Right");
        LiveWindow.add(driveBackRight);

        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
    }
}
