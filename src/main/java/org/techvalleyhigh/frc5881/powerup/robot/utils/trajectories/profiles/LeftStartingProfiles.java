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
public class LeftStartingProfiles {
    private static HashMap<Integer, Autonomous> autos = new HashMap<>();
    
    // Left Side Switch
    private static final Waypoint[] path_0 = new Waypoint[] {
            TrajectoryUtil.startLeft, // Starting point
            new Waypoint(13.5, 23, 0), // Middle Point
            TrajectoryUtil.leftSideSwitch // Ending Point
    };
    private static final Autonomous auto_0 = new Autonomous(path_0, defaultConfig, SWITCH_NEAR, LEFT); // Autonomous Object
    
    // Left Front Switch
    private static final Waypoint[] path_1 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(8,18.25, 0),
            TrajectoryUtil.leftFrontSwitch
    };
    private static final Autonomous auto_1 = new Autonomous(path_1, defaultConfig, SWITCH_NEAR, LEFT);
    
    // Right Side Switch
    private static final Waypoint[] path_2 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(5, 11, Math.toRadians(-90)),
            TrajectoryUtil.rightSideSwitch
    };
    private static final Autonomous auto_2 = new Autonomous(path_2, defaultConfig, SWITCH_NEAR, RIGHT);
    
    // Left Side Scale
    private static final Waypoint[] path_3 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(13.5, 24.5, 0),
            new Waypoint(22, 24.5, 0),
            TrajectoryUtil.leftSideScale
    };
    private static final Autonomous auto_3 = new Autonomous(path_3, defaultConfig, SCALE, LEFT);
    
    // Left Front Scale
    private static final Waypoint[] path_4 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(16, 25, 0),
            new Waypoint(21, 21, Math.toRadians(-45)),
            TrajectoryUtil.leftFrontScale
    };
    private static final Autonomous auto_4 = new Autonomous(path_4, defaultConfig, SCALE, LEFT);
    
    // Right Side Scale
    private static final Waypoint[] path_5 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(2, 23, 0),
            new Waypoint(10,24.8, 0),
            new Waypoint(15, 24.8, 0),
            new Waypoint(19.65, 19, Math.toRadians(-90)),
            new Waypoint(19.65, 10, Math.toRadians(-90)),
            new Waypoint(19.8, 4, Math.toRadians(-135)),
            new Waypoint(16.5, 3.25, Math.toRadians(-200)),
            TrajectoryUtil.rightSideScale
    };
    private static final Autonomous auto_5 = new Autonomous(path_5, defaultConfig, SCALE, RIGHT);
    
    // Right Front Scale
    private static final Waypoint[] path_6 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(14.5, 23, 0),
            new Waypoint(19.25, 20.5, Math.toRadians(-68)),
            new Waypoint(19.5, 10, Math.toRadians(-90)),
            new Waypoint(22.25, 6.5, 0),
            TrajectoryUtil.rightFrontScale
    };
    private static final Autonomous auto_6 = new Autonomous(path_6, TrajectoryUtil.ourConfig(4.25, 4), SCALE, RIGHT);
    
    static {
        autos.put(0, auto_0);
        autos.put(1, auto_1);
        autos.put(2, auto_2);
        autos.put(3, auto_3);
        autos.put(4, auto_4);
        autos.put(5, auto_5);
        autos.put(6, auto_6);
    }

    public static HashMap<Integer, Autonomous> getAutos() {
        return autos;
    }
}
