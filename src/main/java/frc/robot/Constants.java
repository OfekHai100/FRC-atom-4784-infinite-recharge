/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    
    public static final int kUPR = 4096; // CTRE Encoder spins 4096 units per rotation.
    public static final int kPIDIdx = 0; // Inner PID Loop.
    
    public final class Ports {
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
        // Joysticks:
        public static final int kMain = 0;
        public static final int kSecond = 1;
    }

    public final class DrivetrainConstants {
        public static final double kWheelDiameterMeters = 0.1524;
        public static final int kSlotIdxLeft = 1;
        public static final int kSlotIdxRight = 2;
    }

    public final class ShooterConstants {
        public static final int kSlotIdx = 3;
    }
    
    public final class FieldConstants {
        // Generator Switch highest point, in cm. 
        public static final double kGeneratorHstPoint = 200.3425;
        // Generator Switch lowest point, in cm.
        public static final double kGeneratorLstPoint = 117.1575;
    }

}
