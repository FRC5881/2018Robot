package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;

/**
 * Interface to handle getAuto in each profile group
 */
public interface ProfileGroup {
    static Autonomous getAuto(int autoNum) {
        // Return nothing it'll be overridden
        return null;
    }
}
