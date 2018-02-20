package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.commands.drive.ArcadeDrive;


public class DriveControl extends Subsystem {
    //SmartDashboard key for Auto Gyro Tolerance
    private static final String AUTO_GYRO_TOLERANCE = "Auto Gyro Tolerance (+- Deg)";

    // SmartDashboard key for Auto Turn Speed
    private static final String AUTO_TURN_SPEED = "Auto Turn Speed %";

    // SmartDashboard key for X_AXIS sensitivity
    private static final String X_AXIS_SENSITIVITY = "X Axis sensitivity on drive controls";

    // SmartDashboard key for Y_AXIS sensitivity
    private static final String Y_AXIS_SENSITIVITY = "Y Axis sensitivity on drive controls";

    // SmartDashboard key for ticks per foot scalar
    private static final String SCALE_TICKS_PER_FOOT = "Ticks per foot scalar";

    // Joystick dead zone
    private static final double deadZone = 0.1;

    // Robot drive
    public DifferentialDrive robotDrive;

    // Gyro PID
    private static PIDController gyroPID;
    public double gyroPIDOutput;

    /**
     * Used for converting feet to ticks.
     * 1 rotation = 2( pi )( wheel radius )
     * 1 rotation = 2( pi )( 1/4 feet )
     * 1 rotation = pi / 2 feet
     * 1 tick = pi / (2 * 1440) feet
     * k ticks = k * pi / (2 * 1440) feet // k is number of ticks
     * d = pi * k / (2 * 1440) // d feet to travel
     * 2 * 1440 * d / pi feet = k ticks
     * 2 * 1440 / pi = ticks per foot
     */
    public static final double ticksPerFoot = 2 * 1440 / Math.PI;


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
        // Calibrate gyro on robot start up
        calibrateGyro();

        // Create Differential ArcadeDrive Object
        SpeedControllerGroup m_left = new SpeedControllerGroup(RobotMap.driveFrontLeft);
        SpeedControllerGroup m_right = new SpeedControllerGroup(RobotMap.driveFrontRight);

        robotDrive = new DifferentialDrive(m_left, m_right);
        robotDrive.stopMotor();

        // Disable safety ¯\_(ツ)_/¯
        robotDrive.setSafetyEnabled(false);
        setMotorSafety(false);

        // Set "dead zone" on the joystick stick inputs
        robotDrive.setDeadband(deadZone);

        /* --- Put SmartDashboard values ---*/
        // Auto values
        SmartDashboard.putNumber(AUTO_GYRO_TOLERANCE, 5);
        SmartDashboard.putNumber(AUTO_TURN_SPEED, 0.25);
        SmartDashboard.putNumber(SCALE_TICKS_PER_FOOT, 1);

        // Joystick sensitivities
        SmartDashboard.putNumber(X_AXIS_SENSITIVITY, 1);
        SmartDashboard.putNumber(Y_AXIS_SENSITIVITY, -1);

        // Pid controls
        SmartDashboard.putNumber("Left kP", 2.0);
        SmartDashboard.putNumber("Left kI", 0.0);
        SmartDashboard.putNumber("Left kD", 20.0);
        SmartDashboard.putNumber("Left kF", 0.076);

        SmartDashboard.putNumber("Right kP", 2.0);
        SmartDashboard.putNumber("Right kI", 0.0);
        SmartDashboard.putNumber("Right kD", 20.0);
        SmartDashboard.putNumber("Right kF", 0.076);

        SmartDashboard.putNumber("Gyro kP", 0.057);
        SmartDashboard.putNumber("Gyro kI", 0.000000001);
        SmartDashboard.putNumber("Gyro kD", 0.14);
        SmartDashboard.putNumber("Gyro kF", 0.0);

        SmartDashboard.putNumber("Allowed Error", 5);

        SmartDashboard.putNumber("Acceleration", 1);
        SmartDashboard.putNumber("Velocity", 1);

        gyroPID = new PIDController(getGyro_kP(), getGyro_kI(), getGyro_kD(), getGyro_kF(),
                RobotMap.digitalGyro, output -> gyroPIDOutput = output);
        initPID();
    }


    @Override
    public void initDefaultCommand() {

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

    /**
     * edit the setpoint on the gyro PID
     * @param setPoint absolute bearing in degrees.
     */
    public void writeGyroPid(double setPoint) {
        gyroPID.setSetpoint(setPoint);
    }

    /**
     * Get gyro pid setpoint
     * @return a double the current gyro setpoint
     */
    public double getGyroSetpoint() {
        return gyroPID.getSetpoint();
    }

    /**
     * Get gyro pid error
     * @return a double the current gyro error
     */
    public double getGyroError() {
        return gyroPID.getError();
    }

    /**
     * Get gyro pid on target
     * @return true if pid is on target
     */
    public boolean getGyroOnTarget() {
        return gyroPID.onTarget();
    }


    // ----------------------- DRIVE HANDLING ----------------------- //
    /* --- Getters for SmartDashboard drive values --- */
    public static double getAutoTurnSpeed() {
        return SmartDashboard.getNumber(AUTO_TURN_SPEED, 0.25);
    }

    public static double getXAxisSensitivity() {
        return SmartDashboard.getNumber(X_AXIS_SENSITIVITY, -1);
    }

    public static double getYAxisSensitivity() {
        return SmartDashboard.getNumber(Y_AXIS_SENSITIVITY, 1);
    }

    public static double getScaleTicksPerFoot() {
        return SmartDashboard.getNumber(SCALE_TICKS_PER_FOOT, -1);
    }

    /* --- Scale methods for joystick inputs --- */
    // Scale joysticks by a constant
    private double scaleXAxis(double x) {
        return x * getXAxisSensitivity();
    }
    private double scaleYAxis(double y) {
        return y * getYAxisSensitivity();
    }

    /* --- Joystick drive methods --- */
    public void arcadeJoystickInputs() {
        double x = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS);
        double y = Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS);

        robotDrive.arcadeDrive(scaleXAxis(x), scaleYAxis(y), true);
    }

    public void curvatureJoystickInputs(boolean isQuickTurn) {
        double x = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS);
        double y = Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS);

        rawCurvatureDrive(scaleXAxis(x), scaleYAxis(y), !isQuickTurn);
    }

    public void tankJoystickInputs() {
        double right = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_Y_AXIS);
        double left = Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS);

        robotDrive.tankDrive(right, left, true);
    }

    /* --- Raw drive methods to be used during autonomous --- */
    public void rawArcadeDrive(double move, double turn) {
        updateDashboard();

        robotDrive.arcadeDrive(turn, move, true);
    }

    public void rawCurvatureDrive(double speed, double rotation, boolean isQuickTurn) {
        updateDashboard();

        robotDrive.curvatureDrive(speed, rotation, isQuickTurn);
    }

    public void rawTankDrive(double right, double left) {
        updateDashboard();

        robotDrive.tankDrive(left, right, true);
    }

    /**
     * Stops all drive motors
     */
    public void stopDrive() {
        RobotMap.driveFrontRight.stopMotor();
        RobotMap.driveFrontLeft.stopMotor();
    }

    public void zeroEncoders() {
        RobotMap.driveFrontRight.setSelectedSensorPosition(0, 0, 10);
        RobotMap.driveFrontLeft.setSelectedSensorPosition(0, 0,10);
    }

    /**
     * Init PID control values to be ready for driving
     */
    public void initPID() {
        zeroEncoders();

        // Update all PID values
        RobotMap.driveFrontLeft.config_kP(0 , getLeft_kP(), 10);
        RobotMap.driveFrontLeft.config_kI(0, getLeft_kI(), 10);
        RobotMap.driveFrontLeft.config_kD(0, getLeft_kD(), 10);
        RobotMap.driveFrontLeft.config_kF(0, getLeft_kF(), 10);

        RobotMap.driveFrontRight.config_kP(0 , getRight_kP(), 10);
        RobotMap.driveFrontRight.config_kI(0, getRight_kI(), 10);
        RobotMap.driveFrontRight.config_kD(0, getRight_kD(), 10);
        RobotMap.driveFrontRight.config_kF(0, getRight_kF(), 10);

        gyroPID.setP(getGyro_kP());
        gyroPID.setI(getGyro_kI());
        gyroPID.setD(getGyro_kD());
        gyroPID.setF(getGyro_kF());
        gyroPID.setPercentTolerance(getAutoGyroTolerance());
        gyroPID.reset();

        // Just keep the gyro pid running
        gyroPID.enable();

        // RobotMap.driveFrontLeft.configAllowableClosedloopError(0, 100, 10);
    }

    /**
     * Set motor safety on each drive motor
     * @param enable boolean to set safety too
     */
    public void setMotorSafety(boolean enable) {
        RobotMap.driveFrontLeft.setSafetyEnabled(enable);
        RobotMap.driveFrontRight.setSafetyEnabled(enable);
        RobotMap.driveBackRight.setSafetyEnabled(enable);
        RobotMap.driveBackLeft.setSafetyEnabled(enable);
    }

    public double getLeft_kP() {
        return SmartDashboard.getNumber("Left kP", 0.013);
    }

    public double getLeft_kI() {
        return SmartDashboard.getNumber("Left kI", 0.001);
    }

    public double getLeft_kD() {
        return SmartDashboard.getNumber("Left kD", 0.012);
    }

    public double getLeft_kF() {
        return SmartDashboard.getNumber("Left kF", 0d);
    }

    public double getRight_kP() {
        return SmartDashboard.getNumber("Right kP", 0.013);
    }

    public double getRight_kI() {
        return SmartDashboard.getNumber("Right kI", 0.001);
    }

    public double getRight_kD() {
        return SmartDashboard.getNumber("Right kD", 0.012);
    }

    public double getRight_kF() {
        return SmartDashboard.getNumber("Right kF", 0d);
    }

    public double getGyro_kP() {
        return SmartDashboard.getNumber("Gyro kP", 0.14);
    }

    public double getGyro_kI() {
        return SmartDashboard.getNumber("Gyro kI", 0.02);
    }

    public double getGyro_kD() {
        return SmartDashboard.getNumber("Gyro kD", 0.045);
    }

    public double getGyro_kF() {
        return SmartDashboard.getNumber("Gyro kF", 0.0);
    }

    public double getAllowed_Error() {
        return SmartDashboard.getNumber("Allowed Error", 5);
    }

    public double getAcceleration() {
        return SmartDashboard.getNumber("Acceleration", 1);
    }

    public double getVelocity() {
        return SmartDashboard.getNumber("Velocity", 1);
    }
}
