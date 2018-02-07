package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles;

import jaci.pathfinder.Waypoint;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.ProfileGroup;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

import java.util.HashMap;

import static org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil.AutoTarget.*;
import static org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil.defaultConfig;

public class LeftStartingProfiles extends ProfileGroup {
    public static final Waypoint[] path_0 = new Waypoint[] {
            TrajectoryUtil.startLeft, // Starting point
            new Waypoint(13.5, 23, Math.toRadians(0)), // Middle Point
            TrajectoryUtil.leftSideSwitch // Ending Point
    };
    public static final Autonomous auto_0 = new Autonomous(path_0, defaultConfig, SWITCH_LEFT); // Autonomous Object

    public static final Waypoint[] path_1 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(8,18.25, Math.toRadians(0)),
            TrajectoryUtil.leftFrontSwitch
    };
    public static final Autonomous auto_1 = new Autonomous(path_1, defaultConfig, SWITCH_LEFT);

    public static final Waypoint[] path_2 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(5, 11, Math.toRadians(-90)),
            TrajectoryUtil.rightSideSwitch
    };
    public static final Autonomous auto_2 = new Autonomous(path_2, defaultConfig, SWITCH_RIGHT);

    public static final Waypoint[] path_3 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(13.5, 24.5, Math.toRadians(0)),
            new Waypoint(22, 24.5, Math.toRadians(0)),
            TrajectoryUtil.leftSideScale
    };
    public static final Autonomous auto_3 = new Autonomous(path_3, defaultConfig, SCALE_LEFT);

    public static final Waypoint[] path_4 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(16, 25, Math.toRadians(0)),
            new Waypoint(21, 21, Math.toRadians(-45)),
            TrajectoryUtil.leftFrontScale
    };
    public static final Autonomous auto_4 = new Autonomous(path_4, defaultConfig, SCALE_LEFT);

    public static final Waypoint[] path_5 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(2, 23, Math.toRadians(0)),
            new Waypoint(10,24.8, Math.toRadians(0)),
            new Waypoint(15, 24.8, Math.toRadians(0)),
            new Waypoint(19.65, 19, Math.toRadians(-90)),
            new Waypoint(19.65, 10, Math.toRadians(-90)),
            new Waypoint(19.8, 4, Math.toRadians(-135)),
            new Waypoint(16.5, 3.25, Math.toRadians(-200)),
            TrajectoryUtil.rightSideScale
    };
    public static final Autonomous auto_5 = new Autonomous(path_5, defaultConfig, SCALE_LEFT);
    //TODO: mess around with velocity\/
    public static final Waypoint[] path_6 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(14.5, 23, Math.toRadians(0)),
            new Waypoint(19.25, 20.5, Math.toRadians(-68)),
            new Waypoint(19.5, 10, Math.toRadians(-90)),
            new Waypoint(22.25, 6.5, Math.toRadians(0)),
            TrajectoryUtil.rightFrontScale
    };
    public static final Autonomous auto_6 = new Autonomous(path_6, defaultConfig, SCALE_RIGHT);
}
