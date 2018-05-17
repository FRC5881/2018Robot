package org.techvalleyhigh.frc5881.powerup.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ArmDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.drive.*;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorDrive;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.*;

public class Robot extends TimedRobot {
    // Define OI and subsystems
    public static OI oi;
    public static DriveControl driveControl;
    public static Manipulator manipulator;
    public static Arm arm;
    public static Elevator elevator;
    public static Ratchet ratchet;

    public static final String ADDRESS = "10.58.81.11";
    public static final int port = 8080;

    // Define drive commands
    public static ElevatorDrive elevatorCommand;
    public static ArmDrive armCommand;
    public static Command driveCommand;
    public static SendableChooser<Command> driveChooser;


    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        RobotMap.init();

        // Define Subsystems
        driveControl = new DriveControl();
        manipulator = new Manipulator();
        arm = new Arm();
        elevator = new Elevator();
        ratchet = new Ratchet();

        // Define drive and elevator command to during tele - op
        elevatorCommand = new ElevatorDrive();
        armCommand = new ArmDrive();

        /*
        OI must be constructed after subsystems. If the OI creates Commands
        (which it very likely will), subsystems are not guaranteed to be
        constructed yet. Thus, their requires() statements may grab null
        pointers. Bad news. Don't move it.
        */
        oi = new OI();

        // Instantiate the command used for the autonomous period
        driveCommand = null;


        // Drive Control Selection
        driveChooser = new SendableChooser<>();
        driveChooser.addDefault("Arcade Drive", new ArcadeDrive());
        driveChooser.addObject("Tank Drive", new TankDrive());

        SmartDashboard.putData("Drive Mode Selection", driveChooser);

        // Camera Server
        NetworkTableInstance.getDefault()
                .getEntry("/CameraPublisher/NVIDIACAMERA/streams")
                .setStringArray(new String[]{"mjpeg:http://" + ADDRESS + ":" + port + "/cam.mjpg"});

        SmartDashboard.putData(Scheduler.getInstance());
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {
        System.out.println("We've been disabled :(");
    }

    /**
     * This function is called periodically while we're disabled
     */
    @Override
    public void disabledPeriodic() {
        updateSensors();
    }

    /**
     * This function is called before autonomous periodic is called for the first time
     */
    @Override
    public void autonomousInit() {
        // Make sure the motors are in the correct states
        RobotMap.initMotorState();

        arm.initPID();

        //new Straight(12);

        // Get autonomous selection data
        String autoOptions = SmartDashboard.getString("Possible Paths", "None");
        double seconds = SmartDashboard.getNumber("Seconds to wait", 0);
        long timeToWait = Double.valueOf(seconds).longValue() * 1000;

        SmartDashboard.putBoolean("Match Data", false);

        // Clear trajectories and PID set point, not so much a problem for competition but necessary testing
        RobotMap.driveFrontRight.clearMotionProfileTrajectories();
        RobotMap.driveFrontLeft.clearMotionProfileTrajectories();

        RobotMap.driveFrontRight.pidWrite(0);
        RobotMap.driveFrontLeft.pidWrite(0);
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        updateSensors();

        Scheduler.getInstance().run();
    }

    /**
     * This function is called before teleop periodic is called for the first time
     */
    @Override
    public void teleopInit() {
        RobotMap.initMotorState();

        // Ends autonomous command

        // Starts elevator command
        if (elevatorCommand != null) {
            elevatorCommand.start();
            // Get selected drive command and start it
        } else {
            System.err.println("teleopInit() failed to start elevator command due to null");
        }


        // Start arm command
        if (armCommand != null) {
            armCommand.start();
        } else {
            System.err.println("teleopInit() failed to start arm command due to null");
        }

        // Starts drive command
        if (driveChooser.getSelected() != null) {
            driveCommand = driveChooser.getSelected();
            driveCommand.start();
        } else {
            System.err.println("teleopInit() failed to start drive command due to null");
        }

        driveControl.initPID();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        updateSensors();

        // If the drive command ends restart it
        if (!elevatorCommand.isRunning()) {
            System.out.println("Restarting the elevator command");
            elevatorCommand.start();
        }

        // If the arm command ends restart it
        if (!armCommand.isRunning()) {
            System.out.println("Restarting the arm command");
            armCommand.start();
        }

        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        updateSensors();
    }

    /**
     * This function is called before test periodic is called for the first time
     */
    @Override
    public void testInit() {

    }

    /**
     * Update the current sensors to the SmartDashboard used for a lot of debugging
     */
    private void updateSensors() {
        // Since this method is called by the periodic we use it to make sure hte compressor stays on
        RobotMap.compressor.setClosedLoopControl(true);

        /*
        MotionProfileStatus status = new MotionProfileStatus();
        RobotMap.driveFrontLeft.getMotionProfileStatus(status);
        System.out.println(status.);
        */

        SmartDashboard.putNumber("Right encoder", RobotMap.driveFrontRight.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Left encoder", RobotMap.driveFrontLeft.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Right Error", RobotMap.driveFrontRight.getClosedLoopError(0));
        SmartDashboard.putNumber("Left Error", RobotMap.driveFrontLeft.getClosedLoopError(0));
        SmartDashboard.putNumber("Left Target Velocity", RobotMap.driveFrontLeft.getActiveTrajectoryVelocity());
        SmartDashboard.putNumber("Right Target Velocity", RobotMap.driveFrontRight.getActiveTrajectoryVelocity());
        SmartDashboard.putNumber("Left Velocity", RobotMap.driveFrontLeft.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Right Velocity", RobotMap.driveFrontRight.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Left output", RobotMap.driveFrontLeft.get());
        SmartDashboard.putNumber("Right output", RobotMap.driveFrontRight.get());

        SmartDashboard.putNumber("Velocity", driveControl.getVelocity());

        SmartDashboard.putNumber("Gyro output", driveControl.gyroPIDOutput);
        SmartDashboard.putNumber("Gyro setpoint", driveControl.getGyroSetpoint());
        SmartDashboard.putNumber("Gyro error", driveControl.getGyroError());

        SmartDashboard.putBoolean("Ratchet enabled", ratchet.getRatchetEnabled());
        SmartDashboard.putBoolean("Grabber enabled", manipulator.getGrabberEnabled());

        SmartDashboard.putNumber("Elevator encoder", RobotMap.elevatorTalonMaster.getSelectedSensorPosition(0));
        //SmartDashboard.putNumber("Elevator setpoint", elevator.getSetpoint());
        SmartDashboard.putNumber("Elevator error", elevator.getError());
        SmartDashboard.putNumber("Elevator voltage", RobotMap.elevatorTalonMaster.getMotorOutputVoltage());

        SmartDashboard.putNumber("Arm encoder", RobotMap.armTalon.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Arm output", RobotMap.armTalon.getMotorOutputPercent());
        //SmartDashboard.putNumber("Arm setpoint", arm.getSetpoint());
        SmartDashboard.putNumber("Arm error", arm.getError());
        SmartDashboard.putNumber("Arm voltage", RobotMap.armTalon.getMotorOutputVoltage());

        MatchData();
    }

    /**
     * Puts owned side position on the SmartDashboard
     */
    private void MatchData() {
        /*
        if (MatchData.getOwnedSide(MatchData.GameFeature.SCALE) != MatchData.OwnedSide.UNKNOWN) {
            SmartDashboard.putBoolean("Far Right", MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_FAR) == MatchData.OwnedSide.RIGHT);
            SmartDashboard.putBoolean("Far Left", MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_FAR) == MatchData.OwnedSide.LEFT);
            SmartDashboard.putBoolean("Scale Right", MatchData.getOwnedSide(MatchData.GameFeature.SCALE) == MatchData.OwnedSide.RIGHT);
            SmartDashboard.putBoolean("Scale Left", MatchData.getOwnedSide(MatchData.GameFeature.SCALE) == MatchData.OwnedSide.LEFT);
            SmartDashboard.putBoolean("Near Right", MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR) == MatchData.OwnedSide.RIGHT);
            SmartDashboard.putBoolean("Near Left", MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR) == MatchData.OwnedSide.LEFT);
        }
        */
    }
}