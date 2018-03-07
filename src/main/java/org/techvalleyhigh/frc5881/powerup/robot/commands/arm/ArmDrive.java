package org.techvalleyhigh.frc5881.powerup.robot.commands.arm;
// TODO: Get an arm
/*
import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class ArmDrive extends Command {
    private static final int timeoutKill = 20 * 1000;
    private double lastpoint;
    private double time;

    public ArmDrive() {
        requires(Robot.arm);

        // Init lastpoint
        lastpoint = 0;
        resetTimeouts();
    }

    @Override
    protected void initialize() {
        System.out.println("Initializing arm command");
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.arm.driveJoystickInputs();

        double newpoint = Robot.arm.getSetpoint();

        if (lastpoint != newpoint) {
            resetTimeouts();
        } else {
            long current = System.currentTimeMillis();
            if (current - time > timeoutKill && Math.abs(Robot.arm.getError()) > 1440) {
                // Kill
                RobotMap.armTalon.disable();
            }
        }
        lastpoint = newpoint;
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.arm.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }


    private void resetTimeouts() {
        time = System.currentTimeMillis();
    }
}
*/