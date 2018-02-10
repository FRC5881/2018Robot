package org.techvalleyhigh.frc5881.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import jaci.pathfinder.Waypoint;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.LeftStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.MiddleStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.RightStartingProfiles;

import java.util.HashMap;

public class AutonomousCommand extends CommandGroup {
    @Override
    protected boolean isFinished() {
        return false;
    }
    // public static HashMap<Integer, Autonomous> autos;

    public AutonomousCommand(String routine) {
        HashMap<Integer, Autonomous> autos = new HashMap<>();
        autos.putAll(LeftStartingProfiles.getAutos());
        autos.putAll(MiddleStartingProfiles.getAutos());
        autos.putAll(RightStartingProfiles.getAutos());

        if (!routine.equals("None")) {
            // TODO: auto commands
            if (routine.equals("Figure Eight")) {
                addSequential(new MotionProfile(TrajectoryUtil.customFigureEight(5, 5)));
                //addSequential(new MotionProfile(TrajectoryUtil.testFigureEight));
            } else if (routine.equals("Test")) {
                addSequential(new MotionProfile(autos.get(1).getPath()));
            } else if (routine.equals("Foot")) {
                Waypoint[] foot = new Waypoint[] {
                        new Waypoint(0, 0, 0),
                        new Waypoint(1, 0, 0),
                };
                addSequential(new MotionProfile(foot));
            }
        }
    }
}
