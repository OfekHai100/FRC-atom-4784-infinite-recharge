/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    
    public static final int kEdgesPerRevolution = 4096; // CTR Mag Encoder.
    public static final int kCyclesPerRevolution = 1024; // CTR Mag Encoder.
    public static final int kPIDIdx = 0; // Inner PID Loop.
    
    public static final class Ports {
        // Drivetrain hardware:
        public static final int kLeftMaster = 1;
        public static final int kRightMaster = 2;
        public static final int kLeftSlave = 3;
        public static final int kRightSlave = 4;

        // Shooter hardware:
        public static final int kShooterFront = 5;
        public static final int kShooterRear = 6;
        public static final int kRotator = 7;

        // Climber hardware:
        public static final int kLeftClimber = 8;
        public static final int kRightClimber = 9;

        // Roller hardware:
        public static final int kRoller = 10;
        public static final int kCylinderForward = 0;
        public static final int kCylinderReverse = 1;

        // LED:
        public static final int kLEDStrip = 0; // PWM
        
        // Arduino I2C:
        public static final int kArduinoI2C = 4;
        
        // Joysticks:
        public static final int kMain = 0;
        public static final int kSecond = 1;
    }

    public static final class DrivetrainConstants {
        public static final double kWheelDiameterMeters = 0.1524;
        
        // UPDATE!
        public static final double ksVolts = 0;
        public static final double ksVoltSecondsPerMeter = 0;
        public static final double ksVoltSecondsSquaredPerMeter = 0;

        // UPDATE!
        public static final double kMaxSpeed = 0;
        public static final double kMaxAcceleration = 0;

        // UPDATE!
        public static final double kTrackWidth = 0.69;
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidth);

        // UPDATE!
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;

        // UPDATE!
        public static final double kP = 0;

        public static final int kSlotIdxLeft = 1;
        public static final int kSlotIdxRight = 2;

        public static final boolean kGyroReversed = false;
    }

    public static final class ShooterConstants {
        public static final int kSlotIdx = 3;

        public static final double kMinVelocity = 0.2;
        public static final double kMaxVelocity = 1;
        public static final double kMinAngle = 31.4;
        public static final double kMaxAngle = 81.2;

        public static final int kLowestPosition = 891; // In sensor units
    }
    
    public static final class FieldConstants {
        // Generator Switch highest point, in cm. 
        public static final double kGeneratorHstPoint = 200.3425;
        // Generator Switch lowest point, in cm.
        public static final double kGeneratorLstPoint = 117.1575;
        // The minimum height of the Outer Port with one Power Cell included, in cm.
        public static final double kPowerPortMin = 220.455;
        // The maximum height of the Outer Port with one Power Cell included, in cm.
        public static final double kPowerPortMax = 278.655;
        // Minimum distance from the Power Port when shooting angle is 45 degrees, in cm.
        public static final double kDistanceMin = 136.1022204;
        // Maximum distance from the Power Port when shooting angle is 45 degrees, in cm.
        public static final double kDistanceMax = 172.0331325;
        // The height of the center of the Inner Port, in cm.
        public static final double kInnerPortHeight = 249.555;
        // Minimum distance from the Inner Port when shooting angle is 45 degrees, in cm.
        public static final double kInnerPortDistanceMin = 154.0676765;
        // Blue Power Port coordinates, as a Pose2d object.
        public static final Pose2d kBluePowerPort = new Pose2d(15.714, -5.808, new Rotation2d(1, 0));
        // Red Power Port coordinates, as a Pose2d object.
        public static final Pose2d kRedPowerPort = new Pose2d(0.306, -2.429, new Rotation2d(-1, 0));
    }

}
