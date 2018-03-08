package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Generates and exports all current Autonomous Routine Trajectories to an
 * external file.
 */
public class TrajectoryExport {
    public static void main(String[] args) {
        System.out.println("Exporting Trajectory Paths to: " + getExportDir());

        Map<Integer, Autonomous> autos = TrajectoryUtil.getAutos();

        for(Integer key : autos.keySet()) {
            Autonomous auto = autos.get(key);

            System.out.println("Generating Trajectory for Auto " + key);

            Trajectory trajectory = Pathfinder.generate(auto.getPath(), auto.getConfig());

            exportTrajectory(trajectory, auto.getPath(), auto.getConfig(), key);

            System.out.println("");
        }
    }

    private static String getExportDir() {
        return System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                + File.separator + "resources" + File.separator;
    }

    private static void exportTrajectory(Trajectory trajectory, Waypoint[] path, Trajectory.Config config,
                                         int autoNumber) {
        try {
            File export = new File(getExportDir() + "auto_" + autoNumber + ".csv");
            PrintWriter printWriter = new PrintWriter(export);

            // Start with config values:
            printWriter.println("" + config.dt + "," + config.max_velocity + "," + config.max_acceleration
                    + "," + config.max_jerk);

            // Dump waypoints
            for(Waypoint waypoint : path) {
                printWriter.println("" + waypoint.x + "," + waypoint.y + "," + waypoint.angle);
            }

            // Add a separator to denote start of trajectory data
            printWriter.println("---");

            for(Trajectory.Segment segment : trajectory.segments) {
                printWriter.println("" + segment.dt + "," + segment.x + "," + segment.y + "," + segment.position
                        + "," + segment.velocity + "," + segment.acceleration + "," + segment.jerk
                        + "," + segment.heading);
            }

            printWriter.close();

            System.out.println("Auto " + autoNumber + " exported successfully.");
        } catch (FileNotFoundException e) {
            System.err.println("Cannot output auto " + autoNumber);
            e.printStackTrace();
        }
    }
}
