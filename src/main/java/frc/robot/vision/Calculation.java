/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.vision;

import edu.wpi.first.wpilibj.trajectory.Trajectory;

/**
 * This class represents result of a vision-calculation done in the {@link VisionController} class.
 * Every calculation is a set of Velocity, Angle and Path.
 */
public class Calculation {

    private double m_velocity; // In TalonSRX Percent-Output units.
    private double m_angle; // Angle correction of the Shooter, in degrees.
    private Trajectory m_path; // Correction path of the Robot, as a Trajectory.

    /**
     * Constructor.
     * @param velocity
     * @param angle
     * @param path
     */
    public Calculation(double velocity, double angle, Trajectory path) {
        this.m_velocity = velocity;
        this.m_angle = angle;
        this.m_path = path;
    }

    /**
     * Returns the velocity needed.
     * @return velocity, in TalonSRX Percent-Output units.
     */
    public double getVelocity() {
        return this.m_velocity;
    }


    /**
     * Returns the angle-correction needed.
     * @return angle-correction, in degrees.
     */
    public double getAngle() {
        return this.m_angle;
    }

    /**
     * Returns the correction-path needed.
     * @return Correction-path, as a Trajectory.
     */
    public Trajectory getPath() {
        return this.m_path;
    }

}
