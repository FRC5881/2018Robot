package org.techvalleyhigh.frc5881.powerup.robot.subsystem;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.utils.vision.VisionCamera;


/**
 *
 */
public class Vision extends Subsystem {
    //We can totally have more than one camera up here
    /**
     * Holds the vision camera
     */
    public VisionCamera camera;

    public Vision(VisionCamera camera) {
        super();
        init();

        //Define each camera
        this.camera = camera;
    }

    public Vision(String name, VisionCamera camera) {
        super(name);
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
