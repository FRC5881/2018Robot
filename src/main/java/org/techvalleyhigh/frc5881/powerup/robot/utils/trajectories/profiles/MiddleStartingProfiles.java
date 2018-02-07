package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles;

import jaci.pathfinder.Waypoint;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.ProfileGroup;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

import java.util.HashMap;

import static org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil.AutoTarget.*;
import static org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil.defaultConfig;

public class MiddleStartingProfiles extends ProfileGroup {
    public static HashMap<Integer, Autonomous> autos = new HashMap<>();

    // Right Side Scale
    public static final Waypoint[] path_26 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 7, - Math.PI / 2),
            new Waypoint(10, 3, 0),
            new Waypoint(22.15, 3, 0),
            TrajectoryUtil.rightSideScale
    };
    public static final Autonomous auto_26 = new Autonomous(path_26, defaultConfig, SCALE_RIGHT);

    // Right Front Scale
    public static final Waypoint[] path_27 = new Waypoint[] {
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 7, Math.toRadians(-60)),
            new Waypoint(14, 4, 0),
            TrajectoryUtil.rightFrontScale
    };
    public static final Autonomous auto_27 = new Autonomous(path_27, defaultConfig, SCALE_RIGHT);

    // Left Side Scale
    public static final Waypoint[] path_28 = new Waypoint[] {
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 20, Math.PI / 2),
            new Waypoint(10, 24, 0),
            new Waypoint(22.15, 24, 0),
            TrajectoryUtil.leftSideScale
    };
    public static final Autonomous auto_28 = new Autonomous(path_28, defaultConfig, SCALE_LEFT);

    // Left Front Scale
    public static final Waypoint[] path_29 = new Waypoint[] {
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 20, Math.toRadians(60)),
            new Waypoint(14, 23, 0),
            TrajectoryUtil.leftFrontScale
    };
    public static final Autonomous auto_29 = new Autonomous(path_29, defaultConfig, SCALE_LEFT);
}
