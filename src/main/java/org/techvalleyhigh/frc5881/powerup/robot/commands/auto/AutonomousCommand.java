package org.techvalleyhigh.frc5881.powerup.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import openrio.powerup.MatchData;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.motion.MotionProfile;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

import java.util.ArrayList;
import java.util.Map;

public class AutonomousCommand extends CommandGroup {
    @Override
    protected boolean isFinished() {
        return false;
    }

    /**
     *
     * @param chosen array list of integers
     */
    public AutonomousCommand(ArrayList<Integer> chosen) {
        // Add all the autonomous paths into one big HashMap
        Map<Integer, Autonomous> autos = TrajectoryUtil.getAutos();

        // Keep track if we find an auto routine so if we don't we'll default to crossing the auto line
        boolean found = false;

        // Filter
        for (Integer i: chosen) {
            Autonomous auto = autos.get(i);
            /* If statement checks to see it the MatchData's "owned side"
            is the same as the Autonomous command's version of "owned side" */

            // Set feature to null to tell the bot to overwrite normal checks and run that specific auto
            if (auto.getFeature() == null) {
                System.out.println("Overwriting auto choosing");
                System.out.println("Added Auto " + i);
                addSequential(new MotionProfile(auto));
                found = true;
                break;
            }

            if (MatchData.getOwnedSide(auto.getFeature()) == auto.getSide()) {
                System.out.println("Added Auto " + i);
                addSequential(new MotionProfile(auto));
                found = true;
                break;
            }
        }

        // If we don't find a routine
        if (!found) {
            // Add path 100 (auto line)
            addSequential(new MotionProfile(autos.get(100)));
        }
    }
}
