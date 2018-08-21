package org.techvalleyhigh.frc5881.powerup.robot.commands.arm;
// TODO: Get an arm

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;

/**
 * Handles drive team control of the arm and safeties
 */
public class ArmDrive extends Command {
    /**
     * Timeout in milliseconds
     */
    private static final int timeoutKill = 20 * 1000;

    /**
     * Last measured time
     */
    private double time;

    /**
     * Last setpoint
     */
    private double lastpoint;

    public ArmDrive() {
        requires(Robot.arm);
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        System.out.println("Initializing arm command");

        // Init lastpoint
        lastpoint = 0;

        // Prevent the arm from jumping to last enabled position
        Robot.arm.setSetpoint(Robot.arm.getPosition());
        resetTimeouts();
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    @Override
    protected void execute() {
        // Try to drive with inputs first
        Robot.arm.driveControllerInput();

        // Make sure we're moving and not burning out the motors
        double newpoint = Robot.arm.getSetpoint();
        if (lastpoint != newpoint) {
            resetTimeouts();
        } else {
            long current = System.currentTimeMillis();
            if (current - time > timeoutKill && Math.abs(Robot.arm.getError()) > 1440) {
                // Kill
                RobotMap.armTalon.disable();
                System.out.println("Killing arm for safety");
            }
        }
        lastpoint = newpoint;
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * Since this is a drive command we never want it to end
     */
    @Override
    protected boolean isFinished() {
        return false;
    }

    /**
     * Called once after isFinished returns true OR the command is interrupted
     */
    @Override
    protected void end() {
        System.out.println("Arm drive command ended... That shouldn't happen");
        Robot.arm.stop();
    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {
        end();
    }

    /**
     * Reset the timeouts
     */
    private void resetTimeouts() {
        time = System.currentTimeMillis();
    }
}
