/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class to control the Dashboard.
 */
public class Dashboard {
    
    /**
     * Displays match data every match for the drivers.
     */
    public static void putMatchData() {
        if(DriverStation.getInstance().isFMSAttached()) {
            String alliance;
            String matchType;
            String event;
            int matchNum;
            int station;
            
            matchNum = DriverStation.getInstance().getMatchNumber();
            event = DriverStation.getInstance().getEventName();
            station = DriverStation.getInstance().getLocation();
            
            if(DriverStation.getInstance().getAlliance() == Alliance.Blue) {
                alliance = "Blue Alliance";
            } else {
                alliance = "Red Alliance";
            }

            switch(DriverStation.getInstance().getMatchType()) {
                case Qualification:
                    matchType = "QUAL";
                case Elimination:
                    matchType = "ELIM";
                default:
                    matchType = "PRCT";
            }

            SmartDashboard.putNumber(matchType, matchNum);
            SmartDashboard.putString(alliance, "Station "+station);
            SmartDashboard.putString("Competing at:", event);
        }
    }

    /**
     * Checks the color for position control (Shield Generator phase 3 only!), return the required color
     * for the ColorSensorV3 which used for the Control Panel tasks.
     * @return The required game color for the sensor.
     */
    public static String getGameColor() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        if(gameData.length() > 0) {
            switch(gameData.charAt(0)) {
                case 'B':
                    return "Red";
                case 'G':
                    return "Yellow";
                case 'R':
                    return "Blue";
                case 'Y':
                    return "Green";
            }
        }
        return null;
    }

}
