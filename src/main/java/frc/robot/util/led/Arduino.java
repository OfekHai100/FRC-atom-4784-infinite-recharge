/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.led;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import frc.robot.Constants;

/**
 * This class represents an Arduino connected to RIO via I2C.
 */
public class Arduino {

    // Define variables:
    private I2C m_wire;
    private int m_adress;
    private final int kMaxBytes = 32;
    private static Arduino m_instance; // The singleton instance.

    /**
     * Constructor.
     */
    private Arduino() {
        m_adress = Constants.Ports.kArduinoI2C;
        m_wire = new I2C(Port.kOnboard, this.m_adress);
    }

    /**
     * Access to Arduino.
     * @return Arduino singleton instance.
     */
    public static Arduino getInstnace() {
        if(m_instance == null) {
            m_instance = new Arduino();
        }
        return m_instance;
    }

    /**
     * Writes something to the Arduino.
     * @param input
     */
    public void write(String input) {
        char[] charArray = input.toCharArray();
        byte[] data = new byte[charArray.length];
        for(int i = 0 ; i < charArray.length ; i++) {
            data[i] = (byte) charArray[i];
        }
        boolean failed = m_wire.transaction(data, data.length, null, 0);
        if(failed) {
            System.out.println("FAILED TO PASS TO ARDUINO: " + input);
        }
    }

    /**
     * Reads values from the Arduino.
     * @return Values read.
     */
    public String read() {
        byte[] data = new byte[kMaxBytes];
        boolean failed = m_wire.read(this.m_adress, kMaxBytes, data);
        if(!failed) {
            String returnedString = new String(data);
            int pt = returnedString.indexOf((char) 255);
            return (String) returnedString.subSequence(0, pt < 0 ? 0 : pt);
        }
        System.out.println("FAILED TO READ FROM ARDUINO!");
        return "";
    }

}
