package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.motion.MotionProfile;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;

public class profile extends CommandGroup {
    Autonomous auto;
    MotionProfile profile;
    ManipulatorClose close;

}
