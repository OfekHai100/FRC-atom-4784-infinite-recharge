/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;

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
        m_targetYaw = m_camera.getEntry("yaw").getDouble(0);
        m_targetPitch = m_camera.getEntry("pitch").getDouble(0);
        m_targetArea = m_camera.getEntry("area").getDouble(0);
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
    
            // First, Calculate velocity.
            velocity = calculateVelocity();

            if(velocity < 0.2 || velocity > 1.0) {

            } else {
                return new Calculation(velocity, 0.0, null);
            }

        } 
        
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

}
