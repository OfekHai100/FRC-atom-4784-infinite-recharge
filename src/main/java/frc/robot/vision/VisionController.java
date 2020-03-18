/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Vision prcoessing controller.
 */
public class VisionController {
    
    /**
     * Auxiliary enum class to define target for the camera to calculate.
     */
    enum Target {
        OUTER_PORT_DISTANCE,
        OUTER_PORT_VELOCITY,
        INNER_PORT_DISTANCE,
        INNER_PORT_VELOCITY
    }

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
     * It calculates parameters based on needed target and Shooter angle. Targets can be:
     * 1. Outer Port Distance - Calculates the distance from the Robot to Outer Port (Vision-Tape), in meters.
     * 2. Outer Port Velocity - Calculates the velocity needed to shoot Power Cell into the Outer Port, in TalonSRX Percent-Output.
     * 3. Inner Port Distance - Calculates the distance from the Robot to Outer Port (Vision-Tape), in meters.
     * 4. Inner Port Velocity - Calculates the velocity needed to shoot Power Cell into the Outer Port, in TalonSRX Percent-Output.
     * 
     * 
     * 
     * @param t
     * @param angle
     * @return
     */
    public double calculate(Target t, double angle) {
        switch(t) {
            case OUTER_PORT_DISTANCE:
                update();

        }
        
        return 0.0;
    }
}
