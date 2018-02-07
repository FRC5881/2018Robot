package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles;

import jaci.pathfinder.Waypoint;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.ProfileGroup;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

import java.util.HashMap;

import static org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil.AutoTarget.*;
import static org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil.defaultConfig;

public class MiddleStartingProfiles extends ProfileGroup {
    //Right Side Switch
    public static final Waypoint[] path_20 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 9, Math.toRadians(-50)),
            new Waypoint(10, 4, 0),
            new Waypoint(12, 4, 0),
            TrajectoryUtil.rightSideSwitch
    };
    public static final Autonomous auto_20 = new Autonomous(path_20, defaultConfig, SWITCH_RIGHT);
    //Right Side Switch Wide
    public static final Waypoint[] path_21 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(4, 10, Math.toRadians(-90)),
            new Waypoint(2.5, 6.5, Math.toRadians(-90)),
            new Waypoint(10, 2, 0),
            TrajectoryUtil.rightSideSwitch
    };
    public static final Autonomous auto_21 = new Autonomous(path_21, defaultConfig, SWITCH_RIGHT);
    //Right Front Switch
    public static final Waypoint[] path_22 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(4, 12, Math.toRadians(-50)),
            new Waypoint(5, 9, Math.toRadians(-40)),
            TrajectoryUtil.rightFrontSwitch
    };
    public static final Autonomous auto_22 = new Autonomous(path_22, defaultConfig, SWITCH_RIGHT);
    //Left Side Switch
    public static final Waypoint[] path_23 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(7, 22, Math.toRadians(75)),
            new Waypoint(10.5, 24, 0),
            TrajectoryUtil.leftSideSwitch
    };
    public static final Autonomous auto_23 = new Autonomous(path_23, defaultConfig, SWITCH_LEFT);
    //Left Side Switch
    public static final Waypoint[] path_24 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(4, 17, Math.toRadians(90)),
            new Waypoint(2.5, 20.5, Math.toRadians(90)),
            new Waypoint(10 ,25, 0),
            TrajectoryUtil.leftSideSwitch
    };
    public static final Autonomous auto_24 = new Autonomous(path_24, defaultConfig, SWITCH_LEFT);
    //Left Front Switch
    public static final Waypoint[] path_25 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            TrajectoryUtil.leftFrontSwitch
    };
    public static final Autonomous auto_25 = new Autonomous(path_25, defaultConfig, SWITCH_LEFT);
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
