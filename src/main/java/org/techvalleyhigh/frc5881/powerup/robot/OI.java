package org.techvalleyhigh.frc5881.powerup.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ArmLower;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ArmRaise;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ManipulatorOpen;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorLiftAuto;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorLowerAuto;

/**
 * Controls operator interfaces, such as controllers
 */
public class OI {
    public GenericHID xboxController;

    public GenericHID xboxController2;

    public JoystickButton driveControllerButtonA;
    public JoystickButton driveControllerButtonB;
    public JoystickButton driveControllerButtonX;
    public JoystickButton driveControllerButtonY;
    public JoystickButton driveControllerBackButton;
    public JoystickButton driveControllerStartButton;
    public JoystickButton driveControllerLeftBumper;
    public JoystickButton driveControllerRightBumper;

    public JoystickButton coPilotControllerButtonA;
    public JoystickButton coPilotControllerButtonB;
    public JoystickButton coPilotControllerButtonX;
    public JoystickButton coPilotControllerButtonY;
    public JoystickButton coPilotControllerBackButton;
    public JoystickButton coPilotControllerStartButton;
    public JoystickButton coPilotControllerLeftBumper;
    public JoystickButton coPilotControllerRightBumper;

    // Joysticks
    /**
     * Controls Left joystick, forward/backward for Arcade Drive
     */
    public static int LeftYAxis = 1;
    public static int LeftXAxis = 2;
    public static int RightYAxis = 3;
    public static int RightXAxis = 4;

    public static int BUTTON_A = 1;
    public static int BUTTON_B = 2;
    public static int BUTTON_X = 3;
    public static int BUTTON_Y = 4;
    public static int BUTTON_LEFT_BUMPER = 5;
    public static int BUTTON_RIGHT_BUMPER = 6;
    public static int BUTTON_BACK = 7;
    public static int BUTTON_START = 8;

    public OI() {
        xboxController = new Joystick(0);

        xboxController2 = new Joystick(1);

        // Button 1 == A Button
        // Button 2 == B Button
        // Button 3 == X Button
        // Button 4 == Y Button
        // Button 5 == Left Bumper
        // Button 6 == Right Bumper
        // Button 7 == Back Button
        // Button 8 == Start Button

        driveControllerButtonA = new JoystickButton(xboxController, BUTTON_A);
        driveControllerButtonB = new JoystickButton(xboxController, BUTTON_B);
        driveControllerButtonX = new JoystickButton(xboxController, BUTTON_X);
        driveControllerButtonY = new JoystickButton(xboxController, BUTTON_Y);
        driveControllerLeftBumper = new JoystickButton(xboxController, BUTTON_LEFT_BUMPER);
        driveControllerRightBumper = new JoystickButton(xboxController, BUTTON_RIGHT_BUMPER);
        driveControllerBackButton = new JoystickButton(xboxController, BUTTON_BACK);
        driveControllerStartButton = new JoystickButton(xboxController, BUTTON_START);

        //buttons for the second controller
        coPilotControllerButtonA = new JoystickButton(xboxController2, BUTTON_A);
        coPilotControllerButtonB = new JoystickButton(xboxController2, BUTTON_B);
        coPilotControllerButtonX = new JoystickButton(xboxController2, BUTTON_X);
        coPilotControllerButtonY = new JoystickButton(xboxController2, BUTTON_Y);
        coPilotControllerLeftBumper = new JoystickButton(xboxController2, BUTTON_LEFT_BUMPER);
        coPilotControllerRightBumper = new JoystickButton(xboxController2, BUTTON_RIGHT_BUMPER);
        coPilotControllerBackButton = new JoystickButton(xboxController2, BUTTON_BACK);
        coPilotControllerStartButton = new JoystickButton(xboxController2, BUTTON_START);

        //When the "a" button is pressed the elevator rises to the next level
        coPilotControllerButtonA.whenPressed(new ElevatorLowerAuto());
        //When the y button is pressed the elevator rises to the next level
        coPilotControllerButtonY.whenPressed(new ElevatorLiftAuto());
        //When the x button is pressed the grabber closes
        coPilotControllerButtonX.whenPressed(new ManipulatorClose());
        //When the b button is pressed the grabber opens
        coPilotControllerButtonB.whenPressed(new ManipulatorOpen());
        //When the start button is pressed the arm rises
        coPilotControllerStartButton.whenPressed(new ArmRaise());
        //When the back button is pressed the arm lowers
        coPilotControllerBackButton.whenPressed(new ArmLower());

        //Turns the rumble off
        xboxController2.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
        xboxController.setRumble(GenericHID.RumbleType.kRightRumble, 0);
    }
}
