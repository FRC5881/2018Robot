package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Loops through each of our autonomous and prints how long they take
 */
public class GetTimes {
    public static void printTimes() {
        // Get HashMap of all the autos
        Map<Integer, Autonomous> autos = TrajectoryUtil.getAutos();

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
