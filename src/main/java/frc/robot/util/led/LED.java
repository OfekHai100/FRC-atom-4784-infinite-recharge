/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.led;

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
        INIT(255, 153 ,51), // Light Orange
        SHOOTER_VISION(255, 255, 255), // White
        SHOOTER_TRENCH(255, 255, 0), // Yellow
        SHOOTER_PORT(153, 0, 153), // Purple
        SHOOTER_SIMPLE(51, 255, 255), // Light Blue
        AUTO(0, 255, 0), // Green
        TELEOP(204, 0, 0); // Dark Red

        private int m_red, m_green, m_blue;

        State(int r, int g, int b) {
            m_red = r;
            m_green = g;
            m_blue = b;
        }

        public int getR() {
            return this.m_red;
        }

        public int getG() {
            return this.m_green;
        }

        public int getB() {
            return this.m_blue;
        }
    }

    private State m_state;
    private AddressableLED m_ledStrip = new AddressableLED(Constants.Ports.kLEDStrip);
    private AddressableLEDBuffer m_ledStripBuffer = new AddressableLEDBuffer(40);

    private boolean m_onVision;
    private boolean m_switched;
    private boolean m_passed;

    /**
     * Constructor, inits all LEDs.
     */
    public LED() {
        m_state = State.INIT;
        m_switched = true;
        m_onVision = false;
        m_passed = true;
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
     * Activates or disables the vision mode on the Robot.
     * @param activate - True to activate, false to disable.
     */
    public void activateVision(boolean activate) {
        m_onVision = activate;
        m_passed = false;
    }

    /**
     * Return true if the Robot is using vision, false if not.
     * @return onVision
     */
    public boolean isVisionActivated() {
        return m_onVision;
    }

    public void runRing() {
        if(!m_passed && m_onVision) {
            Arduino.getInstnace().write("OPEN");
            m_passed = true;
        } else if(!m_passed) {
            Arduino.getInstnace().write("SHUT");
            m_passed = true;
        } 
    }

    /**
     * Runs the LED strip, based on Robot state.
     */
    public void runStrip() {        
        if(m_switched) {
            m_switched = false;
            for(var i = 0 ; i < m_ledStripBuffer.getLength() ; i++) {
                m_ledStripBuffer.setRGB(i, m_state.getR(), m_state.getG(), m_state.getB());
            }
            m_ledStrip.setData(m_ledStripBuffer);
        }
    }

}
