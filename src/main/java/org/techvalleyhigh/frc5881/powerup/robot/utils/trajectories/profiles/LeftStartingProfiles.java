package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles;

import jaci.pathfinder.Waypoint;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

import java.util.HashMap;

import static openrio.powerup.MatchData.GameFeature.*;
import static openrio.powerup.MatchData.OwnedSide.*;
import static org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil.defaultConfig;

/**
 * Class full of static variables storing autonomous routines starting in the left position
 */
public class LeftStartingProfiles {
    private static final HashMap<Integer, Autonomous> autos = new HashMap<>();
    
    // Left Side Switch
    private static final Waypoint[] path_0 = new Waypoint[] {
            TrajectoryUtil.startLeft, // Starting point
            new Waypoint(10, 24, 0), // Middle Point
            TrajectoryUtil.leftSideSwitch // Ending Point
    };
    private static final Autonomous auto_0 = new Autonomous(0,path_0, defaultConfig, SWITCH_NEAR, LEFT); // Autonomous Object
    
    // Left Front Switch
    private static final Waypoint[] path_1 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(8,18.25, 0),
            TrajectoryUtil.leftFrontSwitch
    };
    private static final Autonomous auto_1 = new Autonomous(1, path_1, defaultConfig, SWITCH_NEAR, LEFT);
    
    // Right Side Switch
    private static final Waypoint[] path_2 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(16, 24.04, 0),
            new Waypoint(19, 19, - Math.PI / 2),
            new Waypoint(19, 7, - Math.PI / 2),
            new Waypoint(17, 4, - Math.PI),
            TrajectoryUtil.rightSideSwitch
    };
    private static final Autonomous auto_2 = new Autonomous(2, path_2, defaultConfig, SWITCH_NEAR, RIGHT);
    
    // Left Side Scale
    private static final Waypoint[] path_3 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(13.5, 24.5, 0),
            new Waypoint(22, 24.5, 0),
            TrajectoryUtil.leftSideScale
    };
    private static final Autonomous auto_3 = new Autonomous(3, path_3, defaultConfig, SCALE, LEFT);
    
    // Left Front Scale
    private static final Waypoint[] path_4 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(16, 25, 0),
            new Waypoint(21, 21, Math.toRadians(-45)),
            TrajectoryUtil.leftFrontScale
    };
    private static final Autonomous auto_4 = new Autonomous(4, path_4, defaultConfig, SCALE, LEFT);
    
    // Right Side Scale
    private static final Waypoint[] path_5 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(5, 23.04, 0),
            new Waypoint(10,23.04, 0),
            new Waypoint(15, 23.04, 0),
            new Waypoint(20.25, 19, Math.toRadians(-90)),
            new Waypoint(20.25, 8, Math.toRadians(-90)),
            new Waypoint(20.25, 4, Math.toRadians(-45)),
            new Waypoint(23.25, 3.25, 0),
            TrajectoryUtil.rightSideScale
    };
    private static final Autonomous auto_5 = new Autonomous(5, path_5, defaultConfig, SCALE, RIGHT);
    
    // Right Front Scale
    public static final Waypoint[] path_6 = new Waypoint[]{
            TrajectoryUtil.startLeft,
            new Waypoint(14.5, 23, 0),
            new Waypoint(19.25, 20.5, Math.toRadians(-68)),
            new Waypoint(19.5, 10, Math.toRadians(-90)),
            new Waypoint(22.25, 7.3, Math.toRadians(-29)),
            TrajectoryUtil.rightFrontScale
    };
    private static final Autonomous auto_6 = new Autonomous(6, path_6, TrajectoryUtil.ourConfig(4.25, 4), SCALE, RIGHT);
    
    static {
        autos.put(0, auto_0);
        autos.put(1, auto_1);
        autos.put(2, auto_2);
        autos.put(3, auto_3);
        autos.put(4, auto_4);
        autos.put(5, auto_5);
        autos.put(6, auto_6);
    }

    /**
     * Getter for all autos stored in this class
     * @return HashMap of autos
     */
    public static HashMap<Integer, Autonomous> getAutos() {
        return autos;
    }
}
