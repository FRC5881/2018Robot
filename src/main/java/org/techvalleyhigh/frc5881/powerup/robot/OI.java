package org.techvalleyhigh.frc5881.powerup.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ManipulatorClose;
import org.techvalleyhigh.frc5881.powerup.robot.commands.arm.ManipulatorOpen;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorLift;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorLiftAuto;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorLower;
import org.techvalleyhigh.frc5881.powerup.robot.commands.elevator.ElevatorLowerAuto;

/**
 * Controls operator interfaces, such as controllers
 */
public class OI {
    public GenericHID xboxController;

    public GenericHID xboxController2;

    /**
     * a button is toggle on/off for intake
     */
    public JoystickButton driveControllerButtonA;

    /**
     * b button is increase speed of shooter
     */
    public JoystickButton driveControllerButtonB;

    /**
     * x button is decrease speed of shooter
     */
    public JoystickButton driveControllerButtonX;

    /**
     * y button is being used for climber. Hold down y to climb
     */
    public JoystickButton driveControllerButtonY;

    /**
     * back button is not being used as of 2/19
     */
    public JoystickButton driveControllerBackButton;

    /**
     * start button is not being used as of 2/19
     */
    public JoystickButton driveControllerStartButton;

    /**
     * left bumper is not being used as og 2/19
     */
    public JoystickButton driveControllerLeftBumper;

    /**
     * right bumper is toggle on/off for shooter
     */
    public JoystickButton driveControllerRightBumper;



    public JoystickButton coPilotControllerButtonA;

    /**
     * b button is increase speed of shooter
     */
    public JoystickButton coPilotControllerButtonB;

    /**
     * x button is decrease speed of shooter
     */
    public JoystickButton coPilotControllerButtonX;

    /**
     * y button is being used for climber. Hold down y to climb
     */
    public JoystickButton coPilotControllerButtonY;

    /**
     * back button is not being used as of 2/19
     */
    public JoystickButton coPilotControllerBackButton;

    /**
     * start button is not being used as of 2/19
     */
    public JoystickButton coPilotControllerStartButton;

    /**
     * left bumper is not being used as og 2/19
     */
    public JoystickButton coPilotControllerLeftBumper;

    /**
     * right bumper is toggle on/off for shooter
     */
    public JoystickButton coPilotControllerRightBumper;

    /**
     * Controls Left joystick, forward/backward for Arcade Drive
     */
    //TODO: figure out what number corresponds to the triggers on the controller
    public static int LeftYAxis = 1;
    /**
     * Controls right joystick, Turning For Arcade Drive
     */
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
        // Button 5 == driveControllerLeftBumper
        // Button 6 == driveControllerRightBumper
        // Button 7 == driveControllerBackButton
        // Button 8 == driveControllerStartButton


        driveControllerButtonA = new JoystickButton(xboxController, BUTTON_A);
        driveControllerButtonB = new JoystickButton(xboxController, BUTTON_B);
        driveControllerButtonX = new JoystickButton(xboxController, BUTTON_X);
        driveControllerButtonY = new JoystickButton(xboxController, BUTTON_Y);
        driveControllerLeftBumper = new JoystickButton(xboxController, BUTTON_LEFT_BUMPER);
        driveControllerRightBumper = new JoystickButton(xboxController, BUTTON_RIGHT_BUMPER);
        driveControllerBackButton = new JoystickButton(xboxController, BUTTON_BACK);
        driveControllerStartButton = new JoystickButton(xboxController, BUTTON_START);


        coPilotControllerButtonA = new JoystickButton(xboxController2, BUTTON_A);
        coPilotControllerButtonB = new JoystickButton(xboxController2, BUTTON_B);
        coPilotControllerButtonX = new JoystickButton(xboxController2, BUTTON_X);
        coPilotControllerButtonY = new JoystickButton(xboxController2, BUTTON_Y);
        coPilotControllerLeftBumper = new JoystickButton(xboxController2, BUTTON_LEFT_BUMPER);
        coPilotControllerRightBumper = new JoystickButton(xboxController2, BUTTON_RIGHT_BUMPER);
        coPilotControllerBackButton = new JoystickButton(xboxController2, BUTTON_BACK);
        coPilotControllerStartButton = new JoystickButton(xboxController2, BUTTON_START);

        //When the Left bumper is pressed the elevator descends manually
        coPilotControllerLeftBumper.whileHeld(new ElevatorLower());
        //When the "a" button is pressed the elevator rises to the next level
        coPilotControllerButtonA.whenPressed(new ElevatorLowerAuto());
        //When the Right bumper is held down the elevator rises manually
        coPilotControllerRightBumper.whileHeld(new ElevatorLift());
        //When the "b" button is pressed the elevator rises to the next level
        coPilotControllerButtonB.whenPressed(new ElevatorLiftAuto());
        //When the start button is pressed the grabber closes
        coPilotControllerStartButton.whenPressed(new ManipulatorClose());
        //When the back button is pressed the grabber opens
        coPilotControllerBackButton.whenPressed(new ManipulatorOpen());

        xboxController2.setRumble(GenericHID.RumbleType.kLeftRumble, 0);
    }
}
