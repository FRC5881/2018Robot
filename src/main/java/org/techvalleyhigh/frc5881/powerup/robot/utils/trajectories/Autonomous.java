package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import openrio.powerup.MatchData;

/**
 * Read only class stores all the useful data when it comes to storing an autonomous profile
 */
public class Autonomous {
    private final Waypoint[] path;
    private final Trajectory.Config config;
    private final MatchData.GameFeature feature;
    private final MatchData.OwnedSide side;

    /**
     * Stores a path, config and ending target to private variables
     * @param path array of waypoints describing a auto profile
     * @param config Trajectory.Config configs to be used while creating auto profile
     * @param feature Feature autonomous profile targets
     * @param side Side of feature we'd be scoring on
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
