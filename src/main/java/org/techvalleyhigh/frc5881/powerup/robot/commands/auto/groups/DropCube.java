package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorOpen;

public class DropCube extends CommandGroup {
    private ManipulatorOpen open;

    public DropCube() {
        this.open = new ManipulatorOpen();
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }
}