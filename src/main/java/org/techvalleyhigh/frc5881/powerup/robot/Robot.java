package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motion.MotionProfileStatus;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ArmDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorDrive;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Arm;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Elevator;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Manipulator;

public class Robot extends TimedRobot {
    // Define OI and subsystems
    public static OI oi;
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
        manipulator = new Manipulator();
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
        driveCommand = null;

        // Add Auto commands to the smart dashboard
        SmartDashboard.putString("Possible Paths", "None");

        // Drive Control Selection
        driveChooser = new SendableChooser<>();

        SmartDashboard.putData("Drive Mode Selection", driveChooser);
        SmartDashboard.putNumber("Turn", 0);

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

    @Override
    public void disabledPeriodic() {
        updateSensors();

        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {

    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        updateSensors();
        MotionProfileStatus status = new MotionProfileStatus();
        SmartDashboard.putNumber("Btm Buffer Count", status.btmBufferCnt);
        SmartDashboard.putNumber("Top Buffer Count", status.topBufferCnt);

        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        Command test = new ManipulatorClose();
        test.start();

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
    @Override
    public void teleopPeriodic() {
        updateSensors();
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
        System.out.println(oi.pilotController.getPOV() + " POV");
    }

    /**
     * Update the current sensors to the SmartDashboard
     */
    public void updateSensors() {

        SmartDashboard.putBoolean("Ratchet Enabled", elevator.getRatchetEnabled());
        SmartDashboard.putBoolean("Grabber Enabled", manipulator.getGrabberEnabled());

        SmartDashboard.putNumber("Elevator encoder", RobotMap.elevatorTalonMaster.getSelectedSensorPosition(0));

    }
}