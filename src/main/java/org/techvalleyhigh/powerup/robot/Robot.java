package org.techvalleyhigh.powerup.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.powerup.robot.commands.AutonomousCommand;
import org.techvalleyhigh.powerup.robot.commands.Drive;
import org.techvalleyhigh.powerup.robot.subsystem.DriveControl;


public class Robot extends TimedRobot {
    // Define OI and subsystems
    public static OI oi;
    public static DriveControl driveControl;

    // Define drive command
    public static Drive driveCommand;

    // Define auto code
    public static Command autonomousCommand;
    public static SendableChooser autoChooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        RobotMap.init();

        // Define Subsystems
        driveControl = new DriveControl();

        // Define drive command to during tele - op
        driveCommand = new Drive();

        // THEN DEFINE OI
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Instantiate the command used for the autonomous period
        autonomousCommand = null;

        // Add Auto commands to the smart dashboard
        autoChooser = new SendableChooser();
        // TODO: Is the line below an error?
        autoChooser.addDefault("Do Nothing", new AutonomousCommand("None"));

        SmartDashboard.putData("Autonomous Mode Selection", autoChooser);

        SmartDashboard.putData(Scheduler.getInstance());
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        if (autoChooser.getSelected() != null) {
            autonomousCommand = (Command) autoChooser.getSelected();
            autonomousCommand.start();
        } else {
            System.out.println("Null Auto Chooser");
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();

        if (driveCommand != null) {
            driveCommand.start();
        } else {
            System.err.println("teleopInit() Failed to start Drive command due to null");
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
        // TODO: LiveWindow.run() is deprecated
        LiveWindow.run();
    }
}
