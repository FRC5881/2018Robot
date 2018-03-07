package org.techvalleyhigh.frc5881.powerup.robot.test;

import org.junit.Test;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.GetTimes;

public class Times {

    @Test
    public void testTimes() {
        try {
            GetTimes.printTimes();
        } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            System.err.println("Unable to execute Pathfinder. Please install library into one of these paths:");
            System.err.println(System.getProperty("java.library.path"));
        }
    }
}
