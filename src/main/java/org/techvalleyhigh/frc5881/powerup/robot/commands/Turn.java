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
        this.absoluteBearing = this.relativeBearing + Robot.driveControl.getGyroAngle();
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */

    @Override
    protected void execute() {
        Robot.driveControl.rawCurvatureDrive(0, relativeBearing, true);
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * End command once we are within gyro tolerance of our absolute bearing
     */
    @Override
    protected boolean isFinished() {
        return Math.abs(this.absoluteBearing - Robot.driveControl.getGyroAngle()) < Robot.driveControl.getAutoGyroTolerance();
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
