package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles;

import jaci.pathfinder.Waypoint;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

import java.util.HashMap;

import static openrio.powerup.MatchData.GameFeature.*;
import static openrio.powerup.MatchData.OwnedSide.*;
import static org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil.defaultConfig;

/**
 * Static class storing autonomous routines starting in the left position
 */
public class RightStartingProfiles {
    private static HashMap<Integer, Autonomous> autos = new HashMap<>();

    // Right Side Switch
    private static final Waypoint[] path_10 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(4, 3, Math.toRadians(-45)),
            new Waypoint(8, 2, 0),
            TrajectoryUtil.rightSideSwitch
    };
    private static final Autonomous auto_10 = new Autonomous(path_10, defaultConfig, SWITCH_NEAR, RIGHT);

    // Right Front Switch
    private static final Waypoint[] path_11 = new Waypoint[] {
            TrajectoryUtil.startRight,
            TrajectoryUtil.rightFrontSwitch
    };
    private static final Autonomous auto_11 = new Autonomous(path_11, defaultConfig, SWITCH_NEAR, RIGHT);

    // Left Side Switch
    private static final Waypoint[] path_12 = new Waypoint[] {
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
    private static final Autonomous auto_12 = new Autonomous(path_12, defaultConfig, SWITCH_NEAR, LEFT);
    
    // Right Side Scale
    private static final Waypoint[] path_13 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(23.75, TrajectoryUtil.startRight.y, 0),
            TrajectoryUtil.rightSideScale
    };
    private static final Autonomous auto_13 = new Autonomous(path_13, defaultConfig, SCALE, RIGHT);

    // Right Front Scale
    private static final Waypoint[] path_14 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(14, TrajectoryUtil.startRight.y, 0),
            TrajectoryUtil.rightFrontScale
    };
    private static final Autonomous auto_14 = new Autonomous(path_14, defaultConfig, SCALE, RIGHT);

    // Left Side Scale
    private static final Waypoint[] path_15 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(14, TrajectoryUtil.startRight.y, 0),
            new Waypoint(19, 6.56, Math.toRadians(60)),
            new Waypoint(19.33, 9, Math.PI / 2),
            new Waypoint(19.33, 21, Math.PI / 2),
            new Waypoint(23, 24, 0),
            TrajectoryUtil.leftSideScale
    };
    private static final Autonomous auto_15 = new Autonomous(path_15, defaultConfig, SCALE, LEFT);

    // Left Front Scale
    private static final Waypoint[] path_16 = new Waypoint[] {
            TrajectoryUtil.startRight,
            new Waypoint(14, 2, 0),
            new Waypoint(19.75, 6, 0),
            new Waypoint(19.75, 16.75, Math.PI / 2),
            new Waypoint(20.75, 16.5, Math.PI / 4),
            TrajectoryUtil.leftFrontScale
    };
    private static final Autonomous auto_16 = new Autonomous(path_16, defaultConfig, SCALE, LEFT);

    static {
        autos.put(10, auto_10);
        autos.put(11, auto_11);
        autos.put(12, auto_12);
        autos.put(13, auto_13);
        autos.put(14, auto_14);
        autos.put(15, auto_15);
        //autos.put(16, auto_16);
    }

    /**
     * Getter for all autos stored in this class
     * @return HashMap of autos
     */
    public static HashMap<Integer, Autonomous> getAutos() {
        return autos;
    }
}
