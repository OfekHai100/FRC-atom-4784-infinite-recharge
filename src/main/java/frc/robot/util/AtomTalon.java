/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.Constants;

/**
 * Customized, better Talon SRX.
 */
public class AtomTalon extends WPI_TalonSRX {

    private int m_PID;
    private int m_Slot;
    private double m_distancePerPulse;

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
    public void configEncoder() {
        super.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, m_PID, 10);
        this.reset();
        this.m_distancePerPulse = Math.PI * Constants.DrivetrainConstants.kWheelDiameterInches / Constants.DrivetrainConstants.kUPR; // INCORRECT!
    }

    /**
     * Resets encoder value to 0.
     */
    public void reset() {
        super.setSelectedSensorPosition(0);
    }

    /**
     * Convert sensor units to inches.
     * @param units
     * @return inches.
     */
    public double unitsToInches(int units) {
        return units * this.m_distancePerPulse;
    }

    /**
     * Convert inches to sensor units.
     * @param inches
     * @return units.
     */
    public int inchesToUnits(double inches) {
        return (int) (inches / this.m_distancePerPulse);
    }
    
    /**
     * Get current distance traveled, in inches.
     * @return current distance traveled.
     */
    public double getDistanceInches() {
        return super.getSelectedSensorPosition() * this.m_distancePerPulse;
    }

    /**
     * Get current velocity, in inches per second.
     * @return current velocity.
     */
    public double getVelocityInches() {
        return super.getSelectedSensorVelocity() * this.m_distancePerPulse * 10.0;
    }

}