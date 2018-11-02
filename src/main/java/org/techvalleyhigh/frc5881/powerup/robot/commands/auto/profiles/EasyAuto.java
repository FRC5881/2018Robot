package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.profiles;

import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class EasyAuto extends Command {
    private double volts, time;
    private double startTime;

    /**
     * Provide a constant volts to the drive motors, enough to get us across the auto line
     * @param volts volts to apply
     * @param time how many milliseconds to apply volts for
     */
    public EasyAuto(double volts, double time) {
        this.volts = volts;
        this.time = time * 1000;

        requires(Robot.driveControl);
    }

    /**
     * Called just before this Command runs the first time
     */
    @Override
    protected void initialize() {
        System.out.println("Initializing EasyAuto to cross the autoline");
        System.out.println(volts + " volts for " + time + " seconds");
        startTime = System.currentTimeMillis();
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    @Override
    protected void execute() {
        if (!isDone()) {
            Robot.driveControl.rawArcadeDrive(volts/12, 0);
        }
    }

    /**
     * Has run time expired
     * @return boolean false if time hasn't expired
     */
    private boolean isDone() {
        System.out.println(System.currentTimeMillis() - startTime);

        return System.currentTimeMillis() - startTime > time;
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * Since this is a drive command we never want it to end
     */
    @Override
    protected boolean isFinished() {
        return isDone();
    }

    /**
     * Called once after isFinished returns true OR the command is interrupted
     */
    @Override
    protected void end() {
        Robot.driveControl.stopDrive();
        System.out.println("Easy Auto Finished");
    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {
        System.out.println("EasyAuto interrupted, that shouldn't happen");
        end();
    }
}
