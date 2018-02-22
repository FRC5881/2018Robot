package org.techvalleyhigh.frc5881.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

// TODO: Work in progress
public class Straight extends Command {
    private double distance;

    public Straight(double distance) {
        requires(Robot.driveControl);
        this.distance = distance;
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        System.out.println("Driving " + distance + " feet");
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */

    @Override
    protected void execute() {
        double turn = Robot.driveControl.gyroPIDOutput * Robot.driveControl.getAutoTurnSpeed();
        Robot.driveControl.rawArcadeDrive(Robot.driveControl.speedPIDOutput, turn);
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * End command once we are within tolerance
     */
    @Override
    protected boolean isFinished() {
        return true;
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
