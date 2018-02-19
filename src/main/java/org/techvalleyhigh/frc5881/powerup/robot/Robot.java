package org.techvalleyhigh.frc5881.powerup.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ArmDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.drive.ArcadeDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.drive.CurvatureDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.drive.TankDrive;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Arm;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.DriveControl;
import org.techvalleyhigh.frc5881.powerup.robot.commands.AutonomousCommand;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Elevator;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Manipulator;
import org.techvalleyhigh.frc5881.powerup.robot.utils.AutonomousDecoder;
import java.util.ArrayList;


public class Robot extends TimedRobot {
    // Define OI and subsystems
    public static OI oi;
    public static DriveControl driveControl;
    public static Manipulator manipulator;
    public static Arm arm;
    public static Elevator elevator;

    // Define drive commands
    public static ElevatorDrive elevatorCommand;
    public static ArmDrive armCommand;
    public static Command driveCommand;
    public static SendableChooser<Command> driveChooser;

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
        //manipulator = new Manipulator();
        arm = new Arm();
        elevator = new Elevator();

        // Define drive and elevator command to during tele - op
        elevatorCommand = new ElevatorDrive();
        armCommand = new ArmDrive();

        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Instantiate the command used for the autonomous period
        autonomousCommand = null;

        // Add Auto commands to the smart dashboard
        SmartDashboard.putString("Possible Paths", "1-4,7-10,15-20,22,24");

        // Drive Control Selection
        driveChooser = new SendableChooser<>();
        driveChooser.addDefault("Arcade Drive", new ArcadeDrive());
        driveChooser.addObject("Tank Drive", new TankDrive());
        driveChooser.addObject("Curvature Drive", new CurvatureDrive());

        SmartDashboard.putData("Drive Command", driveChooser);

        SmartDashboard.putData(Scheduler.getInstance());
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {
        elevator.disableRatchet();
        System.out.println("We've been disabled :(");
    }

    @Override
    public void disabledPeriodic() {
        String autoOptions = SmartDashboard.getString("Possible Paths", "1-4,7-10,15-20,22,24");
        Scheduler.getInstance().run();

        SmartDashboard.putBoolean("Paths Are Valid", AutonomousDecoder.isValidIntRangeInput(autoOptions));
    }

    @Override
    public void autonomousInit() {
        String autoOptions = SmartDashboard.getString("Possible Paths", "1-4,7-10,15-20,22,24");

        if (AutonomousDecoder.isValidIntRangeInput(autoOptions)) {
            AutonomousCommand autonomousCommand = new AutonomousCommand(autoOptions);
            autonomousCommand.start();
        } else {
            System.err.println("YOU DIDN'T CHOOSE AN AUTO!!!!!");
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // Ends autonomous command
        if (autonomousCommand != null) autonomousCommand.cancel();

        // Starts elevator command
        if (elevatorCommand != null) {
            elevatorCommand.start();
            // Get selected drive command and start it
        } else {
            System.err.println("teleopInit() failed to start elevator command due to null");
        }

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
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        System.out.println(oi.pilotController.getPOV() + " POV");
    }

    private void updateSensors() {
        SmartDashboard.putBoolean("Ratchet Enabled", elevator.getRatchetEnabled());
        SmartDashboard.putBoolean("Grabber Enabled", manipulator.getGrabberEnabled());
        SmartDashboard.putNumber("Right Encoder", RobotMap.driveFrontRight.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Left Encoder", RobotMap.driveFrontLeft.getSelectedSensorPosition(0));
    }
}
