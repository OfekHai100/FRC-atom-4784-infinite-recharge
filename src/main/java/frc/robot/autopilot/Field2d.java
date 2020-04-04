/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.autopilot;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import frc.robot.subsystems.Drivetrain;

/**
 * Represents 2-dimensional field-map of the 2020 FIRST Robotics Competition season: Infinite Recharge, used for {@link AutoPilot}.
 */
public class Field2d {

    /**
     * This enum represents all Zones of the Field.
     */
    enum Zone {
        INITIATION_LINE_BLUE,
        INITIATION_LINE_RED,
        RENDEVOUS_POINT,
        SECTOR_BLUE,
        SECTOR_RED,
        TRENCH_RUN_BLUE,
        TRENCH_RUN_RED,
        INVALID
    }

    private Zone m_currentZone;
    private final Drivetrain m_drive;

    // The singleton instance:
    private static Field2d m_instance;

    /**
     * Constructor.
     */
    private Field2d() {
        m_drive = Drivetrain.getInstance();
    }

    /**
     * Access to Field2d.
     * @return The Field2d singleton instance.
     */
    public static Field2d getInstance() {
        if(m_instance == null) {
            m_instance = new Field2d();
        }
        return m_instance;
    }

    /**
     * Returns the {@link Zone} of a position ({@link Pose2d}).
     * @param position
     * @return The Zone in which the position is in the field.
     */
    public Zone getPositionZone(Pose2d position) {   
        double x = position.getTranslation().getX();
        double y = position.getTranslation().getY();
        Zone zone;

        if(x == 3.114) {
            zone = Zone.INITIATION_LINE_RED;
        } else if(x == 12.894) {
            zone = Zone.INITIATION_LINE_BLUE;
        } else if(x > 5.304 && x < 10.705 && y >= -1.394) {
            zone = Zone.TRENCH_RUN_RED;
        } else if(x > 5.304 && x < 10.705 && y <= -6.831) {
            zone = Zone.TRENCH_RUN_BLUE;
        } else if(x > 5.304 && x < 10.705 && y < -1.394 && y > -6.831) {
            zone = Zone.RENDEVOUS_POINT;
        } else if(x > 12.894 && x <= 15.905) {
            zone = Zone.SECTOR_RED;
        } else if(x < 3.114 && x >= 0.104) {
            zone = Zone.SECTOR_BLUE;
        } else {
            zone = Zone.INVALID;
        }

        return zone;
    }

    /**
     * Returns the {@link Zone} of the current position of the Robot.
     * @return The Zone in which the Robot is in.
     */
    public Zone getCurrentZone() {
        m_currentZone = getPositionZone(m_drive.getPose());
        return m_currentZone;
    } 

}
