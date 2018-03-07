package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.LeftStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.MiddleStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.Misc;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.RightStartingProfiles;

import java.util.HashMap;

/**
 * Loops through each of our autonomous and prints how long they take
 */
public class GetTimes {
    public static void printTimes() {
        // Get HashMap of all the autos
        HashMap<Integer, Autonomous> autos = new HashMap<>();

        autos.putAll(LeftStartingProfiles.getAutos());
        autos.putAll(MiddleStartingProfiles.getAutos());
        autos.putAll(RightStartingProfiles.getAutos());
        autos.putAll(Misc.getAutos());

        // Loop through each entry
        for (HashMap.Entry<Integer, Autonomous> entry: autos.entrySet()) {
            Waypoint[] path = entry.getValue().getPath();
            Trajectory.Config config = entry.getValue().getConfig();

            // Generate the trajectory
            Trajectory trajectory = Pathfinder.generate(path, config);

            System.out.println("Auto " + entry.getKey() + " Time: " + trajectory.length() * config.dt);
        }
    }
}
