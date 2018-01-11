package org.techvalleyhigh.frc5881.powerup.robot.Robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.Robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.Robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.Robot.commands.Drive;

import static org.techvalleyhigh.frc5881.powerup.robot.Robot.RobotMap.init;

public class DriveControl extends Subsystem {
    /**
     * String used for SmartDashboard key for
     */
    private static final String AUTO_GYRO_TOLERANCE = "Auto Gyro Tolerance (+- Deg)";

    public DriveControl() {
        super();
        init();
    }

    public DriveControl(String name){
        super(name);
        init();
    }

    /**
     * Initialize SmartDashboard and other local variables
     */
    public void init() {
        calibrateGyro();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new Drive());
    }

    /**
     * Calibrate the Gyro on init
     */
    public void calibrateGyro() {
        RobotMap.digitalGyro.calibrate();
    }

    /**
     * Gets the current angle as reported by the 1-axis Gyro.
     *
     * @return Current Gyro angle. No drift correction is applied.
     */
    public double getGyroAngle() {
        return RobotMap.digitalGyro.getAngle();
    }

    /**
     * Gets the currently set Gyro Tolerance for Autonomous.
     *
     * @return Number of degrees of tolerance +-
     */
    public int getAutoGyroTolerance() {
        return (int) SmartDashboard.getNumber(AUTO_GYRO_TOLERANCE, 5);
    }

    /**
     * Update the SmartDashboard with current values.
     */
    private void updateDashboard() {
        SmartDashboard.putNumber("Gyro Heading", getGyroAngle());
    }
    //DRIVE HANDLING

    public void driveJoystickInputs() {
        double y = Robot.oi.xboxController.getRawAxis(OI.LeftYAxis);
        double x = Robot.oi.xboxController.getRawAxis(OI.RightXAxis);

        robotDrive.arcadeDrive(y, x, true);
    }
    public void rawDrive(double move, double turn) {
        updateDashboard();

        robotDrive.arcadeDrive(move, turn, true);
    }

    public void stopDrive(){

    }
}