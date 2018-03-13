package org.techvalleyhigh.frc5881.powerup.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;

/**
 * Handles drive team control of the elevator and safeties
 */
public class ElevatorDrive extends Command {
    /**
     * Timeout in milliseconds
     */
    private static final int timeoutKill = 20 * 1000;

    /**
     *  last measured time
     */
    private long time;

    /**
     * Last setpoint
     */
    private double lastpoint;


    public ElevatorDrive() {
        requires(Robot.elevator);
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        System.out.println("Initializing elevator command");

        // Init PID control for the elevator
        Robot.elevator.initPID();

        // Init lastpoint
        lastpoint = 0;
        resetTimeouts();
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    @Override
    protected void execute() {
        // Try to drive with inputs first
        Robot.elevator.driveControllerInputs();

        // Override if we're targeting the switch or scale or floor
        if (Robot.oi.coPilotTopBackLeft.get()) {
            Robot.elevator.setSwitch();

        } else if (Robot.oi.coPilotTopBackRight.get()) {
            Robot.elevator.setScale();

        } else if(Robot.oi.coPilotLeftTrigger.get()) {
            Robot.elevator.setFloor();
        }

        // Make sure we're moving and not burning out the motors
        double newpoint = Robot.elevator.getSetpoint();
        if (lastpoint != newpoint) {
            resetTimeouts();
        } else {
            long current = System.currentTimeMillis();
            if (current - time > timeoutKill && Math.abs(Robot.elevator.getError()) > 1440) {
                // Kill
                RobotMap.elevatorTalonMaster.disable();
                System.out.println("Killing elevator for safety");
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
    protected void end(){
        System.out.println("Elevator drive command ended... That shouldn't happen");
        Robot.elevator.stop();
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

