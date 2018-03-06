package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles;

import jaci.pathfinder.Waypoint;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;

import java.util.HashMap;

import static openrio.powerup.MatchData.GameFeature.*;
import static openrio.powerup.MatchData.OwnedSide.*;
import static org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil.defaultConfig;

public class Misc {
    private static HashMap<Integer, Autonomous> autos = new HashMap<>();

    public static Waypoint[] path_baseline = new Waypoint[] {
            new Waypoint(0, 0, 0),
            new Waypoint(10, 0 ,0)
    };
    public static Autonomous baseline = new Autonomous(path_baseline, defaultConfig, null, null);

    public static Waypoint[] path_straightline = new Waypoint[] {
            new Waypoint(0, 0, 0),
            new Waypoint(25, 0, 0)
    };
    public static Autonomous straightline = new Autonomous(path_straightline, defaultConfig, null, null);

    static {
        autos.put(100, baseline);
        autos.put(101, straightline);
    }

    /**
     * Getter for all autos stored in this class
     * @return HashMap of autos
     */
    public static HashMap<Integer, Autonomous> getAutos() {
        return autos;
    }
}
