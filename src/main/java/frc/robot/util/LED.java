/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.robot.Constants;

/**
 * Manager class for LED strips on the Robot.
 */
public class LED {

    /**
     * Enum for Robot state.
     */
    public enum State {
        INIT,
        SHOOTER_VISION,
        AUTO,
        TELEOP
    }

    private State m_state;
    private boolean m_onVision;
    private AddressableLED m_ledStrip;
    private AddressableLED m_ledRing;
    private AddressableLEDBuffer m_ledStripBuffer;
    private AddressableLEDBuffer m_ledRingBuffer;

    /**
     * Constructor, inits all LEDs.
     */
    public LED() {
        m_ledStrip = new AddressableLED(Constants.Ports.kLEDStrip);
        m_ledRing = new AddressableLED(Constants.Ports.kLEDRing);
        m_ledStripBuffer = new AddressableLEDBuffer(40);
        m_ledRingBuffer = new AddressableLEDBuffer(10);

        m_onVision = false;
        for(var i = 0 ; i < m_ledRingBuffer.getLength() ; i++) {
            m_ledRingBuffer.setRGB(i, 0, 255, 0); // Set to green.
        }

        m_state = State.INIT;
        runStrip();
        m_ledStrip.start();
    }

    /**
     * Sets the state of the Robot.
     * @param state
     */
    public void setState(State state) {
        m_state = state;
    }

    /**
     * Method to set if the Robot is using vision or not. 
     * @param isVision
     */
    public void setIsVision(boolean isVision) {
        m_onVision = isVision;
    }

    /**
     * Return true if the Robot is using vision, false if not.
     * @return isVision
     */
    public boolean getOnVision() {
        return m_onVision;
    }

    /**
     * Runs the LED strip, based on Robot state.
     */
    public void runStrip() {
        int r, g, b;
        
        switch(m_state) {
            case INIT:
                // Light Orange
                r = 255;
                g = 153;
                b = 51;
            case SHOOTER_VISION:
                // White
                r = 255;
                g = 255;
                b = 255;
            case AUTO:
                // Green
                r = 0;
                g = 255;
                b = 0;
            default:
                // = case Teleop:    
                // Dark Red
                r = 204;
                g = 0;
                b = 0;
        }
        
        for(var i = 0 ; i < m_ledStripBuffer.getLength() ; i++) {
            m_ledStripBuffer.setRGB(i, r, g, b);
        }

        m_ledStrip.setData(m_ledStripBuffer);
    }

    /**
     * Runs the LED ring, green when using vision.
     */
    public void runRing() {
        if(m_onVision) {
            m_ledRing.start();
        } else {
            m_ledRing.stop();
        }
    }

}
