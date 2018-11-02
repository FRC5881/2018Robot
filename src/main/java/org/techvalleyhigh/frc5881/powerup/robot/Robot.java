package org.techvalleyhigh.frc5881.powerup.robot;

import edu.wpi.cscore.VideoCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ArmDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ArmDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.drive.*;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorDrive;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.*;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.AutonomousCommand;
import org.techvalleyhigh.frc5881.powerup.robot.utils.AutonomousDecoder;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;

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

    // Choosers
    public static SendableChooser<Command> driveChooser;
    public static SendableChooser<AutonomousCommand.ProfileMode> profileChooser;
    public static SendableChooser<AutonomousCommand.TestMode> testChooser;

    // Define auto code
    public static Command autonomousCommand;

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
        autonomousCommand = null;
        driveCommand = null;

        // Add Auto commands to the smart dashboard
        SmartDashboard.putString("Possible Paths", "None");
        SmartDashboard.putNumber("Seconds to wait", 0);

        // Drive Control Selection
        driveChooser = new SendableChooser<>();
        driveChooser.addDefault("Ramped Arcade Drive", new RampedArcade());
        driveChooser.addObject("Assisted Drive", new AssistedDrive());
        driveChooser.addObject("Arcade Drive", new ArcadeDrive());
        driveChooser.addObject("Tank Drive", new TankDrive());
        driveChooser.addObject("Velocity Tank", new VelocityTank());
        SmartDashboard.putData("Drive Mode Selection", driveChooser);

        // Profile Control Selection
        profileChooser = new SendableChooser<>();
        profileChooser.addDefault("Motion Profile", AutonomousCommand.ProfileMode.MOTION);
        profileChooser.addObject("Velocity Profile", AutonomousCommand.ProfileMode.VELOCITY);
        profileChooser.addObject("Position Profile", AutonomousCommand.ProfileMode.POSITION);
        SmartDashboard.putData("Auto Profile Selection", profileChooser);

        testChooser = new SendableChooser<>();
        testChooser.addDefault("NONE", AutonomousCommand.TestMode.NONE);
        testChooser.addObject("Velocity", AutonomousCommand.TestMode.VELOCITY);
        SmartDashboard.putData("Test Selection", testChooser);


        CameraServer.getInstance().startAutomaticCapture();

        /*
        NetworkTableInstance.getDefault()
                .getEntry("/CameraPublisher/NVIDIACAMERA/streams")
                .setStringArray(new String[]{"mjpeg:http://" + ADDRESS + ":" + port + "/cam.mjpg"});
        */

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
        // Update sensors
        updateSensors();

        // Provide info for autonomous
        String autoOptions = SmartDashboard.getString("Possible Paths", "None");
        Scheduler.getInstance().run();

        SmartDashboard.putBoolean("Paths Are Valid", AutonomousDecoder.isValidIntRangeInput(autoOptions));
    }

    /**
     * This function is called before autonomous periodic is called for the first time
     */
    @Override
    public void autonomousInit() {
        // Make sure the motors are in the correct states
        RobotMap.initMotorState();

        // Reset arm PID values
        arm.initPID();

        // Get autonomous selection data from driver station
        String autoOptions = SmartDashboard.getString("Possible Paths", "None");
        double seconds = SmartDashboard.getNumber("Seconds to wait", 0);
        long timeToWait = Double.valueOf(seconds).longValue() * 1000;

        // Initialize match data check
        SmartDashboard.putBoolean("Match Data", false);

        // Clear trajectories and PID set point, not so much a problem for competition but very necessary testing
        RobotMap.driveFrontRight.clearMotionProfileTrajectories();
        RobotMap.driveFrontLeft.clearMotionProfileTrajectories();

        RobotMap.driveFrontRight.pidWrite(0);
        RobotMap.driveFrontLeft.pidWrite(0);

        // Start Autonomous Command
        if (AutonomousDecoder.isValidIntRangeInput(autoOptions)
                || testChooser.getSelected() != AutonomousCommand.TestMode.NONE) {
            AutonomousCommand autonomousCommand =
                    new AutonomousCommand(AutonomousDecoder.getIntRanges(autoOptions), timeToWait);
            autonomousCommand.start();
        } else {
            System.err.println("YOU DIDN'T CHOOSE AN AUTO!!!!!");
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        updateSensors();

        // Put setpoints and errors on smartdashboard for testing
        SmartDashboard.putNumber("Right setpoint", RobotMap.driveFrontRight.getClosedLoopTarget(0));
        SmartDashboard.putNumber("Left setpoint", RobotMap.driveFrontLeft.getClosedLoopTarget(0));
        SmartDashboard.putNumber("Right Error", RobotMap.driveFrontRight.getClosedLoopError(0));
        SmartDashboard.putNumber("Left Error", RobotMap.driveFrontLeft.getClosedLoopError(0));

        Scheduler.getInstance().run();
    }

    /**
     * This function is called before teleop periodic is called for the first time
     */
    @Override
    public void teleopInit() {
        RobotMap.initMotorState();

        // Starts elevator command
        if (elevatorCommand != null) {
            elevatorCommand.start();
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
     * Update the current sensors to the SmartDashboard used for debugging
     */
    private void updateSensors() {
        SmartDashboard.putNumber("Right encoder", RobotMap.driveFrontRight.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Left encoder", RobotMap.driveFrontLeft.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Left velocity", RobotMap.driveFrontLeft.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Right velocity", RobotMap.driveFrontRight.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Front left voltage", RobotMap.driveFrontLeft.getMotorOutputVoltage());
        SmartDashboard.putNumber("Front right voltage", RobotMap.driveFrontRight.getMotorOutputVoltage());

        SmartDashboard.putBoolean("Ratchet enabled", ratchet.getRatchetEnabled());
        SmartDashboard.putBoolean("Grabber enabled", manipulator.getGrabberEnabled());

        SmartDashboard.putNumber("Elevator encoder", RobotMap.elevatorTalonMaster.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Elevator error", elevator.getError());
        SmartDashboard.putNumber("Elevator voltage", RobotMap.elevatorTalonMaster.getMotorOutputVoltage());

        SmartDashboard.putNumber("Arm encoder", RobotMap.armTalon.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Arm error", arm.getError());
        SmartDashboard.putNumber("Arm voltage", RobotMap.armTalon.getMotorOutputVoltage());

    }
}