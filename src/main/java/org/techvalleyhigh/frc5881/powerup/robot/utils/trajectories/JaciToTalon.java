package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

import jaci.pathfinder.Trajectory;

public class JaciToTalon {
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
    };
}
