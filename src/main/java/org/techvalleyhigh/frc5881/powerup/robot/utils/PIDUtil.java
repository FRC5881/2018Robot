package org.techvalleyhigh.frc5881.powerup.robot.utils;

public class PIDUtil {
    private double mass = 65.8; // Kilogram
    private double width = I2M(27); // Meters
    private double height = I2M(53); // Meters
    private double depth = I2M(31); // Meters

    private double momentOfInertia = 1/6 * mass * width * width;

    private double gear_left = 10.57; // Gear ratio left side
    private double gear_right = 10.57; // Gear ratio right side
    private double wheel_radius = I2M(6); // Drive train wheel radius meters

    // TODO: Calculate kT, kV and R
    private double k_torque = 1; // Torque constant on drive train motors
    private double k_voltage = 1; // Voltage constant on drive train motors
    private double resistance = 1; // Resistance of motor

    // Constant depended on properties of motors
    private final double C1 = - gear_left * gear_left * k_torque / (k_voltage * resistance * wheel_radius * wheel_radius);
    private final double C2 = gear_left * k_torque / (resistance * wheel_radius);
    private final double C3 = - gear_right * gear_right * k_torque / (k_voltage * resistance * wheel_radius * wheel_radius);
    private final double C4 = gear_right * k_torque / (resistance * wheel_radius);

    /**
     * Force by left side
     * @param velocity_left current velocity left side
     * @param voltage_left voltage to apply
     * @return Force Newtons
     */
    private double force_left(double velocity_left, double voltage_left) {
        return C1 * velocity_left + C2 * voltage_left;
    }

    /**
     * Force by right side
     * @param velocity_right current velocity right side
     * @param voltage_right voltage to apply
     * @return Force Newtons
     */
    private double force_right(double velocity_right, double voltage_right) {
        return C3 * velocity_right + C4 * voltage_right;
    }

    /**
     * Converts inches to meters
     * @param inches double inches to convert
     * @return double meters
     */
    private static double I2M(double inches) {
        return 0.0254 * inches;
    }
}
