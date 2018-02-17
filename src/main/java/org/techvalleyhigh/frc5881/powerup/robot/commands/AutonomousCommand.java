package org.techvalleyhigh.frc5881.powerup.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import openrio.powerup.MatchData;
import org.techvalleyhigh.frc5881.powerup.robot.utils.AutonomousDecoder;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.LeftStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.MiddleStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.RightStartingProfiles;

import java.util.ArrayList;
import java.util.HashMap;

public class AutonomousCommand extends CommandGroup {
    @Override
    protected boolean isFinished() {
        return false;
    }
    // public static HashMap<Integer, Autonomous> autos;

    /**
     *
     * @param routine input string "1,2,4-6"
     */
    public AutonomousCommand(String routine) {
        HashMap<Integer, Autonomous> autos = new HashMap<>();
        autos.putAll(LeftStartingProfiles.getAutos());
        autos.putAll(MiddleStartingProfiles.getAutos());
        autos.putAll(RightStartingProfiles.getAutos());

        HashMap<Integer, Autonomous> autos = new HashMap<>();
        autos.putAll(LeftStartingProfiles.getAutos());
        autos.putAll(MiddleStartingProfiles.getAutos());
        autos.putAll(RightStartingProfiles.getAutos());
        // Pass through decoder
        ArrayList<Integer> chosen = AutonomousDecoder.getIntRanges(routine);
        // Filter
        for (Integer i: chosen) {
            Autonomous auto = autos.get(i);
            //If statement checks to see it the MatchData's "owned side" is the same as the Autonomous command's version of "owned side"
            if (MatchData.getOwnedSide(auto.getFeature()) == auto.getSide()) {
                addSequential(new MotionProfile(auto));
            }

        }
        if (!routine.equals("None")) {
            // TODO: auto commands
            if (routine.equals("Figure Eight")) {
                addSequential(new MotionProfile(autos.get(1)));
            }
        }
    }
}
