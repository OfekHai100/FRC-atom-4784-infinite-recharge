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
 * Every calculation is a set of Velocity, Angle, Distance.
 */
public class Calculation {

    private double velocity; // In TalonSRX Percent-Output units.
    private double angle; // Angle correction of the shooter, in degrees.
    private Trajectory distance; // Distance correction of the Robot, as a Trajectory.

    /**
     * Constructor.
     * @param velocity
     * @param angle
     * @param distance
     */
    public Calculation(double velocity, double angle, Trajectory distance) {
        this.velocity = velocity;
        this.angle = angle;
        this.distance = distance;
    }

    /**
     * Returns the velocity needed.
     * @return velocity, in TalonSRX Percent-Output units.
     */
    public double getVelocity() {
        return this.velocity;
    }


    /**
     * Returns the angle-correction needed.
     * @return angle-correction, in degrees.
     */
    public double getAngle() {
        return this.angle;
    }

    /**
     * Returns the distance-correction needed.
     * @return distance-correction, as a Trajectory.
     */
    public Trajectory getDistance() {
        return this.distance;
    }

}
