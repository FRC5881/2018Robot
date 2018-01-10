package org.techvalleyhigh.powerup.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class RobotMap {
    // Gyro
    public static ADXRS450_Gyro digitalGyro;

    // Talons
    public static TalonSRX testTalon;

    // TODO: Drive talons
    public static void init() {
        // Define talons with TalonSRX
        // TODO: LiveWindow.add(Sendable)
        testTalon = new TalonSRX(0);

        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);

    }
}
