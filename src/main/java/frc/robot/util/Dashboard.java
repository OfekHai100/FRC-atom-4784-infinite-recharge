/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.pathing.PathManager.Path;

/**
 * Dashboard++.
 */
public class Dashboard {

    // Path chooser:
    private static SendableChooser<Path> m_chooser = new SendableChooser<Path>();

    /**
     * Displays the {@link Path} chooser at the begining of every match.
     */
    public static void putChooser() {
        SmartDashboard.putData(m_chooser);
        m_chooser.addOption("Red Port", Path.RED_PORT);
        m_chooser.addOption("Red Middle", Path.RED_MIDDLE);
        m_chooser.addOption("Red Blue-Feeder", Path.RED_BLUE_FEEDER);
        m_chooser.addOption("Blue Port", Path.BLUE_PORT);
        m_chooser.addOption("Blue Middle", Path.BLUE_MIDDLE);
        m_chooser.addOption("Blue Red-Feeder", Path.BLUE_RED_FEEDER);
    }

    /**
     * Returns the selected path for autonomous routine.
     * @return Selected path.
     */
    public static Path getSelectedPath() {
        return m_chooser.getSelected();
    }

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
     * for the ColorSensorV3 which is used for the Control Panel tasks.
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
