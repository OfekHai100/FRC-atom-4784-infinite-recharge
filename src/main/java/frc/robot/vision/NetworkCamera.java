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
 * This class represents a Camera used for vision processing, and transmits data to the roboRIO through NetworkTables.
 */
public class NetworkCamera {

    // Vision processing variables:
    private double m_targetYaw, m_targetPitch, m_targetArea;
    private boolean m_targetValid;

    // Network tables:
    private NetworkTableInstance m_table;
    private NetworkTable m_camera;

    // The singleton instance:
    private static NetworkCamera m_instance;

    /**
     * Constructor
     */
    private NetworkCamera() {
        m_table = NetworkTableInstance.getDefault();
        m_camera = m_table.getTable("chameleon-vision").getSubTable("VisionCamera");
    }

    /**
     * Access to the camera.
     * @return NetworkCamera singleton instance.
     */
    public static NetworkCamera getInstance() {
        if(m_instance == null) {
            m_instance = new NetworkCamera();
        }
        return m_instance;
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

}
