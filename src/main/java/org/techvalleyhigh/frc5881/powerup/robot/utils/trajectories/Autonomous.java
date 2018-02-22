package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import openrio.powerup.MatchData;

/**
 * Read only class stores all the useful data when it comes to storing an autonomous profile
 */
public class Autonomous {
    private Waypoint[] path;
    private Trajectory.Config config;
    private MatchData.GameFeature feature;
    private MatchData.OwnedSide side;

    /**
     * Stores a path, config and ending target to private variables
     * @param path array of waypoints describing a auto profile
     * @param config Trajectory.Config configs to be used while creating auto profile
     * @param feature Feature autonomous profile targets
     * @param side
     */
    public Autonomous(Waypoint[] path, Trajectory.Config config, MatchData.GameFeature feature, MatchData.OwnedSide side) {
        this.path = path;
        this.config = config;
        this.feature = feature;
        this.side = side;
    }

    public Waypoint[] getPath() {
        return path;
    }

    public Trajectory.Config getConfig() {
        return config;
    }

    public MatchData.GameFeature getFeature() {
        return feature;
    }

    public MatchData.OwnedSide getSide() {
        return side;
    }
}
