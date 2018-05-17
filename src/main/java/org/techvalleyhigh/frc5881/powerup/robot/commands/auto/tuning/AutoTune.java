package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.tuning;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;

public class AutoTune extends Command {
    public AutoTune(double TuningRate) {
        requires(Robot.driveControl);
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    @Override
    protected void execute() {

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

    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {
        end();
    }

    private double testPD(double P, double D) {
        RobotMap.driveFrontLeft.config_kP(0, P, 10);
        RobotMap.driveFrontLeft.config_kD(0, D, 10);

        double error = 0;

        return error;
    }
}
