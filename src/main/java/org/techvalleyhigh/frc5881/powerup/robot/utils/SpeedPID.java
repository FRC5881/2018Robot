package org.techvalleyhigh.frc5881.powerup.robot.utils;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class SpeedPID implements PIDSource {
    /**
     * Set which parameter of the device you are using as a process control variable.
     *
     * @param pidSource An enum to select the parameter.
     */
    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {

    }

    /**
     * Get which parameter of the device you are using as a process control variable.
     *
     * @return the currently selected PID source parameter
     */
    @Override
    public PIDSourceType getPIDSourceType() {
        return null;
    }

    /**
     * Get the result to use in PIDController.
     *
     * @return the result to use in PIDController
     */
    @Override
    public double pidGet() {
        return 0;
    }
}
