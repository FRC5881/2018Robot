package org.techvalleyhigh.frc5881.powerup.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorFlip;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorOpen;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.SetArm;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ratchet.DisableRatchet;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ratchet.EnableRatchet;

/**
 * Controls operator interfaces, such as controllers (and a few buttons)
 */
public class OI {
    public final GenericHID driverController;
    public final GenericHID coPilotController;

    public final JoystickButton driveControllerButtonA;
    public final JoystickButton driveControllerButtonB;
    public final JoystickButton driveControllerButtonX;
    public final JoystickButton driveControllerButtonY;
    public final JoystickButton driveControllerBackButton;
    public final JoystickButton driveControllerStartButton;
    public final JoystickButton driveControllerLeftBumper;
    public final JoystickButton driveControllerRightBumper;

    public final JoystickButton coPilotTrigger;
    public final JoystickButton coPilotLeftTrigger;
    public final JoystickButton coPilotTopBackLeft;
    public final JoystickButton coPilotTopBackRight;
    public final JoystickButton coPilotTopFrontLeft;
    public final JoystickButton coPilotTopFrontRight;
    public final JoystickButton coPilotBottomLeftForward;
    public final JoystickButton coPilotBottomRightForward;
    public final JoystickButton coPilotBottomLeftMiddle;
    public final JoystickButton coPilotBottomRightMiddle;
    public final JoystickButton coPilotBottomLeftBack;
    public final JoystickButton coPilotBottomRightBack;

    // Joysticks
    public static final int XBOX_LEFT_Y_AXIS = 1;
    public static final int XBOX_LEFT_X_AXIS = 2;
    public static final int XBOX_RIGHT_Y_AXIS = 3;
    public static final int XBOX_RIGHT_X_AXIS = 4;

    public static final int BUTTON_A = 1;
    public static final int BUTTON_B = 2;
    public static final int BUTTON_X = 3;
    public static final int BUTTON_Y = 4;
    public static final int BUTTON_LEFT_BUMPER = 5;
    public static final int BUTTON_RIGHT_BUMPER = 6;
    public static final int BUTTON_BACK = 7;
    public static final int BUTTON_START = 8;

    public static final int PILOT_Y_AXIS = 0;
    public static final int PILOT_X_AXIS = 1;
    public static final int PILOT_Z_ROTATION = 2;
    public static final int PILOT_SLIDER = 3;

    public static final int BUTTON_TRIGGER = 1;
    public static final int BUTTON_LEFT_TRIGGER = 2;
    public static final int BUTTON_TOP_BACK_LEFT = 3;
    public static final int BUTTON_TOP_BACK_RIGHT = 4;
    public static final int BUTTON_TOP_FRONT_LEFT = 5;
    public static final int BUTTON_TOP_FRONT_RIGHT = 6;
    public static final int BUTTON_BOTTOM_LEFT_FORWARD = 7;
    public static final int BUTTON_BOTTOM_RIGHT_FORWARD = 8;
    public static final int BUTTON_BOTTOM_LEFT_MIDDLE = 9;
    public static final int BUTTON_BOTTOM_RIGHT_MIDDLE = 10;
    public static final int BUTTON_BOTTOM_LEFT_BACK = 11;
    public static final int BUTTON_BOTTOM_RIGHT_BACK = 12;

    public OI() {
        // Define controllers as joysticks
        driverController = new Joystick(0);
        coPilotController = new Joystick(1);

        // Assign EACH xBox controller button
        driveControllerButtonA = new JoystickButton(driverController, BUTTON_A);
        driveControllerButtonB = new JoystickButton(driverController, BUTTON_B);
        driveControllerButtonX = new JoystickButton(driverController, BUTTON_X);
        driveControllerButtonY = new JoystickButton(driverController, BUTTON_Y);
        driveControllerLeftBumper = new JoystickButton(driverController, BUTTON_LEFT_BUMPER);
        driveControllerRightBumper = new JoystickButton(driverController, BUTTON_RIGHT_BUMPER);
        driveControllerBackButton = new JoystickButton(driverController, BUTTON_BACK);
        driveControllerStartButton = new JoystickButton(driverController, BUTTON_START);

        // Assign EACH coPilot button
        coPilotTrigger = new JoystickButton(coPilotController, BUTTON_TRIGGER);
        coPilotLeftTrigger = new JoystickButton(coPilotController, BUTTON_LEFT_TRIGGER);
        coPilotTopBackLeft = new JoystickButton(coPilotController, BUTTON_TOP_BACK_LEFT);
        coPilotTopBackRight = new JoystickButton(coPilotController, BUTTON_TOP_BACK_RIGHT);
        coPilotTopFrontLeft = new JoystickButton(coPilotController, BUTTON_TOP_FRONT_LEFT);
        coPilotTopFrontRight = new JoystickButton(coPilotController, BUTTON_TOP_FRONT_RIGHT);
        coPilotBottomLeftForward = new JoystickButton(coPilotController, BUTTON_BOTTOM_LEFT_FORWARD);
        coPilotBottomRightForward = new JoystickButton(coPilotController, BUTTON_BOTTOM_RIGHT_FORWARD);
        coPilotBottomLeftMiddle = new JoystickButton(coPilotController, BUTTON_BOTTOM_LEFT_MIDDLE);
        coPilotBottomRightMiddle = new JoystickButton(coPilotController, BUTTON_BOTTOM_RIGHT_MIDDLE);
        coPilotBottomLeftBack = new JoystickButton(coPilotController, BUTTON_BOTTOM_LEFT_BACK);
        coPilotBottomRightBack = new JoystickButton(coPilotController, BUTTON_BOTTOM_RIGHT_BACK);

        // Turns the rumble off
        driverController.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
        driverController.setRumble(GenericHID.RumbleType.kRightRumble, 0);

        // Add commands below but DON'T put elevator and arm autonomous commands down here since
        // they interfere with operator controls
        coPilotTrigger.whenPressed(new ManipulatorFlip());

        coPilotBottomLeftForward.whenPressed(new EnableRatchet());
        coPilotBottomRightForward.whenPressed(new DisableRatchet());

        coPilotBottomRightBack.whenPressed(new SetArm(1870, 0));
    }

    /**
     * Applies deadzone to input and scales output
     * @param input the input to apply a deadzone to
     * @param deadZone the deadzone to apply
     * @return 0 if absolute value of the input is less than dead zone or the signed input squared if otherwise
     */
    public static double applyDeadzone(double input, double deadZone) {
        double output;

        if (Math.abs(input) < deadZone) {
            output = 0;
        } else {
            // If we're above the joystick deadzone sqaure the inputs but keep the sign
            output = input < 0 ? -input * input : input * input;
        }

        return output;
    }
}
