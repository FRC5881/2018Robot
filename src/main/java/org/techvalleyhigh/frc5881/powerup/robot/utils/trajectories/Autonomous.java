package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import openrio.powerup.MatchData;

/**
 * Simple class stores all the useful data when it comes to storing an autonomous profile
 */
public class Autonomous {
    private final int autoNumber;
    private final Waypoint[] path;
    private final Trajectory.Config config;
    private final MatchData.GameFeature feature;
    private final MatchData.OwnedSide side;
    private final long timeToWait;

    /**
     * Stores a path, config and ending target to private variables
     * @param autoNumber the integer ID of the autonomous routine
     * @param path array of waypoints describing a auto profile
     * @param config Trajectory.Config configs to be used while creating auto profile
     * @param feature Feature autonomous profile targets
     * @param side Side of feature we'd be scoring on
     * @param timeToWait milliseconds to wait before sending the elevator and arm to position to score
     */
    public Autonomous(int autoNumber, Waypoint[] path, Trajectory.Config config, MatchData.GameFeature feature, MatchData.OwnedSide side, long timeToWait) {
        this.autoNumber = autoNumber;
        this.path = path;
        this.config = config;
        this.feature = feature;
        this.side = side;
        this.timeToWait = timeToWait;
    }

    /**
     * Stores a path, config and ending target to private variables, defaults timeToWait to 0
     * @param autoNumber the integer ID of the autonomous routine
     * @param path array of waypoints describing a auto profile
     * @param config Trajectory.Config configs to be used while creating auto profile
     * @param feature Feature autonomous profile targets
     * @param side Side of feature we'd be scoring on
     */
    public Autonomous(int autoNumber, Waypoint[] path, Trajectory.Config config, MatchData.GameFeature feature, MatchData.OwnedSide side) {
        this.autoNumber = autoNumber;
        this.path = path;
        this.config = config;
        this.feature = feature;
        this.side = side;
        this.timeToWait = 0;
    }

    public int getAutoNumber() {
        return autoNumber;
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

    public long getTimeToWait() {
        return timeToWait;
    }
}
