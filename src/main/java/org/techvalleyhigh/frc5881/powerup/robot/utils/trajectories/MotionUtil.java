package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;

public class MotionUtil {
    // 6 inch diameter wheel
    public static final double wheelRadius = 3.0/12.0;

    /**
     * Convert a PathFinder trajectory into a format talons can use
     * @param trajectory input Trajectory
     * @return double[][] with inputs MotionProfileExample can handle
     */
    public static double[][] makeProfile(Trajectory trajectory) {
        // Initiates 2d array
        double[][] output = new double[trajectory.length()][4];

        // Save delta time form 1st segment, since they're all the same anyway
        double dt = trajectory.get(0).dt * 1000;

        // For each segment
        for (int i = 0; i < trajectory.length(); i++) {
            Trajectory.Segment segment = trajectory.get(i);

            // Convert feet -> rotations
            double rot = segment.position * 2d / Math.PI;

            // Linear velocity = radius * angular velocity RAD / second
            // v = r * w
            // v / r = w
            double radiansPerSecond = segment.velocity / wheelRadius;

            // k RAD / second * 2pi/60 = k RPM
            double rpm = radiansPerSecond * 2 * Math.PI / 60;

            // Convert radians -> degrees
            double heading = segment.heading * 180d / Math.PI;

            // Set value
            output[i] = new double[] {rot, rpm, dt, heading};
        }
        // Return
        return output;
    }

    /**
     * Creates a one dimensional array of just the position of every segment in a Pathfinder.Trajectory object
     * @param trajectory PathFinder.Trajectory object to pull positions from
     * @param skip Set >1 to skip some set points
     * @return Positions converted from feet to rotations
     */
    public static double[] positions(Trajectory trajectory, int skip) {
        // Create new output array
        double[] out = new double[trajectory.length()];

        // Loop through each trajectory segment
        for (int i = 0; i < trajectory.length(); i += skip) {
            Trajectory.Segment segment = trajectory.get(i);

            out[i] = segment.position;
        }

        // Return;
        return out;
    }

    /**
     * Creates a one dimensional array of just the velocities of every segment in a Pathfinder.Trajectory object
     * @param trajectory PathFinder.Trajectory object to pull velocities from
     * @return Velocities in feet per second
     */
    public static double[] velocities(Trajectory trajectory) {
        double[] out = new double[trajectory.length()];

        for (int i = 0; i < trajectory.length(); i++) {
            Trajectory.Segment segment = trajectory.get(i);

            // Linear velocity = radius * angular velocity RAD / second
            // v = r * w
            // v / r = w
            //double radiansPerSecond = segment.velocity / wheelRadius;

            // k RAD / second * 2pi/60 = k RPM
            out[i] = segment.velocity;
        }

        // Return
        return out;
    }

    /**
     * Generates a trapezoidal profiles profile to go straightPath
     * @param distance distance to travel
     * @param velocity max velocity
     * @param acceleration max acceleration
     * @param dt length of time intervals
     * @return double[][] with inputs MotionProfileExample can handle
     */
    public static double[][] straightPath(double distance, double velocity, double acceleration, double dt) {
        // Time it takes to accelerate to max velocity
        double t_a = velocity / acceleration;

        // Total distance traveled while accelerating and decelerating
        double d_a = t_a * velocity;

        // Remaining distance to travel at velocity
        double d_v = distance - d_a;

        // Time to travel at velocity to cover remaining distance
        double t_v = d_v / velocity;

        // Total points to generate
        Double n = t_v + 2 * t_a;
        Integer length = n.intValue();

        // Initiates 2d array
        double[][] output = new double[length][4];

        // Distance (area under the curve)
        double d = 0;

        // Start looping
        for (int i = 0; i < length; i++) {
            double time = dt * i;
            double v = 0;

            // Accelerating to speed
            if (time < t_a) {
                // v = v0 + at
                v = (time * acceleration);

                // d = v0 * t + 0.5 * a * t^2
                d += v * dt + 0.5 * acceleration * (dt * dt);
            }
            // At cruising speed
            else if (time < t_a + t_v) {
                // d = vt
                d += velocity * dt;
            }
            // Decelerating
            else {
                // v = v0 + at
                v = velocity - acceleration * (time - (t_a + t_v));

                // d = v0 * t + 0.5 * a * t^2
                d += v * dt - 0.5 * acceleration * (dt * dt);
            }

            // Convert feet to rotations
            double rot = d * 2.0 / Math.PI;

            // Linear velocity = radius * angular velocity RAD / second
            // v = r * w
            // v / r = w
            double radiansPerSecond = velocity / wheelRadius;

            // k RAD / second * 2pi/60 = k RPM
            double rpm = radiansPerSecond * 2 * Math.PI / 60;

            // Form the array, heading is always 0
            output[i] = new double[] {rot, rpm, dt, 0};
        }

        return output;
    }
}
