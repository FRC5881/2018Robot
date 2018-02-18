package org.techvalleyhigh.frc5881.powerup.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    // Define drive command

    //public static Drive driveCommand;
    public static ElevatorDrive elevatorCommand;
    public static Command driveCommand;

    public static SendableChooser<Command> driveChooser;

    // Define auto code
    public static Command autonomousCommand;
    public static SendableChooser<AutonomousCommand> autoChooser;

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

        // Define drive and elevator command to during tele - op
        //driveCommand = new Drive();
        elevatorCommand = new ElevatorDrive();

        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Instantiate the command used for the autonomous period
        autonomousCommand = null;

        // Add Auto commands to the smart dashboard
        autoChooser = new SendableChooser<>();
        autoChooser.addDefault("Do Nothing", new AutonomousCommand("None"));
        autoChooser.addObject("Figure 8", new AutonomousCommand("Figure Eight"));

        SmartDashboard.putData("Autonomous Mode Selection", autoChooser);
        //TODO: Test this code to see if it actually works and then adapt it to the Autonomous Decoder code
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
        System.out.println("We've been disabled :(");
    }

    @Override
    public void disabledPeriodic() {
        String autoOptions = SmartDashboard.getString("Possible Paths", "1-4,7-10,15-20,22,24");
        Scheduler.getInstance().run();

        // TODO Pull SD Auto Value and check for valid
        if (AutonomousDecoder.isValidIntRangeInput(autoOptions)){
            SmartDashboard.putBoolean("Paths Are Valid", true);
        }
        else {
            //System.out.println("Warning! Current chosen path is invalid! Please input path number!");
            SmartDashboard.putBoolean("Paths Are Valid", false);
        }
    }

    @Override
    public void autonomousInit() {
        String autoOptions = SmartDashboard.getString("Possible Paths", "1-4,7-10,15-20,22,24");

        if (AutonomousDecoder.isValidIntRangeInput(autoOptions)) {
            ArrayList<Integer> autos = AutonomousDecoder.getIntRanges(autoOptions);
            AutonomousCommand run = new AutonomousCommand(autoOptions);
        }
        if (autoChooser.getSelected() != null) {
            autonomousCommand = autoChooser.getSelected();
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
        // Ends autonomous command
        if (autonomousCommand != null) autonomousCommand.cancel();

        if (elevatorCommand != null) {
            elevatorCommand.start();
            // Get selected drive command and start it
        }

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

    }
}
