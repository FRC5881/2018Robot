package org.techvalleyhigh.frc5881.powerup.robot.commands.motion;

public class Constants {
    /**
     * How many sensor units per rotation.
     *
     */
    public static final double kSensorUnitsPerRotation = 1440;

    /**
     * Which PID slot to pull gains from. Starting 2018, you can choose from
     * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
     * configuration.
     */
    public static final int kSlotIdx = 0;

    /**
     * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
     * now we just want the primary one.
     */
    public static final int kPIDLoopIdx = 0;
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
     * Motor deadband, set to 1%.
     */
    public static final double kNeutralDeadband = 0.01;
}
