/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.vision;

import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Vision prcoessing using Limelight 2+.
 */
public class Limelight {
    
    private double tx, ty, ta, tv;

    /**
     * Constructor, sets the intial values of the Limelight.
     */
    public Limelight() {
        update();
    }

    /**
     * Updates Limelight values periodically.
     */
    private void update() {
        tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    }

    public boolean isTracked() {
        update();
        if(tv > 1.0) {
            return true;
        }
        return false;
    }
}
