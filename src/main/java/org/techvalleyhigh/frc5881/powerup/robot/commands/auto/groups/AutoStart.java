package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import openrio.powerup.MatchData;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.SetArm;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.SetElevator;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.motion.MotionProfile;
import org.techvalleyhigh.frc5881.powerup.robot.subsystem.Elevator;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;

public class AutoStart extends CommandGroup {
    private Autonomous auto;
    private MotionProfile profile;
    private ManipulatorClose close;
    private SetArm arm;
    private SetElevator elevator;

    public AutoStart(Autonomous auto) {
        this.auto = auto;
        this.profile = new MotionProfile(auto);
        this.close = new ManipulatorClose();
        this.arm = new SetArm(800, 0);

        if (auto.getFeature() == MatchData.GameFeature.SWITCH_NEAR) {
            this.elevator = new SetElevator(Elevator.switchTicks, auto.getTimeToWait());

        } else if (auto.getFeature() == MatchData.GameFeature.SCALE) {
            this.elevator = new SetElevator(Elevator.scaleTicks, auto.getTimeToWait());
        }
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return profile.isCompleted();
    }

    @Override
    protected void end() {
        System.out.println("Auto start has finished");
    }

    @Override
    protected void interrupted() {
        System.out.println("Auto start has been interrupted... That shouldn't happen");
    }
}
