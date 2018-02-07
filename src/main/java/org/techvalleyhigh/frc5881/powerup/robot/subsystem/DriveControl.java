package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.commands.Drive;


public class DriveControl extends Subsystem {
    /**
     * String used for SmartDashboard key for Auto Gyro Tolerance
     */
    private static final String AUTO_GYRO_TOLERANCE = "Auto Gyro Tolerance (+- Deg)";

    public static final double distancePerTick = 4 * 18.84954 / 4096;

    private DifferentialDrive robotDrive;

    private static final double deadZone = 0.1;

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

        SpeedControllerGroup m_left = new SpeedControllerGroup(RobotMap.driveFrontLeft, RobotMap.driveBackLeft);
        SpeedControllerGroup m_right = new SpeedControllerGroup(RobotMap.driveFrontRight, RobotMap.driveBackRight);

        robotDrive = new DifferentialDrive(m_left, m_right);
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

        // Add a dead zone to the joysticks
        x = Math.abs(x) > deadZone ? x : 0;
        y = Math.abs(y) > deadZone ? y : 0;

        robotDrive.arcadeDrive(x, y, true);
    }

    /**
     * Command the drive motors to move and turn without correcting for deadzone or scaling.
     * Joystick input should NOT be fed through this function.
     *
     * @param move Motor amount to move from -1 to 1
     * @param turn Motor amount to turn from -1 to 1
     */

    public void rawArcadeDrive(double move, double turn) {
        updateDashboard();

        robotDrive.arcadeDrive(move, turn, true);
    }

    public void rawTenkDrive(double right, double left) {
        updateDashboard();

        robotDrive.tankDrive(left, right);
    }

    /**
     * Stops all drive motors
     */
    public void stopDrive() {
        RobotMap.driveFrontRight.stopMotor();
        RobotMap.driveFrontLeft.stopMotor();
        RobotMap.driveBackRight.stopMotor();
        RobotMap.driveBackLeft.stopMotor();
    }

    public void changeMode(ControlMode mode, double value) {
        RobotMap.driveFrontRight.set(mode, value);
        RobotMap.driveFrontLeft.set(mode, value);
    }

    // ----------------------- PID CONTROL ----------------------- //
    /*
    WORK IN PROGRESS
    // TODO: PID CONTROL
    private static final int kTimeoutMs = 10;
    // TODO: What does error physically mean?
    private static final int kError = 1;

    public void initPid() {
        RobotMap.driveFrontLeft.configAllowableClosedloopError(0, kError, kTimeoutMs);

        RobotMap.driveFrontLeft.config_kF(0, 0.0, kTimeoutMs);
        RobotMap.driveFrontLeft.config_kP(0, 0.1, kTimeoutMs);
        RobotMap.driveFrontLeft.config_kI(0, 0.0, kTimeoutMs);
        RobotMap.driveFrontLeft.config_kD(0, 0.0, kTimeoutMs);

        RobotMap.driveFrontRight.configAllowableClosedloopError(0, kError, kTimeoutMs);

        RobotMap.driveFrontRight.config_kF(0, 0.0, kTimeoutMs);
        RobotMap.driveFrontRight.config_kP(0, 0.1, kTimeoutMs);
        RobotMap.driveFrontRight.config_kI(0, 0.0, kTimeoutMs);
        RobotMap.driveFrontRight.config_kD(0, 0.0, kTimeoutMs);
    }
         */

}
