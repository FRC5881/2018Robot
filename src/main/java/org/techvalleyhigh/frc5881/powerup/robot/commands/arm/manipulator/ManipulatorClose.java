package org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class ManipulatorClose extends Command {
    /**
     * Milliseconds to wait before sending the okay to move the arm
     */
    private long timeToWait;

    /**
     * The current time when the command was called
     */
    private long startTime;

    /**
     * Create command with zero timeToWait
     */
    public ManipulatorClose() {
        this(0);
    }

    /**
     * Create command with chosen timeToWait
     * @param timeToWait seconds to wait
     */
    public ManipulatorClose(long timeToWait) {
        this.timeToWait = timeToWait * 1000;
        this.startTime = System.currentTimeMillis();

        requires(Robot.manipulator);
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        System.out.println("ManipulatorClose command initialized... Waiting " + timeToWait/1000.0 + " seconds");
    }

    /**
     * Called repeatedly when this Command is scheduled to run (just once)
     */
    @Override
    protected void execute() {
        if (doneWaiting()) Robot.manipulator.closeGrabbers();
    }

    /**
     *  Check if we've waited enough time before closing
     */
    private boolean doneWaiting() {
        return System.currentTimeMillis() - startTime >= timeToWait;
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * This is a pneumatic command so needs to end instantly
     */
    @Override
    protected boolean isFinished() {
        return doneWaiting();
    }

    /**
     * Called once after isFinished returns true OR the command is interrupted
     */
    @Override
    protected void end() {
        System.out.println("Manipulator Closed");
    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {
        System.out.println("Manipulator Close command was interrupted... That shouldn't happen");
        end();
    }
}
