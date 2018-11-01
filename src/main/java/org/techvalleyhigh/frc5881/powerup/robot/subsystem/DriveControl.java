package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.OI;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;


/**
 * Subsystem to control everything that has to do with drive (except profiles profiling)
 */
public class DriveControl extends Subsystem {
    // SmartDashboard key for Gyro Tolerance
    private static final String GYRO_TOLERANCE = "Gyro Tolerance %";

    // SmartDashboard key for Auto Turn Speed
    private static final String AUTO_TURN_SPEED = "Auto Turn Speed %";

    // SmartDashboard key for X_AXIS sensitivity
    private static final String X_AXIS_SENSITIVITY = "X Axis sensitivity";

    // SmartDashboard key for Y_AXIS sensitivity
    private static final String Y_AXIS_SENSITIVITY = "Y Axis sensitivity";

    // Joystick dead zone
    private static final double deadZone = 0.2;

    // Robot drive
    private DifferentialDrive robotDrive;

    // PIDs
    private static PIDController gyroPID;

    // PID output
    public double gyroPIDOutput;

    // Previous voltage
    public double voltage = 0;

    public static final double maxVoltage = 12;

    /**
     * Used for converting feet to ticks.
     * 1 rotation = 2( pi )( wheel radius )
     * 1 rotation = 2( pi )( 1/4 feet )
     * 1 rotation = pi / 2 feet
     * 1 tick = pi / (2 * 1440) feet
     * 2 * 1440 / pi ticks = 1 foot
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

        addChild(RobotMap.driveFrontLeft);

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

        double[] gyro = get_gyroPIDf();

        // Create PID controls
        gyroPID = new PIDController(gyro[0], gyro[1], gyro[2], gyro[3],
                RobotMap.digitalGyro, output -> gyroPIDOutput = output);

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
        SmartDashboard.putNumber(X_AXIS_SENSITIVITY, -0.80);
        SmartDashboard.putNumber(Y_AXIS_SENSITIVITY, -1.0);


        // --- Pid controls --- //
        SmartDashboard.putNumber("Left kP", 1.6);
        SmartDashboard.putNumber("Left kI", 0.0);
        SmartDashboard.putNumber("Left kD", 0.0);
        SmartDashboard.putNumber("Left kF", 0.0);

        SmartDashboard.putNumber("Right kP", 1.6);
        SmartDashboard.putNumber("Right kI", 0.0);
        SmartDashboard.putNumber("Right kD", 0.0);
        SmartDashboard.putNumber("Right kF", 0.0);

        // Gyrp PIDf
        SmartDashboard.putNumberArray("Gyro PIDf", new double[]{0.057, 0.000000001, 0.14, 0.0});

        // Speed control
        SmartDashboard.putNumber("Speed A1", 0.1);
        SmartDashboard.putNumber("Speed A2", 0.1);
        SmartDashboard.putNumber("Speed B1", 0.1);
        SmartDashboard.putNumber("Speed B2", 0.1);
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
        double[] gyro = get_gyroPIDf();
        gyroPID.setP(gyro[0]);
        gyroPID.setI(gyro[1]);
        gyroPID.setD(gyro[2]);
        gyroPID.setF(gyro[3]);
        gyroPID.setPercentTolerance(getGyroTolerance());
        gyroPID.reset();

        // Just keep the PIDs running
        gyroPID.enable();
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

    public double[] get_gyroPIDf() {
        return SmartDashboard.getNumberArray("Gyro PIDf", new double[] {0, 0, 0, 0});
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
        double turn = scaleXAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS));
        double speed = scaleYAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS));

        robotDrive.arcadeDrive(turn, speed, false);
    }

    /**
     * Implements tank drive with joystick inputs
     */
    public void tankJoystickInputs() {
        double right = scaleYAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_Y_AXIS));
        double left = scaleYAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS));

        rawTankDrive(right, left);
    }

    /**
     * Implements tank drive with joystick inputs but using ControlMode.velocity
     * Used for testing only
     */
    public void velocityTankJoystickInputs() {
        // feet per second
        double right = scaleYAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_Y_AXIS)) * 5.0;
        double left = scaleYAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS)) * 5.0;

        // w = v / r
        double wRight = right / 0.25;
        double wLeft = left / 0.25;

        // 2 PI rad/s = 1440 ticks/second
        // w * 1440 / 2 PI = ticks per second
        // w * 1440 / 2Pi / 10 = ticks per 100 milliseconds
        RobotMap.driveFrontLeft.set(ControlMode.Velocity, wLeft * 1440); // (2 * Math.PI));
        RobotMap.driveFrontRight.set(ControlMode.Velocity, wRight * 1440); // (2 * Math.PI));
    }

    // --- Raw drive methods to be used during autonomous --- //

    /**
     * Pass raw values to arcade drive, don't pass joystick inputs directly
     * @param move Drive speed -1 backwards -> 1 forward
     * @param turn Turn rate -1 left -> 1 right
     */
    public void rawArcadeDrive(double move, double turn) {
        updateDashboard();

        robotDrive.arcadeDrive(turn, move, false);
    }

    /**
     * Pass raw values to tank drive, don't pass joystick inputs directly
     * @param right Speed to command right motors, -1 backwards -> 1 forward
     * @param left Speed to command left motors, -1 backwards -> 1 forward
     */
    public void rawTankDrive(double right, double left) {
        updateDashboard();

        robotDrive.tankDrive(left, right, false);
    }

    // ---- Getters ---- //
    public double getRight_kP() {
        return SmartDashboard.getNumber("Right kP", 0);
    }
    public double getRight_kI() {
        return SmartDashboard.getNumber("Right kI", 0);
    }
    public double getRight_kD() {
        return SmartDashboard.getNumber("Right kD", 0);
    }
    public double getRight_kF() {
        return SmartDashboard.getNumber("Right kF", 0);
    }

    public double getLeft_kP() {
        return SmartDashboard.getNumber("Left kP", 0);
    }
    public double getLeft_kI() {
        return SmartDashboard.getNumber("Left kI", 0);
    }
    public double getLeft_kD() {
        return SmartDashboard.getNumber("Left kD", 0);
    }
    public double getLeft_kF() {
        return SmartDashboard.getNumber("Left kF", 0);
    }




    // ----------------------- Speed Control ----------------------- //
    /**
     * Ramps turn and speed inputs for arcade drive
     */

    public void rampedArcade() {
        // Get and scale controller inputs
        double speed = scaleYAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS));
        double turn = scaleXAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS));

        // Get elevator height %
        double pHeight = Robot.elevator.getHeight() / Elevator.maxTicks;
        // If our height is negative
        System.out.println(pHeight);
        if (pHeight < 0) {
            // Run normal arcade drive
            arcadeJoystickInputs();
            return;
        }

        // Initialize Maximum Change in voltage
        double dV;
        // We have different ramps based on wanted direction
        if (speed >= 0) {
            // dV = a1 * (1 - (1 - a2/a1)*x^2)
            dV = getA1() * (1 - (1 - getA2()/getA1()) * pHeight * pHeight);
        } else {
            // dV = b1 * (1 - (1 - b2/b1)*x^2)
            dV = getB1() * (1 - (1 - getB2()/getB1()) * pHeight * pHeight);
        }

        // Requested change in voltage
        double difference = 12 * speed - voltage;

        // dV is the max change if dV < |the requested change| just set to requested voltage
        if (Math.abs(difference) < dV) {
            voltage = 12 * speed;
        } else {
            // If speed is position
            if (difference >= 0) {
                voltage += dV;
            } else {
                voltage -= dV;
            }
        }

        // Update voltage
        SmartDashboard.putNumber("Voltage", voltage);

        // Drive
        rawArcadeDrive(voltage / maxVoltage, turn);
    }

    /**
     * Returns the robots current average velocity
     * @return average of the right speed and left speed in feet per second
     */
    public double getVelocity() {
        double v1 = RobotMap.driveFrontLeft.getSelectedSensorVelocity(0);
        double v2 = RobotMap.driveFrontLeft.getSelectedSensorVelocity(0);

        double ave = (v1 + v2) / 2;
        // Convert ticks per 100 milliseconds to ticks per second
        double ticksPerSecond = ave * 10;

        // Then to feet per second
        return ticksPerSecond / ticksPerFoot;
    }

    // Change in voltage per second when elevator is down going forward
    public double getA1() {
        return SmartDashboard.getNumber("Speed A1", 0) / 50.0;
    }

    // Change in voltage per second when elevator is up going forward
    public double getA2() {
        return SmartDashboard.getNumber("Speed A2", 0) / 50.0;
    }

    // Change in voltage per second when elevator is down going backwards
    public double getB1() {
        return SmartDashboard.getNumber("Speed B1", 0) / 50.0;
    }

    // Change in voltage per second when elevator is up going backwards
    public double getB2() {
        return SmartDashboard.getNumber("Speed B2", 0) / 50.0;
    }

    public void assistedDrive() {
        double speed = scaleYAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_LEFT_Y_AXIS));
        double turn = scaleXAxis(Robot.oi.driverController.getRawAxis(OI.XBOX_RIGHT_X_AXIS));

        // Edit the gyro set point
        gyroPID.setSetpoint(getGyroSetpoint() + turn);

        // Pass speed and gyro PID output to arcade drive
        rawArcadeDrive(speed, gyroPIDOutput);
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
