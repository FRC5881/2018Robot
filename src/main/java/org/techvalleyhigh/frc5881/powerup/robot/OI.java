package org.techvalleyhigh.frc5881.powerup.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorFlip;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.manipulator.ManipulatorOpen;
import org.techvalleyhigh.frc5881.powerup.robot.commands.auto.control.SetArm;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ratchet.RatchetDisable;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ratchet.RatchetEnable;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ratchet.RatchetFlip;

/**
 * Controls operator interfaces, such as controllers (and a few buttons)
 */
public class OI {
    public final GenericHID driverController;

    public final JoystickButton driveControllerButtonA;
    public final JoystickButton driveControllerButtonB;
    public final JoystickButton driveControllerButtonX;
    public final JoystickButton driveControllerButtonY;
    public final JoystickButton driveControllerBackButton;
    public final JoystickButton driveControllerStartButton;
    public final JoystickButton driveControllerLeftBumper;
    public final JoystickButton driveControllerRightBumper;

    // Joysticks
    public static final int XBOX_LEFT_Y_AXIS = 1;
    public static final int XBOX_LEFT_X_AXIS = 0;
    public static final int XBOX_LEFT_TRIGGER = 3;
    public static final int XBOX_RIGHT_TRIGGER = 2;
    public static final int XBOX_RIGHT_Y_AXIS = 5;
    public static final int XBOX_RIGHT_X_AXIS = 4;

    public static final int BUTTON_A = 1;
    public static final int BUTTON_B = 2;
    public static final int BUTTON_X = 3;
    public static final int BUTTON_Y = 4;
    public static final int BUTTON_LEFT_BUMPER = 5;
    public static final int BUTTON_RIGHT_BUMPER = 6;
    public static final int BUTTON_BACK = 7;
    public static final int BUTTON_START = 8;


    public OI() {
        // Define controllers as joysticks
        driverController = new Joystick(0);

        // Assign EACH xBox controller button
        driveControllerButtonA = new JoystickButton(driverController, BUTTON_A);
        driveControllerButtonB = new JoystickButton(driverController, BUTTON_B);
        driveControllerButtonX = new JoystickButton(driverController, BUTTON_X);
        driveControllerButtonY = new JoystickButton(driverController, BUTTON_Y);
        driveControllerLeftBumper = new JoystickButton(driverController, BUTTON_LEFT_BUMPER);
        driveControllerRightBumper = new JoystickButton(driverController, BUTTON_RIGHT_BUMPER);
        driveControllerBackButton = new JoystickButton(driverController, BUTTON_BACK);
        driveControllerStartButton = new JoystickButton(driverController, BUTTON_START);

        // Turns the rumble off in case it was left on
        driverController.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
        driverController.setRumble(GenericHID.RumbleType.kRightRumble, 0);

        // Controls cube manipulator closing / opening
        driveControllerButtonA.whenPressed(new ManipulatorOpen());
        driveControllerButtonB.whenPressed(new ManipulatorClose());
        driveControllerButtonY.whenPressed(new ManipulatorFlip());

        // Controls elevator ratchet
        driveControllerBackButton.whenPressed(new RatchetEnable());
        driveControllerStartButton.whenPressed(new RatchetDisable());
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
