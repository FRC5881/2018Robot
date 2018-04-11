package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.tuning;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class VelocityTest extends Command {
    private WPI_TalonSRX motor;
    private double velocity;

    /**
     * Seconds to wait
     */
    private double time;

    /**
     * Runs speed test on a motor given a velocity following some time
     * @param motor motor to run test on
     * @param velocity velocity to target
     * @param time seconds to run
     */
    public VelocityTest(WPI_TalonSRX motor, double velocity, double time) {
        requires(Robot.driveControl);

        this.motor = motor;
        this.velocity = velocity;
        this.time = time;
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
        motor.set(ControlMode.Velocity, velocity);
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * Since this is a drive command we never want it to end
     */
    @Override
    protected boolean isFinished() {
        return timeSinceInitialized() >= time;
    }

    /**
     * Called once after isFinished returns true OR the command is interrupted
     */
    @Override
    protected void end() {
        System.out.println("Speed test finished...");
        System.out.println("Motor reached: " + Robot.driveControl.getVelocity() + " feet per second\n" +
                "after running for " + time + " milliseconds while targeting " + velocity + " units per 100 milliseconds");

        
    }

    /**
     * Called when another command which requires one or more of the same
     * subsystems is scheduled to run
     */
    @Override
    protected void interrupted() {
        System.out.println("Speed test interrupted... That shouldn't happen");
    }
}
