package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.Straight;

public class BackUp extends CommandGroup {
    private Straight move;

    public BackUp(double distance) {
        this.move = new Straight(-distance);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return move.isCompleted();
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
        end();
    }
}
