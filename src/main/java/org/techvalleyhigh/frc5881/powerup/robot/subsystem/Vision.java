package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.utils.vision.VisionCamera;


/**
 * Created by cmahoney on 10/28/2018
 */
public class Vision extends Subsystem {
    //We can totally have more than one camera up here
    /**
     * Holds the vision camera
     */
    public VisionCamera camera;

    public Vision(VisionCamera camera) {
        init();

        //Define each camera
        this.camera = camera;
    }

    public Vision(String name, VisionCamera camera) {
        init();

        //Define each camera
        this.camera = camera;
    }

    public void init() {

    }

    @Override
    protected void initDefaultCommand() {
        // No commands, this is a read-only subsystem
    }
}