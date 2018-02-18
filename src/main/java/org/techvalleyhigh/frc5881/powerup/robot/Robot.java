package org.techvalleyhigh.frc5881.powerup.robot;

import com.ctre.phoenix.motion.MotionProfileStatus;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.commands.drive.ArcadeDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.drive.CurvatureDrive;
import org.techvalleyhigh.frc5881.powerup.robot.commands.drive.TankDrive;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.DriveControl;
import org.techvalleyhigh.frc5881.powerup.robot.commands.AutonomousCommand;
import org.techvalleyhigh.frc5881.powerup.robot.utils.AutonomousDecoder;

import java.util.ArrayList;


public class Robot extends TimedRobot {
    // Define OI and subsystems
    public static OI oi;
    public static DriveControl driveControl;
    //public static Manipulator manipulator;
    //public static Arm arm;
    //public static Elevator elevator;

    // Define drive code
    public static SendableChooser<Command> driveChooser;
    public static Command driveCommand;

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
        //arm = new Arm();
        //elevator = new Elevator();

        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Instantiate the command used for the autonomous period
        autonomousCommand = null;
        driveCommand = null;

        SmartDashboard.putString("Possible Paths", "None");

        // Drive Control Selection
        driveChooser = new SendableChooser<>();
        driveChooser.addDefault("Arcade Drive", new ArcadeDrive());
        driveChooser.addObject("Tank Drive", new TankDrive());
        driveChooser.addObject("Curvature Drive", new CurvatureDrive());

        SmartDashboard.putData("Drive Mode Selection", driveChooser);

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

        String autoOptions = SmartDashboard.getString("Possible Paths", "1-4,7-10,15-20,22,24");
        Scheduler.getInstance().run();

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
        System.out.println("Auto Init");
        String autoOptions = SmartDashboard.getString("Possible Paths", "1-4,7-10,15-20,22,24");

        // Clear trajectories and PID set point
        RobotMap.driveFrontRight.clearMotionProfileTrajectories();
        RobotMap.driveFrontLeft.clearMotionProfileTrajectories();

        RobotMap.driveFrontRight.pidWrite(0);
        RobotMap.driveFrontLeft.pidWrite(0);

        if (AutonomousDecoder.isValidIntRangeInput(autoOptions)) {
            // Get int ranges
            ArrayList<Integer> autos = AutonomousDecoder.getIntRanges(autoOptions);
            // Set autonomous command
            System.out.println("Command");
            autonomousCommand = new AutonomousCommand(autos);
            autonomousCommand.start();
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        updateSensors();
        MotionProfileStatus status = new MotionProfileStatus();
        RobotMap.driveFrontLeft.getMotionProfileStatus(status);
        SmartDashboard.putNumber("Btm Buffer Count", status.btmBufferCnt);
        SmartDashboard.putNumber("Top Buffer Count", status.topBufferCnt);

        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        // Ends autonomous command
        if (autonomousCommand != null) autonomousCommand.cancel();

        // Get selected drive command and start it
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

    }

    public void updateSensors() {
        SmartDashboard.putNumber("Right encoder", RobotMap.driveFrontRight.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Left encoder", RobotMap.driveFrontLeft.getSelectedSensorPosition(0));

        SmartDashboard.putNumber("Gyro output", driveControl.gyroPIDOutput);
        SmartDashboard.putNumber("Gyro setpoint", driveControl.getGyroSetpoint());
        SmartDashboard.putNumber("Gyro error", driveControl.getGyroError());
    }
}
