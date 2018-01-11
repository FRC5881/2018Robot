package org.techvalleyhigh.frc5881.powerup.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;

public class RobotMap {
    // Gyro
    public static ADXRS450_Gyro digitalGyro;

    // Talons
    public static TalonSRX testTalon;

    /**
     *
     */
    public static void init() {
        testTalon = new TalonSRX(0);

        digitalGyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
    }
}