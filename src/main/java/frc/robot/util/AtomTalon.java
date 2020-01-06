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

    public void configEncoder() {
        super.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, m_PID, 10);
        this.reset();
        this.m_distancePerPulse = Math.PI * Constants.kWheelDiameterInches / Constants.kUPR;
    }

    public void reset() {
        super.setSelectedSensorPosition(0);
    }

    public double unitsToInches(int units) {
        return units * this.m_distancePerPulse;
    }

    public int inchesToUnits(double inches) {
        return (int) (inches / this.m_distancePerPulse);
    }
    
    public double getDistanceInches() {
        return super.getSelectedSensorPosition() * this.m_distancePerPulse;
    }

    public double getVelocityInches() {
        return super.getSelectedSensorVelocity() * this.m_distancePerPulse * 10.0;
    }
    // sasi
}
