package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class RobotMap {

    public static Compressor compressor;
    public static DoubleSolenoid doubleSolenoid;

    public static void init() {
        //Compressor for pneumatics
        compressor = new Compressor(0);
        compressor.setName("Compressor", "Compressor1");
        LiveWindow.add(compressor);

        //Double Solenoids for (you guessed it) the solenoid
        doubleSolenoid = new DoubleSolenoid(0, 0);
        doubleSolenoid.setName("Solenoid", "Double Solenoid 1");
        LiveWindow.add(doubleSolenoid);
    }
}
