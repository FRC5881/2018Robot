package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class RightStartingProfiles {
    // Right Side Switch
    public static final Waypoint[] path_10 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(4, 3, Math.toRadians(-45)),
            new Waypoint(8, 2, 0),
            TrajectoryUtil.rightSideSwitch
    };
    public static final Trajectory.Config config_10 = TrajectoryUtil.defaultConfig;

    // Right Front Switch
    public static final Waypoint[] path_11 = new Waypoint[] {
            TrajectoryUtil.startRight,
            TrajectoryUtil.rightFrontSwitch
    };
    public static final Trajectory.Config config_11 = TrajectoryUtil.defaultConfig;

    // Left Side Switch
    public static final Waypoint[] path_12 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(2, 4, 0),
            new Waypoint(10, 2.2, 0),
            new Waypoint(15, 2.2, 0),
            new Waypoint(19.65, 8, Math.PI / 2),
            new Waypoint(19.65, 17, Math.PI / 2),
            new Waypoint(19.8, 23, Math.toRadians(135)),
            new Waypoint(16.5, 23.75, Math.toRadians(200)),
            TrajectoryUtil.leftSideSwitch
    };
    public static final Trajectory.Config config_12 = TrajectoryUtil.defaultConfig;
}
