/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.util.Units;

import frc.robot.Constants;

/**
 * Customized, better Talon SRX.
 */
public class AtomTalon extends WPI_TalonSRX {

    /**
     * Auxiliary enum class to define Talon's closed-loop subsystem.
     */
    public enum Subsystem {
        DRIVETRAIN,
        SHOOTER
    }

    private int m_PID = 0; // PID Index - Inner (0) or Outer (1), we use inner.
    private int m_slot; // Slot Index
    private double m_distancePerPulse; // Distance traveled per encoder pulse.
    private double m_ticksPerDegree; // Encoder pulse per degree.
    private int m_timeout = 10; // Timeout = 10ms.
    
    /**
     * Constructor for AtomTalon.
     * @param port of the Talon.
     * @param slotIdx - slot to store PIDF constants (1-4).
     * @param kP
     * @param kI
     * @param kD
     * @param kF
     */
    public AtomTalon(int port, int slotIdx, double kP, double kI, double kD, double kF) {
        super(port);
        super.configFactoryDefault();

        this.m_slot = slotIdx;

        super.config_kP(m_slot, kP);
        super.config_kI(m_slot, kI);
        super.config_kD(m_slot, kD);
        super.config_kF(m_slot, kF);

        super.selectProfileSlot(m_slot, m_PID);
    }

    /**
     * Constructor for AtomTalon for specific Velocity-control.
     * @param port of the Talon.
     * @param slotIdx - slot to store PIDF constants (1-4).
     * @param kp
     */
    public AtomTalon(int port, int slotIdx, double kP) {
        this(port, slotIdx, kP, 0.0, 0.0, 0.0);
    }

    /**
     * Configures the Talon to use CTRE Mag Encoder as it's sensor.
     * @param s Subsystem that requires the encoder.
     */
    public void configEncoder(Subsystem s) {
        switch(s) {
            case DRIVETRAIN:
                super.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, m_PID, m_timeout);
                this.reset();
                this.m_distancePerPulse = Math.PI * Constants.DrivetrainConstants.kWheelDiameterMeters / Constants.kEdgesPerRevolution;
            default:
                // == case SHOOTER:
                super.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, m_PID, m_timeout);
                this.m_ticksPerDegree = Constants.kEdgesPerRevolution / (3 * 360); // (4096 / 1080) 1:3 Gearbox.
        }
    }

    /**
     * Resets encoder value to 0.
     */
    public void reset() {
        super.setSelectedSensorPosition(0);
    }

    /**
     * Convert sensor units to meters.
     * @param units
     * @return meters.
     */
    public double unitsToMeters(int units) {
        return units * this.m_distancePerPulse;
    }

    /**
     * Convert meters to sensor units.
     * @param meters
     * @return units.
     */
    public int metersToUnits(double meters) {
        return (int) (meters / this.m_distancePerPulse);
    }

    /**
     * Convert sensor units to degrees.
     * @param units
     * @return degrees.
     */
    public double unitsToDegrees(int units) {
        return units * this.m_ticksPerDegree;
    }

    /**
     * Convert degrees to sensor units.
     * @param degrees
     * @return units.
     */
    public int degreesToUnits(double degrees) {
        return (int) (degrees / this.m_ticksPerDegree);
    }

    /**
     * Get current distance traveled, in meters.
     * @return current distance traveled.
     */
    public double getDistanceMeters() {
        return super.getSelectedSensorPosition() * this.m_distancePerPulse;
    }

    /**
     * Get current distance traveled, in feet.
     * @return current distance traveled.
     */
    public double getDistanceFeet() {
        return Units.metersToFeet(getDistanceMeters());
    }

    /**
     * Get current velocity, in meters per second.
     * @return current velocity.
     */
    public double getVelocityMeters() {
        return super.getSelectedSensorVelocity() * this.m_distancePerPulse * 10.0;
    }

    /**
     * Get current velocity, in feet per second.
     * @return current velocity.
     */
    public double getVelocityFeet() {
        return Units.metersToFeet(getVelocityMeters());
    }

    /**
     * Calculates the cosine scalar needed for {@link Shooter} Arbitarty Feed-Forward.
     * @return Calculated cosine scalar.
     */
    public double calculateCoisneScalar() {
        double current = super.getSelectedSensorPosition();
        double degrees = (current - Constants.ShooterConstants.kLowestPosition) / this.m_ticksPerDegree;
        double radians = Units.degreesToRadians(degrees);
        double scalar = Math.cos(radians);

        double maxFeedForward = 0.07;
        return maxFeedForward * scalar;
    }

}