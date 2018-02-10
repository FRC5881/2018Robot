package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

/**
 * Util class holding all kinds of useful data to streamline working with autonomous profiles
 */
public class TrajectoryUtil {
    // Starting locations
    public static final Waypoint startRight = new Waypoint(1.36333333, 3.9583, 0);
    public static final Waypoint startMiddle = new Waypoint(1.36333333, 13.0416, 0);
    public static final Waypoint startLeft = new Waypoint(1.36333333, 23.041, 0);

    // Ending locations
    public static final Waypoint leftSideSwitch = new Waypoint(14, 21.37496667, -Math.PI / 2);
    public static final Waypoint leftFrontSwitch = new Waypoint(10.20836667, 18.5, 0);
    public static final Waypoint leftSideScale = new Waypoint(27, 21.35413333, -Math.PI / 2);
    public static final Waypoint leftFrontScale = new Waypoint(23.54953333, 20, 0);
    public static final Waypoint rightSideSwitch = new Waypoint(14, 5.625033333, Math.PI / 2);
    public static final Waypoint rightFrontSwitch = new Waypoint(10.20836667, 8.5, 0);
    public static final Waypoint rightSideScale = new Waypoint(27, 5.645866667, Math.PI / 2);
    public static final Waypoint rightFrontScale = new Waypoint(23.54953333, 7, 0);

    // Configs
    public static final Trajectory.Config defaultConfig = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
            Trajectory.Config.SAMPLES_HIGH, 0.05, 5, 4, 60);

    /**
     * Set of waypoints that together generate a figure eight for testing
     */
    public static final Waypoint[] testFigureEight = new Waypoint[] {
            new Waypoint(10, 5, Math.toRadians(0)),
            new Waypoint(15, 10, Math.toRadians(90)),
            new Waypoint(10, 15, Math.toRadians(135)),
            new Waypoint(5, 20, Math.toRadians(90)),
            new Waypoint(10, 25, Math.toRadians(0)),
            new Waypoint(15, 20, Math.toRadians(270)),
            new Waypoint(10, 15, Math.toRadians(-135)),
            new Waypoint(5, 10, Math.toRadians(-90)),
            new Waypoint(10, 5, Math.toRadians(0))
    };

    /**
     * Reflects inputs across the line y = 13.5 and negate angles
     * Since the field is nicely symmetrical this allows us to flip auto paths from the left side to the right side
     * @param input input waypoints
     * @return
     */
    public static Waypoint[] mirror(Waypoint[] input) {
        Waypoint[] ret = input.clone();

        for (int i = 0; i < input.length; i++) {
            // Reflect over y = 13.5
            ret[i].y = 27d - input[i].y;

            // Negate the angle
            ret[i].angle = -input[i].angle;
        }

        return ret;
    }

    /**
     * Takes in the only 2 parameters we'll likely ever change using Trajectory.Config velocity and acceleration
     * And returns Trajectory.Config with everything else defaults
     * @param max_velocity max velocity to generate trajectories can drive
     * @param max_acceleration max acceleration the bot should never pass
     * @return {Trajectory.Config} our default trajectory with edited velocity and acceleration
     */
    public static Trajectory.Config ourConfig(double max_velocity, double max_acceleration) {
        return new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH,
                0.5, max_velocity, max_acceleration, 60);
    }

    /**
     * Possible auto targets the bot can reach left/right is in respect of our drive team
     */
    public enum AutoTarget {
        /**
         * Left side of the near switch
         */
        SWITCH_LEFT,

        /**
         * Right side of the near switch
         */
        SWITCH_RIGHT,

        /**
         * Left side of the switch
         */
        SCALE_LEFT,

        /**
         * Right side of the switch
         */
        SCALE_RIGHT,

        /**
         * Simply the autonomous line
         */
        AUTO_LINE
    }

    /**
     * Possible starting locations
     */
    public enum StartingLocations {
        /**
         * Left starting location
         */
        LEFT,
        /**
         * Middle starting location
         */
        MIDDLE,
        /**
         * Right starting location
         */
        RIGHT
    }

    /**
     * Returns array of waypoints that paths out a figure eight fits tightly inside a rectangle
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @return Waypoint[] of generated figure eight
     */
    public static Waypoint[] customFigureEight(double width, double height) {
        return new Waypoint[] {
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
