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

    /**
     * Constructor for PSController
     * @param port
     */
    public PSController(int port){
        super(port);
    }

    /**
     * Get the Square button value.
     * @return Square button value
     */
    public static int getSquare() {
        return 1;
    }

    /**
     * Get the Triangle button value.
     * @return Triangle button value
     */
    public static int getTriangle() {
        return 2;
    }

    /**
     * Get the Circle button value.
     * @return Circlea button value
     */
    public static int getCircle() {
        return 3;
    }

    /**
     * Get the X button value.
     * @return X button value
     */
    public static int getIx() {
        return 4;
    }

    /**
     * Get the R1 button value.
     * @return R1 button value
     */
    public static int getR1() {
        return 6;
    }

    /**
     * Get the R2 button value.
     * @return R2 button value
     */
    public static int getR2() {
        return 8;
    }

    /**
     * Get the L1 button value.
     * @return L1 button value
     */
    public static int getL1() {
        return 5;
    }

    /**
     * Get the L2 button value.
     * @return L2 button value
     */
    public static int getL2() {
        return 7;
    }

    /**
     * Get the Right button value.
     * @return Right button value
     */
    public static int getRight() {
        return 9;
    }

    /**
     * Get the Left button value.
     * @return Left button value
     */
    public static int getLeft() {
        return 11;
    }

    /**
     * Get the Up button value.
     * @return Up button value
     */
    public static int getUp() {
        return 10;
    }

    /**
     * Get the Down button value.
     * @return Down button value
     */
    public static int getDown() {
        return 12;
    }
    
}