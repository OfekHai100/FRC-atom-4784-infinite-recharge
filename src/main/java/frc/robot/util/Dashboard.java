/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.Constants;
import frc.robot.pathing.PathManager.Path;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.Roller;
import frc.robot.subsystems.Shooter;

/**
 * Dashboard++.
 */
public class Dashboard {

    // Path chooser:
    private static SendableChooser<Path> m_chooser = new SendableChooser<Path>();

    private static DriverStation m_DS;

    private static ShuffleboardTab m_setupTab = Shuffleboard.getTab("Setup Tab");
    private static ShuffleboardTab m_autoTab = Shuffleboard.getTab("Autonomous Tab");
    private static ShuffleboardTab m_teleopTab = Shuffleboard.getTab("Teleop Tab");

    private static NetworkTableEntry m_heading, m_distance, m_angle, m_velocity;
    private static NetworkTableEntry m_reverseClimb, m_reverseIntake, m_activateIntake, m_vision, m_position, m_output;

    private static boolean m_passedAuto;
    private static boolean m_passedTeleop;

    /**
     * Utility class, private constructor.
     */
    private Dashboard() {
    }

    public static void setup() {
        m_DS = DriverStation.getInstance();

        putChooserData();
        putMatchData();
        putAutonomousData();
        putTeleopData();
        Shuffleboard.selectTab("Setup Tab");

        m_passedAuto = false;
        m_passedTeleop = false;
    }

    public static void loop() {
        if(m_DS.isAutonomous()) {
            if(!m_passedAuto) {
                Shuffleboard.selectTab("Autonomous Tab");
                m_passedAuto = true;
            }
            updateAutonomousData();
        } else if(m_DS.isOperatorControl()) {
            if(!m_passedTeleop) {
                Shuffleboard.selectTab("Teleop Tab");
                m_passedTeleop = true;
            }
            updateTeleopData();
        }
    }

    /**
     * Displays the {@link Path} chooser at the begining of every match.
     */
    private static void putChooserData() {
        m_chooser.addOption("Red Port", Path.RED_PORT);
        m_chooser.addOption("Red Middle", Path.RED_MIDDLE);
        m_chooser.addOption("Red Blue-Feeder", Path.RED_BLUE_FEEDER);
        m_chooser.addOption("Blue Port", Path.BLUE_PORT);
        m_chooser.addOption("Blue Middle", Path.BLUE_MIDDLE);
        m_chooser.addOption("Blue Red-Feeder", Path.BLUE_RED_FEEDER);

        m_setupTab.add("Routine chooser", m_chooser);
    }

    /**
     * Displays match data every match for the drivers.
     */
    private static void putMatchData() {
        boolean isRed;
        String matchType;
        String event;
        int matchNum;
        int station;
        
        if(m_DS.isFMSAttached()) {
            matchNum = DriverStation.getInstance().getMatchNumber();
            event = DriverStation.getInstance().getEventName();
            station = DriverStation.getInstance().getLocation();
            
            if(m_DS.getAlliance() == Alliance.Blue) {
                isRed = false;
            } else {
                isRed = true;
            }

            switch(m_DS.getMatchType()) {
                case Qualification:
                    matchType = "QUAL";
                case Elimination:
                    matchType = "ELIM";
                default:
                    matchType = "PRCT";
            }
        } else {
            matchType = "PRCT";
            matchNum = 0;
            isRed = true;
            station = 0;
            event = "Virtual Practice Event";
        }

        m_setupTab.add("Alliance", isRed)
                  .withProperties(Map.of("Color when true", "#FF0000", "Color when false", "#FF5555"))
                  .withSize(6, 3)
                  .withPosition(0, 0)
                  .withWidget(BuiltInWidgets.kBooleanBox);

        m_setupTab.add("Match", matchType + " " + matchNum)
                  .withSize(6, 1)
                  .withPosition(0, 4)
                  .withWidget(BuiltInWidgets.kTextView);
        
        m_setupTab.add("Event", "Competing at " + event + ", Station number " + station)
                  .withSize(6,1)
                  .withPosition(0, 5)
                  .withWidget(BuiltInWidgets.kTextView);
    }

    /**
     * Displays and initializes all Autonomous Tab values.
     */
    private static void putAutonomousData() {    
        m_heading = m_autoTab.add("Robot Heading", 0.0)
                             .withSize(4, 8)
                             .withPosition(0, 0)
                             .withWidget(BuiltInWidgets.kTextView)
                             .getEntry();

        m_distance = m_autoTab.add("Robot Distance", 0.0)
                              .withSize(4, 8)
                              .withPosition(0, 5)
                              .withWidget(BuiltInWidgets.kTextView)
                              .getEntry();

        m_angle = m_autoTab.add("Shooter Angle", 0.0)
                           .withProperties(Map.of("Min", Constants.ShooterConstants.kMinAngle, "Max", Constants.ShooterConstants.kMaxAngle))
                           .withSize(4, 6)
                           .withPosition(0, 9)
                           .withWidget(BuiltInWidgets.kNumberBar)
                           .getEntry();

        m_velocity = m_autoTab.add("Shooter - Velocity", 0.0)
                              .withSize(4, 6)
                              .withPosition(2, 0)
                              .withWidget(BuiltInWidgets.kNumberBar)
                              .getEntry();
    }

    /**
     * Updates the Autonomous Tab values periodically.
     */
    private static void updateAutonomousData() {
        Drivetrain drive = Drivetrain.getInstance();
        Shooter shooter = Shooter.getInstance();

        m_heading.setDouble(drive.getHeading());
        m_distance.setDouble(drive.getDistance());
        m_angle.setDouble(shooter.getAngle());
        m_velocity.setDouble(shooter.getShooterOutput());
    }
    
    /**
     * Displays and initializes all Teleop Tab values.
     */
    private static void putTeleopData() {
        m_reverseClimb = m_teleopTab.add("Climber - Reversed", false)
                                    .withProperties(Map.of("Color when true", "#008000", "Color when false", "#FF0000"))
                                    .withSize(4, 4)
                                    .withPosition(0, 1)
                                    .withWidget(BuiltInWidgets.kBooleanBox)
                                    .getEntry();

        m_reverseIntake = m_teleopTab.add("Intake - Reversed", false)
                                     .withProperties(Map.of("Color when true", "#008000", "Color when false", "#FF0000"))
                                     .withSize(4, 4)
                                     .withPosition(6, 1)
                                     .withWidget(BuiltInWidgets.kBooleanBox)
                                     .getEntry();

        m_activateIntake = m_teleopTab.add("Intake - Activated", false)
                                      .withProperties(Map.of("Color when true", "#008000", "Color when false", "#FF0000"))
                                      .withSize(4, 4)
                                      .withPosition(6, 6)
                                      .withWidget(BuiltInWidgets.kBooleanBox)
                                      .getEntry();

        m_vision = m_teleopTab.add("Vision Mode", false)
                              .withProperties(Map.of("Color when true", "#008000", "Color when false", "#FF0000"))
                              .withSize(4, 4)
                              .withPosition(12, 1)
                              .withWidget(BuiltInWidgets.kBooleanBox)
                              .getEntry();

        m_position = m_teleopTab.add("Shooter - Position", "")
                                .withSize(8, 4)
                                .withPosition(14, 1)
                                .withWidget(BuiltInWidgets.kTextView)
                                .getEntry();

        m_output = m_teleopTab.add("Shooter - Output", 0.0)
                              .withSize(4, 6)
                              .withPosition(16, 1)
                              .withWidget(BuiltInWidgets.kNumberBar)
                              .getEntry();
    }

    /**
     * Updates the Teleop Tab values periodically.
     */
    private static void updateTeleopData() {
        Climber climber = Climber.getInstance();
        LED led = LED.getInstance();
        Roller roller = Roller.getInstance();
        Shooter shooter = Shooter.getInstance();


        m_reverseClimb.setBoolean(climber.isReversed());
        m_reverseIntake.setBoolean(roller.isReversed());
        m_activateIntake.setBoolean(roller.isIntakeModeActivated());
        m_vision.setBoolean(led.isVisionActivated());
        m_position.setString(shooter.getPositionAsString());
        m_output.setDouble(shooter.getShooterOutput());
    }

    /**
     * Returns the selected path for autonomous routine.
     * @return Selected path.
     */
    public static Path getSelectedPath() {
        return m_chooser.getSelected();
    }

}
