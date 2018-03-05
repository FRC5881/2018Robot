package org.techvalleyhigh.frc5881.powerup.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorOpen;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ratchet.ElevatorDisableRatchet;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ratchet.ElevatorEnableRatchet;
/**
 * Controls operator interfaces, such as controllers
 */
public class OI {
    public GenericHID driverController;

    public GenericHID pilotController;

    public JoystickButton driveControllerButtonA;
    public JoystickButton driveControllerButtonB;
    public JoystickButton driveControllerButtonX;
    public JoystickButton driveControllerButtonY;
    public JoystickButton driveControllerBackButton;
    public JoystickButton driveControllerStartButton;
    public JoystickButton driveControllerLeftBumper;
    public JoystickButton driveControllerRightBumper;

    public JoystickButton coPilotTrigger;
    public JoystickButton coPilotLeftTrigger;
    public JoystickButton coPilotTopBackLeft;
    public JoystickButton coPilotTopBackRight;
    public JoystickButton coPilotTopFrontLeft;
    public JoystickButton coPilotTopFrontRight;
    public JoystickButton coPilotBottomLeftForward;
    public JoystickButton coPilotBottomRightForward;
    public JoystickButton coPilotBottomLeftMiddle;
    public JoystickButton coPilotBottomRightMiddle;
    public JoystickButton coPilotBottomLeftBack;
    public JoystickButton coPilotBottomRightBack;

    // Joysticks
    /**
     * Controls Left joystick, forward/backward for Arcade Drive
     */
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
        pilotController = new Joystick(1);

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
        coPilotTrigger = new JoystickButton(pilotController, BUTTON_TRIGGER);
        coPilotLeftTrigger = new JoystickButton(pilotController, BUTTON_LEFT_TRIGGER);
        coPilotTopBackLeft = new JoystickButton(pilotController, BUTTON_TOP_BACK_LEFT);
        coPilotTopBackRight = new JoystickButton(pilotController, BUTTON_TOP_BACK_RIGHT);
        coPilotTopFrontLeft = new JoystickButton(pilotController, BUTTON_TOP_FRONT_LEFT);
        coPilotTopFrontRight = new JoystickButton(pilotController, BUTTON_TOP_FRONT_RIGHT);
        coPilotBottomLeftForward = new JoystickButton(pilotController, BUTTON_BOTTOM_LEFT_FORWARD);
        coPilotBottomRightForward = new JoystickButton(pilotController, BUTTON_BOTTOM_RIGHT_FORWARD);
        coPilotBottomLeftMiddle = new JoystickButton(pilotController, BUTTON_BOTTOM_LEFT_MIDDLE);
        coPilotBottomRightMiddle = new JoystickButton(pilotController, BUTTON_BOTTOM_RIGHT_MIDDLE);
        coPilotBottomLeftBack = new JoystickButton(pilotController, BUTTON_BOTTOM_LEFT_BACK);
        coPilotBottomRightBack = new JoystickButton(pilotController, BUTTON_BOTTOM_RIGHT_BACK);

        // Turns the rumble off
        pilotController.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
        driverController.setRumble(GenericHID.RumbleType.kRightRumble, 0);

        // Add commands below
        coPilotLeftTrigger.whenPressed(new ManipulatorOpen());
        coPilotTrigger.whenPressed(new ManipulatorClose());

        coPilotBottomLeftBack.whenPressed(new ElevatorEnableRatchet());
        coPilotBottomRightBack.whenPressed(new ElevatorDisableRatchet());
    }
}