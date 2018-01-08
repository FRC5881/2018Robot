package org.techvalleyhigh.frc5881.powerup.robot.Robot;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
    public static OI oi;

    public void robotInit() {
        RobotMap.init();

        oi = new OI();
    }
    //This function is called when the disabled button is hit
    public void disabledInit() {
    }
}
