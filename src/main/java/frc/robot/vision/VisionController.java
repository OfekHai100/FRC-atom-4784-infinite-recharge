/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.vision;

import java.util.List;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

import frc.robot.Constants;
import frc.robot.pathing.Configuration;

/**
 * Vision prcoessing controller.
 */
public class VisionController {
    
    // Vision processing varaibles.
    private double m_targetYaw, m_targetPitch, m_targetArea;
    private boolean m_targetValid;

    private NetworkTableInstance m_table;
    private NetworkTable m_camera;

    /**
     * This enum represents the required target for the Path component in a {@link Calculation} object.
     * Target can be: further from the Power Port or closer to it.  
     */
    enum Target {
        FURTHER,
        CLOSER
    }

    /**
     * Constructor.
     */
    public VisionController() {
        m_table = NetworkTableInstance.getDefault();
        
        m_camera = m_table.getTable("chameleon-vision").getSubTable("VisionCamera");
        
        update();
    }

    /**
     * This method updates retrieved camera values when called.
     */
    private void update() {
        m_targetYaw = m_camera.getEntry("yaw").getDouble(0.0);
        m_targetPitch = m_camera.getEntry("pitch").getDouble(0.0);
        m_targetArea = m_camera.getEntry("area").getDouble(0.0);
        m_targetValid = m_camera.getEntry("isValid").getBoolean(false);
    }

    /**
     * Returns true if a valid target found.
     * @return true if valid, false else.
     */
    public boolean isValid() {
        update();
        return m_targetValid;
    }

    /**
     * Returns the targetPitch (Y-Offset) value.
     * @return targetPitch.
     */
    public double getPitch() {
        update();
        return this.m_targetPitch;
    }

    /**
     * Returns the targetYaw (X-Offset) value.
     * @return targetYaw.
     */
    public double getYaw() {
        update();
        return this.m_targetYaw;
    }

    /**
     * Returns the targetArea value.
     * @return targetArea.
     */
    public double getArea() {
        update();
        return this.m_targetArea;
    }

    /**
     * This is the main method for the controller:
     * It calculates parameters based on Shooter angle and Robot position.
     * 
     * @param alpha - Current angle of Robot shooter, in degrees.
     * @param position - Current position of the Robot, as a {@link Pose2d} object.
     * @return Result as a {@link Calculation} object. 
     */
    public Calculation calculate(double alpha, Pose2d position) {
        
        // Define variables:
        double velocity;
        double angle;
        Trajectory path;

        if(isValid()) {
            // First, calculate Velocity and Angle:
            velocity = calculateVelocity();
            angle = calculateAngle();
            if(!inRange(alpha, angle)) {
                // Angle correction is needed:
                if(angle < 31.4 || angle > 81.2) {
                    // Position correction is needed:
                    if(angle < 31.4) {
                        // We need to get closer to the target:
                        path = calculatePath(position, Target.CLOSER);
                        angle = calculateAngle();
                        velocity = calculateVelocity();
                        return new Calculation(velocity, angle, path);
                    } else {
                        // We need to get further from the target.
                        path = calculatePath(position, Target.FURTHER);
                        angle = calculateAngle();
                        velocity = calculateVelocity();
                        return new Calculation(velocity, angle, path);
                    }
                } else {
                    // No Position correction is needed, only Velocity and Angle-Correction:
                    return new Calculation(velocity, angle, null);
                }
            } else {
                if(velocity < 0.2 || velocity > 1) {
                    // Position correction is needed:
                    if(velocity < 0.2) {
                        // We need to get further from the target.
                        path = calculatePath(position, Target.FURTHER);
                        angle = calculateAngle();
                        velocity = calculateVelocity();
                        return new Calculation(velocity, angle, path);
                    } else {
                        // We need to get closer to the target
                        path = calculatePath(position, Target.CLOSER);
                        angle = calculateAngle();
                        velocity = calculateVelocity();
                        return new Calculation(velocity, angle, path);
                    }
                } else {
                    // No Position or Angle correction is needed, only Velocity:
                    return new Calculation(velocity, 0.0, null);
                }
            }

        } 
        
        // No valid target has been found, therefore can not calculate!
        return new Calculation(0.0, 0.0, null);
    }

    /**
     * Method to calculate the Velocity parameter of a {@link Calculation}.
     * @return Velocity of the Shooter motor, in TalonSRX Percent-Output units.
     */
    private double calculateVelocity() {
        double velocity;
        update();
        // Fake Function!
        velocity = 0.0606 * m_targetPitch * m_targetPitch - 4E-15 * m_targetPitch + 5.9545;
        return velocity;
    }

    /**
     * Method to calculate the Angle parameter of a {@link Calculation}.
     * @return Angle-Correction of the Shooter, in degrees.
     */
    private double calculateAngle() {
        double angle;
        update();
        // Fake Function!
        angle = 0.6556 * m_targetPitch * m_targetPitch * m_targetPitch - 6.3111 * m_targetPitch * m_targetPitch + 20.789 * m_targetPitch + 16.867;
        return angle;
    }

    /**
     * This method checks if the calculated angle is in range with the current angle, in order to determine
     * if angle-correction is needed for the final calculation. Range is an offset of plus or minus 5 degrees.
     * @param alpha the current angle, in degrees.
     * @param beta the calculated angle, in degrees.
     * @return True if in range, false if not.
     */
    private boolean inRange(double alpha, double beta) {
        if(beta >= alpha-5.0 && beta <= alpha+5.0) {
            return true;
        }
        return false;
    }

    /**
     * This method finds the needed {@link Trajectory} for the position-correction component of a {@link Calculation}.
     * It starts by creating configuration for the path, then finds it's waypoint and endpoint and in the end returns final path.
     * @param position of the Robot.
     * @param target - A {@link Target} is what the path is based on. A target can be a path that drives CLOSER to the Power Port,
     *  or FURTHER from it.
     * @return Path-Correction, as a {@link Trajectory} object.
     */
    private Trajectory calculatePath(Pose2d position, Target target) {
        
        Trajectory path;
        Configuration config = Configuration.getConfiguration();
        Alliance side = DriverStation.getInstance().getAlliance();

        TrajectoryConfig trajectoryConfig = config.getConfig();

        // Find interior waypoint and endpoint:
        if(target == Target.CLOSER) {
            Pose2d endpoint;
            if(side == Alliance.Blue) {
                endpoint = Constants.FieldConstants.kBluePowerPort; // Blue Power Port coordinates.
            } else {
                endpoint = Constants.FieldConstants.kRedPowerPort; // Red Power Port coordinates.

            }

            Translation2d waypoint = calculateWaypoint(position, endpoint);

            path = TrajectoryGenerator.generateTrajectory(
                position, 
                List.of(
                    waypoint
                ), 
                endpoint, 
                trajectoryConfig
            );

        } else {
            // == FURTHER
            double x, y, omega;

            if(side == Alliance.Blue) {
                x = position.getTranslation().getX() - (Constants.FieldConstants.kBluePowerPort.getTranslation().getX() - position.getTranslation().getX());
                y = (Constants.FieldConstants.kBluePowerPort.getTranslation().getY() + position.getTranslation().getY()) / 2.0;
                omega = 1.0;
            } else {
                x = position.getTranslation().getX() - (position.getTranslation().getX() - Constants.FieldConstants.kRedPowerPort.getTranslation().getX());
                y = (Constants.FieldConstants.kRedPowerPort.getTranslation().getY() + position.getTranslation().getY()) / 2.0;
                omega = -1.0;
            }
            
            Pose2d endpoint = new Pose2d(new Translation2d(x, y), new Rotation2d(omega, 0));
            Translation2d waypoint = calculateWaypoint(position, endpoint);

            path = TrajectoryGenerator.generateTrajectory(
                position, 
                List.of(
                    waypoint
                ), 
                endpoint, 
                trajectoryConfig
            );
        }

        // In the end, return final path.
        return path;

    }

    /**
     * This method calculates waypoint between initpoint and endpoint in a position-correction path. 
     * This is a simple average calculation.
     * @param initpoint Current position of the Robot, initpoint, as a {@link Pose2d}.
     * @param endpoint Endpoint of the path, as a {@link Pose2d}.
     * @return Calculated waypoint, as a {@link Translation2d}.
     */
    private Translation2d calculateWaypoint(Pose2d initpoint, Pose2d endpoint) {
        double x, y;
        x = (endpoint.getTranslation().getX() + initpoint.getTranslation().getX()) / 2.0;
        y = (endpoint.getTranslation().getY() + initpoint.getTranslation().getY()) / 2.0;
        return new Translation2d(x, y);
    }

}
