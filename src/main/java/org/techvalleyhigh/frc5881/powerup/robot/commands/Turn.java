package org.techvalleyhigh.frc5881.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

// WORK IN PROGRESS
/**
 * Takes in relative degrees to turn during autonomous and will do just that, turn
 */
public class Turn extends Command {
    private double relativeBearing;
    private double absoluteBearing;

    public Turn(double relativeBearing) {
        this.relativeBearing = relativeBearing;
        requires(Robot.driveControl);
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        System.out.println("Turning " + this.absoluteBearing);
        this.absoluteBearing = this.relativeBearing + Robot.driveControl.getGyroAngle();
        Robot.driveControl.initPID();
        Robot.driveControl.writeGyroPid(absoluteBearing);
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */

    @Override
    protected void execute() {
        Robot.driveControl.rawArcadeDrive(0, Robot.driveControl.gyroPIDOutput * 0.75);
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * End command once we are within gyro tolerance of our absolute bearing
     */
    @Override
    protected boolean isFinished() {
        System.out.println("Turn finished");
        return Robot.driveControl.getGyroOnTarget();
    }

    /**
     * Called once after isFinished returns true OR the command is interrupted
     */
    @Override
    protected void end() {
        Robot.driveControl.stopDrive();
    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {
        end();
    }
}
