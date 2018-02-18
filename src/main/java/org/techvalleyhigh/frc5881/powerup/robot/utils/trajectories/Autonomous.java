package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

/**
 * Read only class stores all the useful data when it comes to storing an autonomous profile
 */
public class Autonomous {
    private Waypoint[] path;
    private Trajectory.Config config;
    private TrajectoryUtil.AutoTarget target;

    /**
     * Stores a path, config and ending target to private variables
     * @param path array of waypoints describing a motion profile
     * @param config Trajectory.Config configs to be used while creating motion profile
     * @param target Ending location of the autonomous profile
     */
    public Autonomous(Waypoint[] path, Trajectory.Config config, TrajectoryUtil.AutoTarget target) {
        this.path = path;
        this.config = config;
        this.target = target;
    }

    // Getters for private variables
    public Waypoint[] getPath() {
        return path;
    }

    public Trajectory.Config getConfig() {
        return config;
    }

    public TrajectoryUtil.AutoTarget getTarget() {
        return target;
    }
}
