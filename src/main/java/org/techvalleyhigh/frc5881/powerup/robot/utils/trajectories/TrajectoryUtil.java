package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.LeftStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.MiddleStartingProfiles;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.Misc;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles.RightStartingProfiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class holding all kinds of useful data to streamline working with autonomous ProfileMode
 */
public class TrajectoryUtil {
    // Starting locations
    public static final Waypoint startRight = new Waypoint(1.36333333, 3.9583, 0);
    public static final Waypoint startMiddle = new Waypoint(1.36333333, 13.0416, 0);
    public static final Waypoint startLeft = new Waypoint(1.36333333, 23.041, 0);

    // Ending locations
    public static final Waypoint leftSideSwitch = new Waypoint(14, 21.37496667, -Math.PI / 2);
    public static final Waypoint leftFrontSwitch = new Waypoint(10.20836667, 18.5, 0);
    public static final Waypoint leftSideScale = new Waypoint(27, 21.35413333, -Math.PI / 2);
    public static final Waypoint leftFrontScale = new Waypoint(23.54953333, 20, 0);
    public static final Waypoint rightSideSwitch = new Waypoint(14, 5.625033333, Math.PI / 2);
    public static final Waypoint rightFrontSwitch = new Waypoint(10.20836667, 8.5, 0);
    public static final Waypoint rightSideScale = new Waypoint(27, 5.645866667, Math.PI / 2);
    public static final Waypoint rightFrontScale = new Waypoint(23.54953333, 7, 0);

    // Configs
    public static final Trajectory.Config defaultConfig = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
            Trajectory.Config.SAMPLES_HIGH, 0.02, 2.5, 1, 60);

    /**
     * Set of waypoints that together generate a figure eight for testing
     */
    public static final Waypoint[] testFigureEight = new Waypoint[] {
            new Waypoint(10, 5, Math.toRadians(0)),
            new Waypoint(15, 10, Math.toRadians(90)),
            new Waypoint(10, 15, Math.toRadians(135)),
            new Waypoint(5, 20, Math.toRadians(90)),
            new Waypoint(10, 25, Math.toRadians(0)),
            new Waypoint(15, 20, Math.toRadians(270)),
            new Waypoint(10, 15, Math.toRadians(-135)),
            new Waypoint(5, 10, Math.toRadians(-90)),
            new Waypoint(10, 5, Math.toRadians(0))
    };

    /**
     * Reflects inputs across the line y = 13.5 and negate angles
     * Since the field is nicely symmetrical this allows us to flip auto paths from the left side to the right side
     * @param input input waypoints
     * @return Array of waypoints reflected over center line of the field (long ways)
     */
    public static Waypoint[] mirror(Waypoint[] input) {
        Waypoint[] clone = input.clone();
        Waypoint[] ret = new Waypoint[clone.length];

        for (int i = 0; i < clone.length; i++) {

            // Reflect over y = 13.5
            double y = 27d - clone[i].y;

            // Negate the angle
            double angle = -clone[i].angle;

            ret[i] = new Waypoint(clone[i].x, y, angle);
        }

        return ret;
    }

    /**
     * Takes in the only 2 parameters we'll likely ever change using Trajectory.Config velocity and acceleration
     * And returns Trajectory.Config with everything else defaults
     * @param max_velocity max velocity to generate trajectories can drive
     * @param max_acceleration max acceleration the bot should ever experience
     * @return {Trajectory.Config} our default trajectory with edited velocity and acceleration
     */
    public static Trajectory.Config ourConfig(double max_velocity, double max_acceleration) {
        return new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH,
                0.05, max_velocity, max_acceleration, 60);
    }

    /**
     * Returns array of waypoints that paths out a figure eight fits tightly inside a rectangle
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @return Waypoint[] of generated figure eight
     */
    public static Waypoint[] customFigureEight(double width, double height) {
        return new Waypoint[] {
                new Waypoint(width / 2, 0, 0),
                new Waypoint(width, height / 4, Math.PI / 4),
                new Waypoint(width / 2, height / 2, Math.toRadians(135)),
                new Waypoint(0, height / 2, Math.PI / 4),
                new Waypoint(width / 2, height, 0),
                new Waypoint(width, 3 * width / 4, -Math.PI / 4),
                new Waypoint(width / 2, height / 2, Math.toRadians(-135)),
                new Waypoint(0, height / 4, -Math.PI / 4)
        };
    }

    /**
     * Gets a Map of all current stored Autonomous routines by their Integer Id.
     * @return Key-Value map of Integers and Autonomous routines.
     */
    public static Map<Integer, Autonomous> getAutos() {
        // Get HashMap of all the autos
        HashMap<Integer, Autonomous> autos = new HashMap<>();

        autos.putAll(LeftStartingProfiles.getAutos());
        autos.putAll(MiddleStartingProfiles.getAutos());
        autos.putAll(RightStartingProfiles.getAutos());
        autos.putAll(Misc.getAutos());

        return autos;
    }

    /**
     * Loads a stored pre-computed Autonomous trajectory for the given Autonomous Id.
     * @param autoNumber Autonomous Id number to load
     * @return The stored Trajectory or Null if an error occurred
     */
    public static Trajectory loadTrajectoryFromFile(int autoNumber) {
        try {
            //InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("auto_" + autoNumber + ".txt");
            InputStream in = TrajectoryUtil.class.getResourceAsStream("/auto_" + autoNumber + ".csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            // Load Config
            String configLine = reader.readLine();
            Trajectory.Config config = getconfigFromLine(configLine);

            // Load WayPoints
            ArrayList<Waypoint> waypoints = new ArrayList<>();

            ArrayList<Trajectory.Segment> segments = new ArrayList<>();

            boolean waypointsDone = false;

            String line;
            while ((line = reader.readLine()) != null) {
                if (!waypointsDone) {
                    if (line.equals("---")) {
                        waypointsDone = true;
                    } else {
                        waypoints.add(getWaypointFromLine(line));
                    }
                } else {
                    segments.add(getSegmentFromLine(line));
                }
            }

            return isStoredTrajectoryValid(autoNumber, config, waypoints.toArray(new Waypoint[waypoints.size()]))
                    ? new Trajectory(segments.toArray(new Trajectory.Segment[segments.size()])) : null;
        } catch (IOException e) {
            System.err.println("IO Exception Loading Stored Auto " + autoNumber);
            e.printStackTrace();
        }

        return null;
    }

    private static Trajectory.Config getconfigFromLine(String line) {
        String[] fields = line.split(",");
        return new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH,
                Double.parseDouble(fields[0]), Double.parseDouble(fields[1]), Double.parseDouble(fields[2]),
                Double.parseDouble(fields[3]));
    }

    private static Waypoint getWaypointFromLine(String line) {
        String[] fields = line.split(",");
        return new Waypoint(Double.parseDouble(fields[0]), Double.parseDouble(fields[1]),
                Double.parseDouble(fields[2]));
    }

    private static Trajectory.Segment getSegmentFromLine(String line) {
        String[] fields = line.split(",");
        return new Trajectory.Segment(Double.parseDouble(fields[0]), Double.parseDouble(fields[1]),
                Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4])
                , Double.parseDouble(fields[5]), Double.parseDouble(fields[6]), Double.parseDouble(fields[7]));
    }

    private static boolean isStoredTrajectoryValid(int autoNumber, Trajectory.Config config, Waypoint[] waypoints) {
        Map<Integer, Autonomous> autos = getAutos();

        Autonomous auto = autos.get(autoNumber);

        if (auto == null) {
            System.err.println("Stored autonomous " + autoNumber + " not found in code.");
            return false;
        }

        if (Double.compare(auto.getConfig().dt, config.dt) != 0
                || Double.compare(auto.getConfig().max_acceleration, config.max_acceleration) != 0
                || Double.compare(auto.getConfig().max_jerk, config.max_jerk) != 0
                || Double.compare(auto.getConfig().max_velocity, config.max_velocity) != 0) {
            System.err.println("Stored autonomous " + autoNumber + " has a different configuration than in code.");
            return false;
        }

        if (auto.getPath().length != waypoints.length) {
            System.err.println("Stored autonomous " + autoNumber
                    + " has a different number of waypoints than in code.");
            return false;
        }

        for (int i = 0; i < waypoints.length; i++) {
            if (Double.compare(auto.getPath()[i].x, waypoints[i].x) != 0
                    || Double.compare(auto.getPath()[i].y, waypoints[i].y) != 0
                    || Double.compare(auto.getPath()[i].angle, waypoints[i].angle) != 0) {
                System.err.println("Stored autonomous " + autoNumber
                        + " has a different set of waypoints than in code.");
                return false;
            }
        }

        return true;
    }
}
