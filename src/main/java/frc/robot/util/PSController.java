/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Class for handling Playstation 4 controller as a Joystick.
 */
public class PSController extends Joystick {

    public PSController(int port){
        super(port);
    }

    public static int getSquare() {
        return 1;
    }

    public static int getTriangle() {
        return 2;
    }

    public static int getCircle() {
        return 3;
    }

    public static int getIx() {
        return 4;
    }

    public static int getR1() {
        return 6;
    }

    public static int getR2() {
        return 8;
    }

    public static int getL1() {
        return 5;
    }

    public static int getL2() {
        return 7;
    }
    
}
