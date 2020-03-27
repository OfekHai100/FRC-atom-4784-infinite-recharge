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
    
    /**
     * This enum represents the required target for the position-correction component in a {@link Calculation} object.
     * Target can be: further from the Power Port or closer to it.  
     */
    private enum Target {
        FURTHER,
        CLOSER
    }

    // Vision processing varaibles.
    private double m_targetYaw, m_targetPitch, m_targetArea;
    private boolean m_targetValid;

    private NetworkTableInstance m_table;
    private NetworkTable m_camera;

    // Used in the calculation methods.
    private final double kMinVelocity = Constants.ShooterConstants.kMinVelocity;
    private final double kMaxVelocity = Constants.ShooterConstants.kMaxVelocity;
    private final double kMinAngle = Constants.ShooterConstants.kMinAngle;
    private final double kMaxAngle = Constants.ShooterConstants.kMaxAngle;

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
     * @param x - Current position of the Robot, as a {@link Pose2d} object.
     * @return Result as a {@link Calculation} object. 
     */
    public Calculation calculate(double alpha, Pose2d x) {
        
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
                if(!checkAngle(angle)) {
                    // Position correction is needed:
                    if(angle < kMinAngle) {
                        // We need to get closer to the target:
                        path = calculatePath(x, Target.CLOSER);
                        angle = calculateAndCheckAngle();
                        velocity = calculateAndCheckVelocity();
                        return new Calculation(velocity, angle, path);
                    } else {
                        // We need to get further from the target.
                        path = calculatePath(x, Target.FURTHER);
                        angle = calculateAndCheckAngle();
                        velocity = calculateAndCheckVelocity();
                        return new Calculation(velocity, angle, path);
                    }
                } else {
                    // No Position correction is needed, only Velocity and Angle-Correction:
                    return new Calculation(velocity, angle, null);
                }
            } else {
                if(!checkVelocity(velocity)) {
                    // Position correction is needed:
                    if(velocity < kMinVelocity) {
                        // We need to get further from the target.
                        path = calculatePath(x, Target.FURTHER);
                        angle = calculateAndCheckAngle();
                        velocity = calculateAndCheckVelocity();
                        return new Calculation(velocity, angle, path);
                    } else {
                        // We need to get closer to the target
                        path = calculatePath(x, Target.CLOSER);
                        angle = calculateAndCheckAngle();
                        velocity = calculateAndCheckVelocity();
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
        // Fake Function!
        velocity = 0.0061 * m_targetPitch * m_targetPitch - (4E-16) * m_targetPitch + 0.5955;
        return velocity;
    }

    /**
     * Checks if the calculated velocity is between the min and the max values determined.
     * @param velocity as calculated
     * @return True if in the range, false if not.
     */
    private boolean checkVelocity(double velocity) {
        if(velocity >= kMinVelocity && velocity <= kMaxVelocity) {
            return true;
        }
        return false;
    }

    /**
     * Calculates the Velocity component of a {@link Calculation}, and then checks if it is between the min and max values.
     * If so, the method returns the calculated velocity, and if not, it returns an average tested output.
     * @return Calculated velocity, or an average tested output in case the calculated is not in range of the min and max values.
     */
    private double calculateAndCheckVelocity() {
        double calculated = calculateVelocity();
        if(checkVelocity(calculated)) {
            return calculated;
        }
        return 0.8; // This output has been tested to give just-fine results.
    }

    /**
     * Method to calculate the Angle parameter of a {@link Calculation}.
     * @return Angle-Correction of the Shooter, in degrees.
     */
    private double calculateAngle() {
        double angle;
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
     * Checks if the calculated angle is between the min and the max values determined.
     * @param angle as calculated
     * @return True if in the range, false if not.
     */
    private boolean checkAngle(double angle) {
        if(angle >= kMinAngle && angle <= kMaxAngle) {
            return true;
        }
        return false;
    }

    /**
     * Calculates the Angle component of a {@link Calculation}, and then checks if it is between the min and max values.
     * If so, the method returns the calculated angle, and if not, it returns an average tested angle.
     * @return Calculated angle, or an average tested angle in case the calculated is not in range of the min and max values.
     */
    private double calculateAndCheckAngle() {
        double calculated = calculateAngle();
        if(checkAngle(calculated)) {
            return calculated;
        }
        return 45.0; // This angle has been tested to give just-fine results.
    }

    /**
     * This method finds the needed {@link Trajectory} for the position-correction component of a {@link Calculation}.
     * It starts by creating configuration for the path, then finds it's waypoint and endpoint and in the end returns final path.
     * @param position of the Robot.
     * @param target - A {@link Target} is what the path is based on. A target can be a path that drives CLOSER to the Power Port,
     *  or FURTHER from it.
     * @return Position-Correction, as a {@link Trajectory} object.
     */
    private Trajectory calculatePath(Pose2d position, Target target) {
        
        Trajectory path;
        Alliance side = DriverStation.getInstance().getAlliance();

        // Find interior waypoint and endpoint:
        if(target == Target.CLOSER) {
            double x, y;
            Rotation2d omega;
            
            if(side == Alliance.Blue) {
                x = (Constants.FieldConstants.kBluePowerPort.getTranslation().getX() - position.getTranslation().getX()) / 2.0;
                y = (Constants.FieldConstants.kBluePowerPort.getTranslation().getY() + position.getTranslation().getY()) / 2.0;
                omega = position.getRotation().minus(new Rotation2d(1, 0));
            } else {
                x = (position.getTranslation().getX() - Constants.FieldConstants.kRedPowerPort.getTranslation().getX()) / 2.0;
                y = (Constants.FieldConstants.kRedPowerPort.getTranslation().getY() + position.getTranslation().getY()) / 2.0;
                omega = position.getRotation().minus(new Rotation2d(-1, 0));
            }

            Pose2d endpoint = new Pose2d(x, y, omega);
            Translation2d waypoint = calculateWaypoint(position, endpoint);

            TrajectoryConfig config = Configuration.getConfiguration(false).getConfig();

            path = TrajectoryGenerator.generateTrajectory(
                position, 
                List.of(
                    waypoint
                ), 
                endpoint, 
                config
            );

        } else {
            // == FURTHER
            double x, y;
            Rotation2d omega;

            if(side == Alliance.Blue) {
                x = position.getTranslation().getX() - ((Constants.FieldConstants.kBluePowerPort.getTranslation().getX() - position.getTranslation().getX()) / 2.0);
                y = (Constants.FieldConstants.kBluePowerPort.getTranslation().getY() + position.getTranslation().getY()) / 2.0;
                omega = position.getRotation().minus(new Rotation2d(1, 0));
            } else {
                x = position.getTranslation().getX() - ((position.getTranslation().getX() - Constants.FieldConstants.kRedPowerPort.getTranslation().getX()) / 2.0);
                y = (Constants.FieldConstants.kRedPowerPort.getTranslation().getY() + position.getTranslation().getY()) / 2.0;
                omega = position.getRotation().minus(new Rotation2d(-1, 0));
            }
            
            Pose2d endpoint = new Pose2d(new Translation2d(x, y), omega);
            Translation2d waypoint = calculateWaypoint(position, endpoint);
            
            TrajectoryConfig config = Configuration.getConfiguration(true).getConfig();

            path = TrajectoryGenerator.generateTrajectory(
                position, 
                List.of(
                    waypoint
                ), 
                endpoint, 
                config
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
