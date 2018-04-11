package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.profiles.motion_profile;

/**
 * Store constant used in profiles profile
 */
public class MotionConstants {
    /**
     * How many sensor units per rotation.
     *
     */
    public static final double kSensorUnitsPerRotation = 1440;

    /**
     * set to zero to skip waiting for confirmation, set to nonzero to wait and
     * report to DS if action fails.
     */
    public static final int kTimeoutMs = 10;

    /**
     * Base trajectory period to add to each individual trajectory point's
     * unique duration. This can be set to any value within [0,255]ms.
     */
    public static final int kBaseTrajPeriodMs = 0;

    /**
     * Max number of points to hold in talon buffers
     */
    public static final int maxTrajectories = 6;
}
