package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.profiles;

import jaci.pathfinder.Waypoint;

public class CustomFigureEight {
    public CustomFigureEight(double width, double height) {
        Waypoint[] points = new Waypoint[] {
                new Waypoint(width / 2, 0, 0),
                new Waypoint(width, height / 4, Math.PI / 4),
                new Waypoint(width / 2, height / 2, Math.toRadians(135)),
                new Waypoint(0, height / 2, Math.PI / 4),
                new Waypoint(width / 2, height, 0),
                new Waypoint(width, 3 * width / 4, -Math.PI / 4),
                new Waypoint(width / 2, height / 2, Math.toRadians(-135)),
                new Waypoint(0, height / 4, -Math.PI / 4)
        };
    }
}
