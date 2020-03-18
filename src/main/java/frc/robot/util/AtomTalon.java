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

    private int m_PID; // PID Index.
    private int m_Slot; // Slot Index
    private double m_distancePerPulse; // Distance traveled per encoder pulse.
    private int m_timeout = 10; // Timeout = 10ms.
    
    /**
     * Constructor for AtomTalon.
     * @param port of the Talon.
     * @param pidIdx - Inner (0) or Outer (1), we use inner.
     * @param slotIdx - slot to store PIDF constants (1-4).
     * @param kP
     * @param kI
     * @param kD
     * @param kF
     */
    public AtomTalon(int port, int pidIdx, int slotIdx, double kP, double kI, double kD, double kF) {
        super(port);
        super.configFactoryDefault();

        this.m_PID = pidIdx;
        this.m_Slot = slotIdx;

        super.config_kP(m_Slot, kP);
        super.config_kI(m_Slot, kI);
        super.config_kD(m_Slot, kD);
        super.config_kF(m_Slot, kF);
    }

    /**
     * Configures the Talon to use CTRE Mag Encoder as it's sensor.
     */
    public void configEncoder(Subsystem s) {
        double wheelDiameter;
        
        switch(s) {
            case DRIVETRAIN:
                wheelDiameter = Constants.DrivetrainConstants.kWheelDiameterMeters;
                super.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, m_PID, m_timeout);
                this.reset();
            default:
                // == case SHOOTER:
                wheelDiameter = 0;
                super.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, m_PID, m_timeout);
        }

        this.m_distancePerPulse = Math.PI * wheelDiameter / Constants.kEdgesPerRevolution;
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

}