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
public class MiddleStartingProfiles {
    private static final HashMap<Integer, Autonomous> autos = new HashMap<>();

    // Right Side Switch
    private static final Waypoint[] path_20 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 9, Math.toRadians(-50)),
            new Waypoint(10, 4, 0),
            new Waypoint(12, 4, 0),
            TrajectoryUtil.rightSideSwitch
    };
    private static final Autonomous auto_20 = new Autonomous(20, path_20, defaultConfig, SWITCH_NEAR, RIGHT);

    // Right Side Switch Wide
    private static final Waypoint[] path_21 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(4, 10, Math.toRadians(-90)),
            new Waypoint(2.5, 6.5, Math.toRadians(-90)),
            new Waypoint(10, 2, 0),
            TrajectoryUtil.rightSideSwitch
    };
    private static final Autonomous auto_21 = new Autonomous(21, path_21, defaultConfig, SWITCH_NEAR, RIGHT);

    // Right Front Switch
    private static final Waypoint[] path_22 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(4, 12, Math.toRadians(-50)),
            new Waypoint(5, 9, Math.toRadians(-40)),
            TrajectoryUtil.rightFrontSwitch
    };
    private static final Autonomous auto_22 = new Autonomous(22, path_22, defaultConfig, SWITCH_NEAR, RIGHT);

    // Left Side Switch
    private static final Waypoint[] path_23 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(7, 22, Math.toRadians(75)),
            new Waypoint(10.5, 24, 0),
            TrajectoryUtil.leftSideSwitch
    };
    private static final Autonomous auto_23 = new Autonomous(23, path_23, defaultConfig, SWITCH_NEAR, LEFT);

    // Left Side Switch
    private static final Waypoint[] path_24 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(4, 17, Math.toRadians(90)),
            new Waypoint(2.5, 20.5, Math.toRadians(90)),
            new Waypoint(10 ,25, 0),
            TrajectoryUtil.leftSideSwitch
    };
    private static final Autonomous auto_24 = new Autonomous(24, path_24, defaultConfig, SWITCH_NEAR, LEFT);

    // Left Front Switch
    private static final Waypoint[] path_25 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            TrajectoryUtil.leftFrontSwitch
    };
    private static final Autonomous auto_25 = new Autonomous(25, path_25, defaultConfig, SWITCH_NEAR, LEFT);

    // Right Side Scale
    private static final Waypoint[] path_26 = new Waypoint[]{
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 7, - Math.PI / 2),
            new Waypoint(10, 3, 0),
            new Waypoint(22.15, 3, 0),
            TrajectoryUtil.rightSideScale
    };
    private static final Autonomous auto_26 = new Autonomous(26, path_26, defaultConfig, SCALE, RIGHT);

    // Right Front Scale
    private static final Waypoint[] path_27 = new Waypoint[] {
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 7, Math.toRadians(-60)),
            new Waypoint(14, 4, 0),
            TrajectoryUtil.rightFrontScale
    };
    private static final Autonomous auto_27 = new Autonomous(27, path_27, defaultConfig, SCALE, RIGHT);

    // Left Side Scale
    private static final Waypoint[] path_28 = new Waypoint[] {
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 20, Math.PI / 2),
            new Waypoint(10, 24, 0),
            new Waypoint(22.15, 24, 0),
            TrajectoryUtil.leftSideScale
    };
    private static final Autonomous auto_28 = new Autonomous(28, path_28, defaultConfig, SCALE, LEFT);

    // Left Front Scale
    private static final Waypoint[] path_29 = new Waypoint[] {
            TrajectoryUtil.startMiddle,
            new Waypoint(6, 20, Math.toRadians(60)),
            new Waypoint(14, 23, 0),
            TrajectoryUtil.leftFrontScale
    };
    private static final Autonomous auto_29 = new Autonomous(29, path_29, defaultConfig, SCALE, LEFT);

    static {
        autos.put(20, auto_20);
        autos.put(21, auto_21);
        autos.put(22, auto_22);
        autos.put(23, auto_23);
        autos.put(24, auto_24);
        autos.put(25, auto_25);
        autos.put(26, auto_26);
        autos.put(27, auto_27);
        autos.put(28, auto_28);
        autos.put(29, auto_29);
    }

    /**
     * Getter for all autos stored in this class
     * @return HashMap of autos
     */
    public static HashMap<Integer, Autonomous> getAutos() {
        return autos;
    }
}
