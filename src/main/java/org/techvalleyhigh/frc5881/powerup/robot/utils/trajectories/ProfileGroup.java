package org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories;


import java.util.HashMap;

public class ProfileGroup {
    private static HashMap<Integer, Autonomous> autos;

    public static Autonomous getAuto(int autoNum) {
        return autos.get(autoNum);
    }
}
