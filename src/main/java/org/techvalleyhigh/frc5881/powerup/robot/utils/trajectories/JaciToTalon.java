package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import jaci.pathfinder.Trajectory;

import java.util.ArrayList;

public class JaciToTalon {
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

            // Convert fps -> RPM
            double rpm = segment.velocity / (30d * Math.PI);

            // Convert radians -> degrees
            double heading = segment.heading * 180 / Math.PI;

            // Set value
            output[i] = new double[] {rot, rpm, dt, heading};
        }
        // Return
        return output;
    }

    /**
     * Generates a trapezoidal motion profile to go straightPath
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

            // Convert f/s to RPM
            double rpm = v / (30.0 * Math.PI);

            // Form the array, heading is always 0
            output[i] = new double[] {rot, rpm, dt, 0};
        }

        return output;
    }
}