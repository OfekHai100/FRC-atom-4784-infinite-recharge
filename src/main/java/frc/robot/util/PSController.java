/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Add your docs here.
 */
public class PSController extends Joystick {

    public PSController(int port){
        super(port);
    }

    public boolean getIx() {
        return super.getRawButton(1);
    }

    public boolean getCircle() {
        return super.getRawButton(2);
    }

    public boolean getTriangle() {
        return super.getRawButton(3);
    }

    public boolean getSquare() {
        return super.getRawButton(4);
    }

    public boolean getR1() {
        return super.getRawButton(10);
    }

    public boolean getR2() {
        return super.getRawButton(12);
    }

    public boolean getL1() {
        return super.getRawButton(11);
    }

    public boolean getL2() {
        return super.getRawButton(13);
    }
    
}
