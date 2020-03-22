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
     * Enum for current Robot state, based on action or match period.
     */
    public enum State {
        INIT,
        SHOOTER_VISION,
        SHOOTER_TRENCH,
        SHOOTER_PORT,
        SHOOTER_SIMPLE,
        AUTO,
        TELEOP
    }

    private State m_state;
    private boolean m_onVision;
    private boolean m_switched;
    private boolean m_passed;
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
        m_ledRing.setData(m_ledRingBuffer);

        m_state = State.INIT;
        m_switched = true;
        m_ledStrip.start();
    }

    /**
     * Sets the state of the Robot.
     * @param state
     */
    public void setState(State state) {
        m_state = state;
        m_switched = true;
    }

    /**
     * Method to set if the Robot is using vision or not. 
     * @param isVision
     */
    public void setIsVision(boolean isVision) {
        m_onVision = isVision;
        m_passed = false;
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
            case SHOOTER_TRENCH:
                // Yellow
                r = 255;
                g = 255;
                b = 0;
            case SHOOTER_PORT:
                // Purple
                r = 153;
                g = 0;
                b = 153;
            case SHOOTER_SIMPLE:
                // Light Blue
                r = 51;
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
        
        if(m_switched) {
            m_switched = false;
            for(var i = 0 ; i < m_ledStripBuffer.getLength() ; i++) {
                m_ledStripBuffer.setRGB(i, r, g, b);
            }
            m_ledStrip.setData(m_ledStripBuffer);
        }
    }

    /**
     * Runs the LED ring, green when using vision.
     */
    public void runRing() {
        if(m_onVision && !m_passed) {
            m_ledRing.start();
            m_passed = true;
        } else if(!m_passed) {
            m_ledRing.stop();
            m_passed = true;
        }
    }

}
