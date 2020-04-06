/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.hardware;

import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.geometry.Rotation2d;

/**
 * Customized, better {@link PigeonIMU}
 */
public class AtomPigeon extends PigeonIMU {

    private boolean m_reversed;

    public AtomPigeon(AtomTalon master, boolean reversed) {
        super(master);
        m_reversed = reversed;
        super.configFactoryDefault();
    }

    public AtomPigeon(int canID, boolean reversed) {
        super(canID);
        m_reversed = reversed;
        super.configFactoryDefault();
    }

    /**
     * Returns the Yaw, Pitch and Roll values - as an array.
     * @return Yaw, Pitch and Roll.
     */
    public double[] getYPR() {
        double[] ypr = new double[3];
        super.getYawPitchRoll(ypr);
        return ypr;
    }

    /**
     * Returns the Yaw value.
     * @return Yaw
     */
    public double getYaw() {
        return getYPR()[0];
    }

    /**
     * Returns the Pitch value.
     * @return Pitch
     */
    public double getPitch() {
        return getYPR()[1];
    }
    
    /**
     * Returns the Roll value.
     * @return Roll
     */
    public double getRoll() {
        return getYPR()[2];
    }

    /**
     * Returns the heading, between -180 to 180 degrees.
     * @return Heading
     */
    public double getHeading() {
        return Math.IEEEremainder(getYaw(), 360) * (m_reversed ? -1.0 : 1.0);
    }

    /**
     * Returns the heading in radians, between -PI to PI.
     * @return Heading in radians.
     */
    public double getInRadians() {
        return Math.toRadians(getHeading());
    }

    /**
     * Returns the heading as a {@link Rotation2d} object.
     * @return Heading as a {@link Rotation2d}.
     */
    public Rotation2d getAsRotation2d() {
        return Rotation2d.fromDegrees(getHeading());
    }

    /**
     * Returns the orientation of the {@link PigeonIMU}.
     * @return Orientation
     */
    public boolean isReversed() {
        return m_reversed;
    }

    /**
     * Zeroes the {@link PigeonIMU}.
     */
    public void zero() {
        super.setYaw(0);
    }

    /**
     * Calibrates the {@link PigeonIMU}.
     */
    public void calibrate() {
        super.enterCalibrationMode(CalibrationMode.BootTareGyroAccel);
    }

}
