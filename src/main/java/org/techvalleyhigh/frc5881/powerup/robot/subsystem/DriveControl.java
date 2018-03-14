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

/**
 * Subsystem to control everything that has to do with drive (except motion profiling)
 */
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
    private static final double deadZone = 0.2;

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

    // Current values for ramping
    public double currentSpeed;
    public double currentTurn;

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
     * Starts a command on init of subsystem, defining commands in robot and OI is preferred
     */
    @Override
    public void initDefaultCommand() {

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
        SmartDashboard.putNumber(X_AXIS_SENSITIVITY, 0.80);
        SmartDashboard.putNumber(Y_AXIS_SENSITIVITY, -1);

        // --- Pid controls --- //
        // Left Motors
        SmartDashboard.putNumber("Left kP", 0.5);
        SmartDashboard.putNumber("Left kI", 0.0);
        SmartDashboard.putNumber("Left kD", 5.0);
        SmartDashboard.putNumber("Left kF", 0.076);

        // Right Motors
        SmartDashboard.putNumber("Right kP", 0.5);
        SmartDashboard.putNumber("Right kI", 0.0);
        SmartDashboard.putNumber("Right kD", 5);
        SmartDashboard.putNumber("Right kF", 0.076);

        // Gyro
        SmartDashboard.putNumber("Gyro kP", 0.057);
        SmartDashboard.putNumber("Gyro kI", 0.000000001);
        SmartDashboard.putNumber("Gyro kD", 0.14);
        SmartDashboard.putNumber("Gyro kF", 0.0);

        // Speed control
        SmartDashboard.putNumber("Speed kP", 0.5);
        SmartDashboard.putNumber("Speed kI", 0.0);
        SmartDashboard.putNumber("Speed kD", 5);
        SmartDashboard.putNumber("Speed kF", 0.076);
        SmartDashboard.putNumber("Speed Ramp", 0.05);
        SmartDashboard.putNumber("Turn Rate", 1);
        SmartDashboard.putNumber("Turn Ramp", 0.35);
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

    // ---- Getters for PID ---- //
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
    /**
     * Scale X axis inputs by sensitivity
     * @param x double value to scale
     * @return scaled double value
     */
    private double scaleXAxis(double x) {
        return OI.applyDeadzone(x, deadZone) * getXAxisSensitivity();
    }

    /**
     * Scale Y axis inputs by sensitivity
     * @param y double value to scale
     * @return scaled double value
     */
    private double scaleYAxis(double y) {
        return OI.applyDeadzone(y, deadZone) * getYAxisSensitivity();
    }

    // --- Joystick drive methods --- //

    /**
     * Implements arcade drive with joystick inputs and co-pilot turn controls
     */
    public void arcadeJoystickInputs() {
        double turn;

        // Checks if driver controller is not being operated and if so give slight control to copilot z rotation
        if (Math.abs(Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS)) < deadZone
                && Math.abs(Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS)) < deadZone) {
            turn = Robot.oi.coPilotController.getRawAxis(OI.PILOT_Z_ROTATION) / 2;

        } else {
            turn = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS);
        }
        double speed = Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS);

        robotDrive.arcadeDrive(scaleXAxis(turn), scaleYAxis(speed), true);
    }

    /**
     * Implements Curvature drive with joystick inputs
     */
    public void curvatureJoystickInputs(boolean isQuickTurn) {
        double turn = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS);
        double speed = Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS);

        robotDrive.curvatureDrive(scaleXAxis(turn), scaleYAxis(speed), !isQuickTurn);
    }

    /**
     * Implements tank drive with joystick inputs
     */
    public void tankJoystickInputs() {
        double right = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_Y_AXIS);
        double left = Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS);

        robotDrive.tankDrive(right, left, true);
    }

    // --- Raw drive methods to be used during autonomous --- //

    /**
     * Pass raw values to arcade drive, shouldn't be used with joystick inputs
     * @param move Drive speed -1 backwards -> 1 forward
     * @param turn Turn rate -1 left -> 1 right
     */
    public void rawArcadeDrive(double move, double turn) {
        updateDashboard();

        robotDrive.arcadeDrive(turn, move, true);
    }

    /**
     * Pass raw values to curvature drive, shouldn't be used with joystick inputs
     * @param speed Drive speed -1 backwards -> 1 forward
     * @param rotation Turn rate -1 left -> 1 right
     * @param isQuickTurn boolean for isQuickTurn function
     */
    public void rawCurvatureDrive(double speed, double rotation, boolean isQuickTurn) {
        updateDashboard();

        robotDrive.curvatureDrive(speed, rotation, isQuickTurn);
    }

    /**
     * Pass raw values to tank drive, shouldn't be used with joystick inputs
     * @param right Speed to command right motors, -1 backwards -> 1 forward
     * @param left Speed to command left motors, -1 backwards -> 1 forward
     */
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

        // Set the setpoints
        // TODO: Need to change gyro pid relatively
        setGyroPid(turn);
        setSpeedPID(speed);

        // Just pass to arcade drive
        rawArcadeDrive(speedPIDOutput, gyroPIDOutput);
    }

    /**
     * Ramps turn and speed inputs for arcade drive
     */
    public void rampedArcade(boolean manual) {
        double turn;

        // Checks if driver controller is not being operated and if so give slight control to copilot z rotation
        if (Math.abs(Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS)) < deadZone
                && Math.abs(Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS)) < deadZone
                // If co pilot is obviously trying to turn
                && Math.abs(Robot.oi.coPilotController.getRawAxis(OI.PILOT_Z_ROTATION)) > 0.75) {
            turn = Math.signum(Robot.oi.coPilotController.getRawAxis(OI.PILOT_Z_ROTATION)) / 2;
            System.out.println();

        } else {
            turn = Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS);
        }

        double speed = scaleYAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS));

        // Initialize dt and ds
        double dt;
        double ds;

        // If manual is selected we can use our test values off SmartDashboard otherwise use regression info
        if (manual) {
            dt = Math.signum(turn - currentTurn) * getTurnRamp();
            ds = Math.signum(speed - currentSpeed) * getSpeedRamp();
        } else {
            dt = Math.signum(turn - currentTurn) * getTurnRamp();
            ds = Math.signum(speed - currentSpeed) * getScaledSpeedRamp();
        }

        // Prevent overshooting on turning
        if (turn - currentTurn < dt) {
            currentTurn = turn;
        } else {
            currentTurn += dt;
        }

        // Prevent overshooting on speed
        if (speed - currentSpeed < ds) {
            currentSpeed = speed;
        } else {
            currentSpeed += ds;
        }

        // Some SmartDashboard debugging
        SmartDashboard.putNumber("Current Speed", currentSpeed);
        SmartDashboard.putNumber("Current Turn", currentTurn);

        gyroPID.setSetpoint(gyroPID.getSetpoint() + currentTurn * getTurnRate());
        rawArcadeDrive(currentSpeed, currentTurn);
    }

    /**
     * returns the robots current average velocity
     * @return average of the right speed and left speed in ticks per 100 ms
     */
    public double getVelocity() {
        //noinspection UnnecessaryLocalVariable
        double v1 = RobotMap.driveFrontLeft.getSelectedSensorVelocity(0);
        // TODO: Get a second encoder!
        //double v2 = RobotMap.driveFrontLeft.getSelectedSensorVelocity(0);
        //return v1 + v2 / 2
        return v1;
    }

    public void setSpeedPID(double setpoint) {
        speedPID.setSetpoint(setpoint);
    }

    // ---- Getters for PID ---- //
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
     * Uses an exponential regression to get speed ramp at different elevator heights
     * @return Speed ramp to send to drive motors
     */
    public double getScaledSpeedRamp() {
        // Elevator Down 0.35, 0.05
        // Elevator Switch 0.35 0.03
        // Elevator Up 0.35, 0.005

        // Speed ramp = A*B^x
        // A = 0.05439865157
        // B = 0.9999283587
        double ramp = 0.05439865157 * Math.pow(0.9999283587, Robot.elevator.getHeight());
        SmartDashboard.putNumber("Speed Ramp", ramp);

        return ramp;
    }

    /**
     * Get the speed ramp to use in ramped arcade drive
     * @return "Speed Ramp" SmartDashboard number
     */
    public double getSpeedRamp() {
        return SmartDashboard.getNumber("Speed Ramp", 0.03);
    }

    /**
     * Get the turn ramp to use in ramped arcade drive
     * @return "Turn Ramp" SmartDashboard number
     */
    public double getTurnRamp() {
        return SmartDashboard.getNumber("Turn Ramp", 0.35);
    }

    /**
     * Degrees to edit gyro pid by in ramping
     * @return "Turn Rate" SmartDashboard number
     */
    public double getTurnRate() {
        return SmartDashboard.getNumber("Turn Rate", 1);
    }

    /**
     * Stops all drive motors
     */
    public void stopDrive() {
        RobotMap.driveFrontRight.stopMotor();
        RobotMap.driveFrontLeft.stopMotor();
    }

    /**
     * Reset drive encoders
     */
    public void zeroEncoders() {
        RobotMap.driveFrontRight.setSelectedSensorPosition(0, 0, 10);
        RobotMap.driveFrontLeft.setSelectedSensorPosition(0, 0,10);
    }
}
