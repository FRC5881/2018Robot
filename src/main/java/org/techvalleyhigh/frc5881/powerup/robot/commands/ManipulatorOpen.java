package org.techvalleyhigh.frc5881.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class ManipulatorOpen extends Command {
    public ManipulatorOpen() {
        super();
    }
    public ManipulatorOpen(String name) {
        super(name);
    }
    @Override
    protected void end(){
    }
    @Override
    protected boolean isFinished() {
        return false;
    }
}
