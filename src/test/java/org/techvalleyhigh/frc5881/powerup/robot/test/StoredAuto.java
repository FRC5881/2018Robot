package org.techvalleyhigh.frc5881.powerup.robot.test;

import jaci.pathfinder.Trajectory;
import org.junit.Assert;
import org.junit.Test;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

import java.util.Map;

public class StoredAuto {

    @Test
    public void testStoredAutos() {
        Map<Integer, Autonomous> autos = TrajectoryUtil.getAutos();

        for (int autoNumber : autos.keySet()) {
            //try {
                Trajectory trajectory = TrajectoryUtil.loadTrajectoryFromFile(autoNumber);

                Assert.assertNotNull("Null Trajectory " + autoNumber, trajectory);
            /*} catch (Exception e) {
                Assert.fail("Auto " + autoNumber + ": " + e.getMessage());
            }*/
        }
    }
}
