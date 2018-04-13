package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

/**
 * Wait for given time then change arm setpoint to this classes input
 */
public class SetArm extends Command {
    /**
     * Encoder ticks to set setpoint
     */
    private double setpoint;

    /**
     * Milliseconds to wait before sending the okay to move the arm
     */
    private long timeToWait;

    /**
     * The current time when the command was called
     */
    private long startTime;

    public SetArm(double setpoint, long timeToWait) {
        requires(Robot.arm);
        this.setpoint = setpoint;
        this.timeToWait = timeToWait;
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    @Override
    protected void execute() {

    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * Since this is a waiting command call end() once we've waited enough time
     */
    @Override
    protected boolean isFinished() {
        System.out.println(System.currentTimeMillis() - startTime >= timeToWait);

        return System.currentTimeMillis() - startTime >= timeToWait;
    }

    /**
     * Called once after isFinished returns true
     */
    @Override
    protected void end() {
        System.out.println("Trying my hardest");

        Robot.arm.setSetpoint(this.setpoint);
    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {
        // Don't call end because that'd move the arm
        System.out.println("Set Arm command interrupted... That shouldn't happen");
    }
}
