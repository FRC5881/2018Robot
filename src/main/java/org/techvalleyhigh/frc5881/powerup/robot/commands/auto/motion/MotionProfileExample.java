/**
 * Example logic for firing and managing auto profiles.
 * This example sends MPs, waits for them to finish
 * Although this code uses a CANTalon, nowhere in this module do we changeMode() or call set() to change the output.
 * This is done in Robot2.java to demonstrate how to change control modes on the fly.
 *
 * The only routines we call on Talon are....
 *
 * changeMotionControlFramePeriod
 *
 * getMotionProfileStatus
 * clearMotionProfileHasUnderrun     to get status and potentially clear the error flag.
 *
 * pushMotionProfileTrajectory
 * clearMotionProfileTrajectories
 * processMotionProfileBuffer,   to push/clear, and process the trajectory points.
 *
 * getControlMode, to check if we are in Motion Profile Control mode.
 *
 * Example of advanced features not demonstrated here...
 * [1] Calling pushMotionProfileTrajectory() continuously while the Talon executes the auto profile, thereby keeping it going indefinitely.
 * [2] Instead of setting the sensor position to zero at the start of each MP, the program could offset the MP's position based on current position.
 */
package org.techvalleyhigh.frc5881.powerup.robot.commands.auto.motion;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Notifier;
import com.ctre.phoenix.motion.*;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.techvalleyhigh.frc5881.powerup.robot.Robot;

public class MotionProfileExample {

    /**
     * The status of the auto profile executer and buffer inside the Talon.
     * Instead of creating a new one every time we call getMotionProfileStatus,
     * keep one copy.
     */
    private MotionProfileStatus _status = new MotionProfileStatus();

    /** additional cache for holding the active trajectory point */
    double _pos=0,_vel=0,_heading=0;

    /**
     * reference to the talon we plan on manipulating. We will not changeMode()
     * or call set(), just get auto profile status and make decisions based on
     * auto profile.
     */
    private TalonSRX _talon;
    /**
     * State machine to make sure we let enough of the auto profile stream to
     * talon before we fire it.
     */
    private int _state = 0;
    /**
     * Any time you have a state machine that waits for external events, its a
     * good idea to add a timeout. Set to -1 to disable. Set to nonzero to count
     * down to '0' which will print an error message. Counting loops is not a
     * very accurate method of tracking timeout, but this is just conservative
     * timeout. Getting time-stamps would certainly work too, this is just
     * simple (no need to worry about timer overflows).
     */
    private int _loopTimeout = -1;
    /**
     * If start() gets called, this flag is set and in the control() we will
     * service it.
     */
    private boolean _bStart = false;

    /**
     * Since the CANTalon.set() routine is mode specific, deduce what we want
     * the set value to be and let the calling module apply it whenever we
     * decide to switch to MP mode.
     */
    private SetValueMotionProfile _setValue = SetValueMotionProfile.Disable;
    /**
     * How many trajectory points do we wait for before firing the auto
     * profile.
     */
    private static final int kMinPointsInTalon = 5;
    /**
     * Just a state timeout to make sure we don't get stuck anywhere. Each loop
     * is about 20ms.
     */
    private static final int kNumLoopsTimeout = 10;

    /**
     * Stores trajectory points
     */
    private double[][] _points;

    /**
     * Store current place in trajectory
     */
    private int currentPoint;

    /**
     * Stores whether drive talon is on the left side or not
     */
    private boolean isLeft;

    /**
     * Starting angle we start auto at
     */
    private double startAngle;

    /**
     * Lets create a periodic task to funnel our trajectory points into our talon.
     * It doesn't need to be very accurate, just needs to keep pace with the auto
     * profiler executer.  Now if you're trajectory points are slow, there is no need
     * to do this, just call _talon.processMotionProfileBuffer() in your teleop loop.
     * Generally speaking you want to call it at least twice as fast as the duration
     * of your trajectory points.  So if they are firing every 20ms, you should call
     * every 10ms.
     */
    class PeriodicRunnable implements java.lang.Runnable {
        public void run() {  _talon.processMotionProfileBuffer();    }
    }
    Notifier _notifer = new Notifier(new PeriodicRunnable());


    /**
     * C'tor
     *
     * @param talon
     *            reference to Talon object to fetch auto profile status from.
     */
    public MotionProfileExample(TalonSRX talon, double [][] _points, boolean isLeft, double startAngle) {
        _talon = talon;
        this._points = _points;
        this.isLeft = isLeft;
        this.currentPoint = 0;
        this.startAngle = startAngle;

        /*
         * since our MP is 10ms per point, set the control frame rate and the
         * notifer to half that
         */
        _talon.changeMotionControlFramePeriod(5);
        _notifer.startPeriodic(0.005);
    }

    /**
     * Called to clear Motion profile buffer and reset state info during
     * disabled and when Talon is not in MP control mode.
     */
    public void reset() {
        /*
         * Let's clear the buffer just in case user decided to disable in the
         * middle of an MP, and now we have the second half of a profile just
         * sitting in memory.
         */
        _talon.clearMotionProfileTrajectories();
        /* When we do re-enter motionProfile control mode, stay disabled. */
        _setValue = SetValueMotionProfile.Disable;
        /* When we do start running our state machine start at the beginning. */
        _state = 0;
        _loopTimeout = -1;
        /*
         * If application wanted to start an MP before, ignore and wait for next
         * button press
         */
        _bStart = false;
    }

    /**
     * Called every loop.
     */
    public void control() {
        /* Get the auto profile status every loop */
        _talon.getMotionProfileStatus(_status);

        /*
         * track time, this is rudimentary but that's okay, we just want to make
         * sure things never get stuck.
         */
        if (_loopTimeout < 0) {
            /* do nothing, timeout is disabled */
        } else {
            /* our timeout is nonzero */
            if (_loopTimeout == 0) {
                /*
                 * something is wrong. Talon is not present, unplugged, breaker
                 * tripped
                 */
                System.out.println("Motion Profile : Talon Timeout");
            } else {
                --_loopTimeout;
            }
        }

        /* first check if we are in MP mode */
        if (_talon.getControlMode() != ControlMode.MotionProfile) {
            /*
             * we are not in MP mode. We are probably driving the robot around
             * using gamepads or some other mode.
             */
            _state = 0;
            _loopTimeout = -1;
        } else {
            /*
             * we are in MP control mode. That means: starting Mps, checking Mp
             * progress, and possibly interrupting MPs if thats what you want to
             * do.
             */
            switch (_state) {
                case 0: /* wait for application to tell us to start an MP */
                    if (_bStart) {
                        _bStart = false;

                        _setValue = SetValueMotionProfile.Disable;
                        startFilling();
                        /*
                         * MP is being sent to CAN bus, wait a small amount of time
                         */
                        _state = 1;
                        _loopTimeout = kNumLoopsTimeout;

                        /*
                         * just in case we are interrupting another MP and there is still buffer
                         * points in memory, clear it.
                         */
                        _talon.clearMotionProfileTrajectories();

                        /* set the base trajectory period to zero, use the individual trajectory period below */
                        _talon.configMotionProfileTrajectoryPeriod(MotionConstants.kBaseTrajPeriodMs, MotionConstants.kTimeoutMs);
                    }
                    break;
                case 1:
                    if (_status.btmBufferCnt > kMinPointsInTalon) {
                        /* start (once) the auto profile */
                        /* MP will start once the control frame gets scheduled */
                        _setValue = SetValueMotionProfile.Enable;
                    }

                    if (_status.btmBufferCnt + _status.topBufferCnt < MotionConstants.maxTrajectories) {
                        _loopTimeout = kNumLoopsTimeout;
                        fill(currentPoint, MotionConstants.maxTrajectories - (_status.btmBufferCnt + _status.topBufferCnt));
                    }
                    /*
                     * wait for MP to stream to Talon, really just the first few
                     * points
                     */
                    /* do we have a minimum numberof points in Talon */
                    if (currentPoint >= _points.length) {
                        _state = 2;
                        _loopTimeout = kNumLoopsTimeout;
                    }
                    break;
                case 2: /* check the status of the MP */
                    /*
                     * if talon is reporting things are good, keep adding to our
                     * timeout. Really this is so that you can unplug your talon in
                     * the middle of an MP and react to it.
                     */
                    if (_status.isUnderrun == false) {
                        _loopTimeout = kNumLoopsTimeout;
                    }
                    /*
                     * If we are executing an MP and the MP finished, start loading
                     * another. We will go into hold state so robot servo's
                     * position.
                     */
                    if (_status.activePointValid && _status.isLast) {
                        /*
                         * because we set the last point's isLast to true, we will
                         * get here when the MP is done
                         */
                        System.out.println("Setting Disabled - end of MP");
                        _setValue = SetValueMotionProfile.Disable;
                        _state = 0;
                        _loopTimeout = -1;
                    }
                    break;
            }

            /* Get the auto profile status every loop */
            _talon.getMotionProfileStatus(_status);
            _heading = _talon.getActiveTrajectoryHeading();
            _pos = _talon.getActiveTrajectoryPosition();
            _vel = _talon.getActiveTrajectoryVelocity();
        }
    }
    /**
     * Find enum value if supported.
     * @param durationMs
     * @return enum equivalent of durationMs
     */
    private TrajectoryDuration GetTrajectoryDuration(int durationMs) {
        /* create return value */
        TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;
        /* convert duration to supported type */
        retval = retval.valueOf(durationMs);

        /* pass to caller */
        return retval;
    }

    /** Start filling the MPs to all of the involved Talons. */
    private void startFilling() {
        /* since this example only has one talon, just update that one */
        fill(currentPoint, MotionConstants.maxTrajectories);
    }

    /**
     * Pushes a number of points into motion profile buffer
     * @param start Where in generated profile to start pushing values from
     * @param cnt How many points to push
     */
    private void fill(int start, int cnt) {
        // create an empty point
        TrajectoryPoint point = new TrajectoryPoint();

        // did we get an under run condition since last time we checked?
        if (_status.hasUnderrun) {
            /*
             * clear the error. This flag does not auto clear, this way
             * we never miss logging it.
             */
            _talon.clearMotionProfileHasUnderrun(0);
        }

        // This is fast since it's just into our TOP buffer
        for (int i = start; i < start + cnt && i < _points.length; ++i) {


            // Edit velocity to account for drift using gyro pid
            double setpoint = _points[i][3] - startAngle;
            if (setpoint > 360) {
                setpoint = -1 * (setpoint - 360);
            }

            Robot.driveControl.setGyroPid(setpoint);
            double percentage = 1 + (Robot.driveControl.gyroPIDOutput * (isLeft ? 1 : -1));
            double velocity = _points[i][1] * percentage;
            SmartDashboard.putNumber("Change in velocity", velocity - _points[i][1]);

            // Edit the position to account for drift using physics
            // Change in position = integral of the change in velocity (assume constant acceleration)
            // d = v0 * t + 0.5 * a * t^2
            // d = v0 * dt + 0.5(v1 - v0)dt^2
            double d = 0;
            // As long as there is a next point to get an acceleration from
            if (i + 1 < _points.length) {
                double dt = _points[i][2] / 1000;
                d = velocity * (dt) + 0.5 * percentage * (_points[i + 1][1] - _points[i][1]) * (dt * dt);
            }
            double position =_points[i][0] + d;
            SmartDashboard.putNumber("Change in position", position - _points[i][0]);

            // for each point, fill our structure and pass it to API
            //point.position = position * MotionConstants.kSensorUnitsPerRotation; // Convert Revolutions to Units
            //point.velocity = velocity * MotionConstants.kSensorUnitsPerRotation / 600.0; // Convert RPM to Units/100ms
            point.position = _points[i][0] * MotionConstants.kSensorUnitsPerRotation;
            point.velocity = _points[i][1] * MotionConstants.kSensorUnitsPerRotation / 600;
            point.headingDeg = 0; // future feature - not used in this example
            point.profileSlotSelect0 = 0; // which set of gains would you like to use [0,3]?
            point.profileSlotSelect1 = 0; // future feature  - not used in this example - cascaded PID [0,1], leave zero
            point.timeDur = GetTrajectoryDuration((int)_points[i][2]);
            point.zeroPos = i == 0;
            point.isLastPoint = (i + 1) == _points.length;

            _talon.pushMotionProfileTrajectory(point);
            currentPoint++;
        }
    }
    /**
     * Called by application to signal Talon to start the buffered MP (when it's
     * able to).
     */
    public void startMotionProfile() {
        _bStart = true;
    }

    /**
     *
     * @return the output value to pass to Talon's set() routine. 0 for disable
     *         auto-profile output, 1 for enable auto-profile, 2 for hold
     *         current auto profile trajectory point.
     */
    public SetValueMotionProfile getSetValue() {
        return _setValue;
    }
}