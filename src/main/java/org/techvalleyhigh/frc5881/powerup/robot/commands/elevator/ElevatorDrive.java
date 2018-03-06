package org.techvalleyhigh.frc5881.powerup.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;

public class ElevatorDrive extends Command {

    // Timeouts in milliseconds
    private static final int timeoutKill = 20 * 1000;

    // last measured time
    private long time;

    // Last setpoint
    private double lastpoint;



    public ElevatorDrive() {
        requires(Robot.elevator);
        Robot.elevator.initPID();

        // Init lastpoint
        lastpoint = 0;
        resetTimeouts();
    }

    @Override
    protected void initialize() {
        System.out.println("Initializing elevator command");
    }

    @Override
    protected void execute(){
        Robot.elevator.driveControllerInput();

        if (Robot.oi.coPilotTopBackLeft.get()) {
            Robot.elevator.setSwitch();
        } else if (Robot.oi.coPilotTopBackRight.get()) {
            Robot.elevator.setScale();
        }

        double newpoint = Robot.elevator.getSetpoint();
        if (lastpoint != newpoint) {
            resetTimeouts();
        } else {
            long current = System.currentTimeMillis();
            if (current - time > timeoutKill) {
                // Kill
                RobotMap.elevatorTalonMaster.disable();
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end(){
        System.out.println("Elevator drive command ended... That shouldn't happen");
        Robot.elevator.stop();
    }

    @Override
    protected void interrupted() {
        end();
    }

    private void resetTimeouts() {
        time = System.currentTimeMillis();
    }
}

