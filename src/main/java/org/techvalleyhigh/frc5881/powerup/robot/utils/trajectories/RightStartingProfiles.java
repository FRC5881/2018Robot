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

    // Right Side Scale
    public static final Waypoint[] path_13 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(23.75, TrajectoryUtil.startRight.y, 0),
            TrajectoryUtil.rightFrontScale
    };
    public static final Trajectory.Config config_13 = TrajectoryUtil.defaultConfig;

    // Right Front Scale
    public static final Waypoint[] path_14 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(14, TrajectoryUtil.startRight.y, 0),
            TrajectoryUtil.rightFrontScale
    };
    public static final Trajectory.Config config_14 = TrajectoryUtil.defaultConfig;

    // Left Side Scale
    public static final Waypoint[] path_15 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(14, TrajectoryUtil.startRight.y, 0),
            new Waypoint(19, 6.56, Math.toRadians(60)),
            new Waypoint(19.33, 9, Math.PI / 2),
            new Waypoint(19.33, 21, Math.PI / 2),
            new Waypoint(23, 24, 0),
            TrajectoryUtil.leftSideScale
    };
    public static final Trajectory.Config config_15 = TrajectoryUtil.defaultConfig;

    // Left Front Scale
    public static final Waypoint[] path_16 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(14, 2, 0),
            new Waypoint(19.75, 6, 0),
            new Waypoint(19.75, 16.75, Math.PI / 2),
            new Waypoint(20.75, 16.5, Math.PI / 4),
            TrajectoryUtil.leftFrontScale
    };
    public static final Trajectory.Config config_16 = TrajectoryUtil.defaultConfig;
}
