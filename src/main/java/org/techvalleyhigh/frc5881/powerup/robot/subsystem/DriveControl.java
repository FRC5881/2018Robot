package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.commands.Drive;


// TODO: PID CONTROL
public class DriveControl extends Subsystem {
    /**
     * String used for SmartDashboard key for
     */
    private static final String AUTO_GYRO_TOLERANCE = "Auto Gyro Tolerance (+- Deg)";


    // ----------------------- Subsystem Control ----------------------- //

    /**
     * Create the subsystem with a default name
     */
    public DriveControl() {
        super();
        init();
    }

    /**
     * Create the subsystem with the given name.
     */
    public DriveControl(String name) {
        super(name);
        init();
    }

    /**
     * Initialize SmartDashboard and other local variables
     */
    public void init() {
        calibrateGyro();

        // TODO: INIT ROBOT DRIVE (wpilib killed it)
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new Drive());
    }

    // ----------------------- GYRO ----------------------- //

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

    // ----------------------- DRIVE HANDLING ----------------------- //

    public void driveJoystickInputs() {
        double y = Robot.oi.xboxController.getRawAxis(OI.LeftYAxis);
        double x = Robot.oi.xboxController.getRawAxis(OI.RightXAxis);

        // TODO: Robot drive
        // robotDrive.arcadeDrive(y, x, true);
    }

    /**
     * Command the drive motors to move and turn without correcting for deadzone or scaling.
     * Joystick input should NOT be fed through this function.
     *
     * @param move Motor amount to move from -1 to 1
     * @param turn Motor amount to turn from -1 to 1
     */

    public void rawDrive(double move, double turn) {
        updateDashboard();

        // TODO: Robot drive
        // robotDrive.arcadeDrive(move, turn, true);
    }

    /**
     * Stops all drive motors
     */
    public void stopDrive() {
        // TODO: Robot drive
        // TODO: define drive talons and stop them
        /*
        talonBackLeft.set(0);
        talonBackRight.set(0);
        talonFrontLeft.set(0);
        RobotMap.talonFrontRight.set(0);
         */
    }
}
