package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class Autonomous {
    private Waypoint[] path;
    private Trajectory.Config config;
    private TrajectoryUtil.AutoTarget target;

    public Autonomous(Waypoint[] path, Trajectory.Config config, TrajectoryUtil.AutoTarget target) {
        this.path = path;
        this.config = config;
        this.target = target;
    }

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
