package org.techvalleyhigh.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup {
    // TODO: Is this definition necessary???
    public AutonomousCommand CommandGroup;

    @Override
    protected boolean isFinished() {
        return false;
    }

    public AutonomousCommand(String routine) {
        if (!routine.equals("None")) {
            // TODO: auto commands
        }
    }
}
