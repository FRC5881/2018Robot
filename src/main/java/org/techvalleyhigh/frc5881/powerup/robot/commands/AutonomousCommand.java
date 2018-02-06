package org.techvalleyhigh.frc5881.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

public class AutonomousCommand extends CommandGroup {
    @Override
    protected boolean isFinished() {
        return false;
    }

    public AutonomousCommand(String routine) {
        if (!routine.equals("None")) {
            // TODO: auto commands
            if (routine.equals("Figure Eight")) {
                new MotionProfile(TrajectoryUtil.testFigureEight);
            }
        }
    }
}
