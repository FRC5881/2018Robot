package org.techvalleyhigh.frc5881.powerup.robot.Robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.Robot.commands.AutonomousCommand;
import org.techvalleyhigh.frc5881.powerup.robot.Robot.commands.Drive;
import org.techvalleyhigh.frc5881.powerup.robot.Robot.subsystems.DriveControl;

public class Robot extends TimedRobot {
    // Define OI and subsystems
    public static OI oi;

    public static DriveControl driveControl;

    public static Drive driveCommand;

    public static Command autonomousCommand;
    public static SendableChooser autoChooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        RobotMap.init();

        driveControl = new DriveControl();

        driveCommand = new Drive();

        // Define Subsystems

        // THEN DEFINE OI
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Instantiate the command used for the autonomous period
        autonomousCommand = null;

        autoChooser = new SendableChooser();

        autoChooser.addDefault("Do Nothing", new AutonomousCommand("None"));

        SmartDashboard.putData("Autonomous Mode Selection", autoChooser);

        SmartDashboard.putData(Scheduler.getInstance());
    }

    @Override
    public void disableInit() {

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
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }
    public void teleopInit() {
        if (autonomousCommand != null) autonomousCommand.cancel();

        if (driveCommand != null) {
            driveCommand.start();
        } else {
            System.err.println("telopInit() Failed to start Drive command due to null");
        }
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    public void testPeriodic() {

        LiveWindow.run();
    }
}