package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.motion;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;
import org.techvalleyhigh.frc5881.powerup.robot.RobotMap;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.Autonomous;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.JaciToTalon;
import org.techvalleyhigh.frc5881.powerup.robot.utils.trajectories.TrajectoryUtil;

public class Profile extends Command {
    private double[] leftPoints;
    private double[] rightPoints;
    private int current;

    public Profile(Autonomous auto) {
        // Get pid values
        Robot.driveControl.initPID();

        // Lets see if we can load a stored trajectory....
        Trajectory trajectory = TrajectoryUtil.loadTrajectoryFromFile(auto.getAutoNumber());

        // If we were unable to load the trajectory then it'll be null.
        if (trajectory == null) {
            // Generate trajectory
            System.err.println("Unable to load Auto " + auto.getAutoNumber() + " Generating Path...");
            trajectory = Pathfinder.generate(auto.getPath(), auto.getConfig());
        } else {
            System.out.println("Loaded stored path...");
        }

        // Change trajectory into a tank drive
        // Wheelbase Width = 2.3226166667 feet
        TankModifier modifier = new TankModifier(trajectory).modify(2.3226166667);

        // Separate trajectories for left and right
        Trajectory leftTrajectory = modifier.getLeftTrajectory();
        Trajectory rightTrajectory = modifier.getRightTrajectory();

        leftPoints = JaciToTalon.positions(leftTrajectory);
        rightPoints = JaciToTalon.positions(rightTrajectory);
    }

    @Override
    protected void initialize() {
        current = 0;
        RobotMap.driveFrontLeft.set(ControlMode.Position, 0);
        RobotMap.driveFrontRight.set(ControlMode.Position, 0);
    }

    @Override
    protected void execute() {
        if (current < leftPoints.length) {
            System.out.println(RobotMap.driveFrontLeft.getClosedLoopTarget(0));

            SmartDashboard.putNumber("left idk target", leftPoints[current]);
            SmartDashboard.putNumber("right idk target", rightPoints[current]);

            RobotMap.driveFrontLeft.set(ControlMode.Position, leftPoints[current]);
            RobotMap.driveFrontRight.set(ControlMode.Position, rightPoints[current]);

            if (RobotMap.driveFrontLeft.getClosedLoopError(0) < 50 || RobotMap.driveFrontRight.getClosedLoopError(0) < 50) {
                current++;
            }
        }
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }

    @Override
    protected boolean isFinished() {
        return false; //current >= leftPoints.length;
    }
}
