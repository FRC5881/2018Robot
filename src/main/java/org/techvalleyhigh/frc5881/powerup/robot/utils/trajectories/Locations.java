package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class Locations {
    // Starting Locations
    public static final Waypoint startRight = new Waypoint(1.36333333, 3.9583, 0);
    public static final Waypoint startMiddle = new Waypoint(1.36333333, 13.0416, 0);
    public static final Waypoint startLeft = new Waypoint(1.36333333, 23.041, 0);

    // Ending Locations
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
            Trajectory.Config.SAMPLES_HIGH, 0.05, 5, 4, 60);
}
