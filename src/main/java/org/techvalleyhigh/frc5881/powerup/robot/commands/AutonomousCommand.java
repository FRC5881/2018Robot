package org.techvalleyhigh.frc5881.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import openrio.powerup.MatchData;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.LeftStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.MiddleStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.Misc;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.RightStartingProfiles;

import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<Integer, Autonomous> autos = new HashMap<>();
        autos.putAll(LeftStartingProfiles.getAutos());
        autos.putAll(MiddleStartingProfiles.getAutos());
        autos.putAll(RightStartingProfiles.getAutos());
        autos.putAll(Misc.getAutos());

        // Filter
        for (Integer i: chosen) {
            Autonomous auto = autos.get(i);
            /*
            If statement checks to see it the MatchData's "owned side"
            is the same as the Autonomous command's version of "owned side"
             */
            if (auto.getFeature() == null) {
                System.out.println("Overwriting auto choosing");
                System.out.println("Added Auto " + i);
                addSequential(new MotionProfile(auto));
                break;
            }

            if (MatchData.getOwnedSide(auto.getFeature()) == auto.getSide()) {
                System.out.println("Added Auto " + i);
                addSequential(new MotionProfile(auto));
                break;
            }
        }
    }
}
