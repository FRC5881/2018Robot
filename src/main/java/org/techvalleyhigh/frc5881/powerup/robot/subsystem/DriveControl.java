package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.utils.SpeedPID;


public class DriveControl extends Subsystem {
    // SmartDashboard key for Gyro Tolerance
    private static final String GYRO_TOLERANCE = "Gyro Tolerance %";

    // SmartDashboard key for Speed Tolerance
    private static final String SPEED_TOLERANCE = "Speed Tolerance %";

    // SmartDashboard key for Auto Turn Speed
    private static final String AUTO_TURN_SPEED = "Auto Turn Speed %";

    // SmartDashboard key for X_AXIS sensitivity
    private static final String X_AXIS_SENSITIVITY = "X Axis sensitivity";

    // SmartDashboard key for Y_AXIS sensitivity
    private static final String Y_AXIS_SENSITIVITY = "Y Axis sensitivity";

    // Joystick dead zone
    private static final double deadZone = 0.1;

    // Max speeds ticks / 100 ms
    private static final double maxForward = 1500;
    private static final double maxReverse = -1500;

    // Robot drive
    private DifferentialDrive robotDrive;

    // PIDs
    private static PIDController gyroPID;
    private static PIDController speedPID;

    // PID outputs
    public double gyroPIDOutput;
    public double speedPIDOutput;

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

        // Create DifferentialDrive for all our control needs
        SpeedControllerGroup m_left = new SpeedControllerGroup(RobotMap.driveFrontLeft);
        SpeedControllerGroup m_right = new SpeedControllerGroup(RobotMap.driveFrontRight);
        robotDrive = new DifferentialDrive(m_left, m_right);

        // Clear stop motors just in case there was some left over nonsense
        robotDrive.stopMotor();

        // Disable safety ¯\_(ツ)_/¯
        robotDrive.setSafetyEnabled(false);
        setMotorSafety(false);

        // Set "dead zone" on differential drive inputs
        robotDrive.setDeadband(deadZone);

        // Put PID values
        putSmartDashboard();

        // Create PID controls
        gyroPID = new PIDController(getGyro_kP(), getGyro_kI(), getGyro_kD(), getGyro_kF(),
                RobotMap.digitalGyro, output -> gyroPIDOutput = output);

        speedPID = new PIDController(getSpeed_kP(), getSpeed_kI(), getSpeed_kD(), getSpeed_kF(),
                new SpeedPID(), output -> speedPIDOutput = output);

        initPID();
    }

    /**
     * Puts values on Smart Dashboard
     */
    private void putSmartDashboard() {
        // Auto values
        SmartDashboard.putNumber(GYRO_TOLERANCE, 5);
        SmartDashboard.putNumber(AUTO_TURN_SPEED, 0.75);

        // Joystick sensitivities
        SmartDashboard.putNumber(X_AXIS_SENSITIVITY, 1);
        SmartDashboard.putNumber(Y_AXIS_SENSITIVITY, -1);

        // --- Pid controls --- //
        // Left Motors
        SmartDashboard.putNumber("Left kP", 2.0);
        SmartDashboard.putNumber("Left kI", 0.0);
        SmartDashboard.putNumber("Left kD", 20.0);
        SmartDashboard.putNumber("Left kF", 0.076);

        // Right Motors
        SmartDashboard.putNumber("Right kP", 2.0);
        SmartDashboard.putNumber("Right kI", 0.0);
        SmartDashboard.putNumber("Right kD", 20.0);
        SmartDashboard.putNumber("Right kF", 0.076);

        // Gyro
        SmartDashboard.putNumber("Gyro kP", 0.057);
        SmartDashboard.putNumber("Gyro kI", 0.000000001);
        SmartDashboard.putNumber("Gyro kD", 0.14);
        SmartDashboard.putNumber("Gyro kF", 0.0);

        // Speed
        SmartDashboard.putNumber("Speed kP", 2.0);
        SmartDashboard.putNumber("Speed kI", 0.0);
        SmartDashboard.putNumber("Speed kD", 20.0);
        SmartDashboard.putNumber("Speed kF", 0.076);
    }

    /**
     * Init PID control values to be ready for driving
     */
    public void initPID() {
        zeroEncoders();

        // Set motor PID values
        RobotMap.driveFrontLeft.config_kP(0 , getLeft_kP(), 10);
        RobotMap.driveFrontLeft.config_kI(0, getLeft_kI(), 10);
        RobotMap.driveFrontLeft.config_kD(0, getLeft_kD(), 10);
        RobotMap.driveFrontLeft.config_kF(0, getLeft_kF(), 10);

        RobotMap.driveFrontRight.config_kP(0 , getRight_kP(), 10);
        RobotMap.driveFrontRight.config_kI(0, getRight_kI(), 10);
        RobotMap.driveFrontRight.config_kD(0, getRight_kD(), 10);
        RobotMap.driveFrontRight.config_kF(0, getRight_kF(), 10);

        // Set other PID values
        gyroPID.setP(getGyro_kP());
        gyroPID.setI(getGyro_kI());
        gyroPID.setD(getGyro_kD());
        gyroPID.setF(getGyro_kF());
        gyroPID.setPercentTolerance(getGyroTolerance());
        gyroPID.reset();

        speedPID.setP(getSpeed_kP());
        speedPID.setI(getSpeed_kI());
        speedPID.setD(getSpeed_kD());
        speedPID.setF(getSpeed_kF());
        speedPID.setPercentTolerance(getSpeedTolerance());
        speedPID.reset();

        // Just keep the PIDs running
        gyroPID.enable();
        speedPID.enable();
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

    @Override
    public void initDefaultCommand() {

    }

    // ----------------------- GYRO ----------------------- //

    /**
     * Calibrate the Gyro, it takes ~15 seconds
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
    public int getGyroTolerance() {
        return (int) SmartDashboard.getNumber(GYRO_TOLERANCE, 5);
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
    public void setGyroPid(double setPoint) {
        gyroPID.setSetpoint(setPoint);
    }

    // Getters for PID
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


    // ----------------------- DRIVE HANDLING ----------------------- //
    // ---- Getters for SmartDashboard drive values ---- //
    public double getAutoTurnSpeed() {
        return SmartDashboard.getNumber(AUTO_TURN_SPEED, 0.75);
    }

    public double getXAxisSensitivity() {
        return SmartDashboard.getNumber(X_AXIS_SENSITIVITY, -1);
    }

    public double getYAxisSensitivity() {
        return SmartDashboard.getNumber(Y_AXIS_SENSITIVITY, 1);
    }

    // --- Scale methods for joystick inputs --- //
    // Scale joysticks by a constant
    private double scaleXAxis(double x) {
        return x * getXAxisSensitivity();
    }
    private double scaleYAxis(double y) {
        return y * getYAxisSensitivity();
    }

    // --- Joystick drive methods --- //
    public void arcadeJoystickInputs() {
        double turn = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS);
        double speed = Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS);

        robotDrive.arcadeDrive(scaleXAxis(turn), scaleYAxis(speed), true);
    }

    public void curvatureJoystickInputs(boolean isQuickTurn) {
        double turn = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS);
        double speed = Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS);

        robotDrive.curvatureDrive(scaleXAxis(turn), scaleYAxis(speed), !isQuickTurn);
    }

    public void tankJoystickInputs() {
        double right = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_Y_AXIS);
        double left = Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS);

        robotDrive.tankDrive(right, left, true);
    }

    // --- Raw drive methods to be used during autonomous --- //
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


    // ---- Getters ---- //
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

    // ----------------------- Speed Control ----------------------- //

    /**
     * Implements gyro and speed PIDs into arcade drive for added control w/ joysticks
     */
    public void arcadedPID() {
        // Get scaled inputs
        double turn = scaleXAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS));
        double speed = scaleYAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS));

        // If speed or gyro PID is on target reset it to clear integral error
        if (getSpeedOnTarget()) speedPID.reset();
        if (getGyroOnTarget()) gyroPID.reset();

        setGyroPid(turn);
        setSpeedPID(speed);

        rawArcadeDrive(speedPIDOutput, gyroPIDOutput);
    }

    /**
     * returns the robots current average velocity
     * @return average of the right speed and left speed in ticks per 100 ms
     */
    public double getVelocity() {
        double v1 = RobotMap.driveFrontRight.getSelectedSensorVelocity(0);
        //double v2 = RobotMap.driveFrontLeft.getSelectedSensorVelocity(0);
        //return v1 + v2 / 2
        return v1;
    }

    public void setSpeedPID(double setpoint) {
        speedPID.setSetpoint(setpoint);
    }

    // Getters
    public double getSpeedSetpoint() {
        return speedPID.getSetpoint();
    }

    public double getSpeedError() {
        return speedPID.getError();
    }

    public boolean getSpeedOnTarget() {
        return speedPID.onTarget();
    }

    public double getSpeedTolerance() {
        return SmartDashboard.getNumber(SPEED_TOLERANCE, 5);
    }

    public double getSpeed_kP() {
        return SmartDashboard.getNumber("Speed kP", 2.0);
    }

    public double getSpeed_kI() {
        return SmartDashboard.getNumber("Speed kI", 0.0);
    }

    public double getSpeed_kD() {
        return SmartDashboard.getNumber("Speed kD", 20.0);
    }

    public double getSpeed_kF() {
        return SmartDashboard.getNumber("Speed kF", 0.076);
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
}
