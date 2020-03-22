/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.pathing;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;

import frc.robot.Constants;

/**
 * This class represents a configuration of a trajectory.
 */
public class Configuration {

    private TrajectoryConfig m_config;
    private DifferentialDriveVoltageConstraint m_voltageConstraint;

    /**
     * Constructor.
     */
    private Configuration(TrajectoryConfig config, DifferentialDriveVoltageConstraint voltageConstraint) {
        this.m_config = config;
        this.m_voltageConstraint = voltageConstraint;
    }

    /**
     * Returns the trajectory configuration.
     * @return Configuration, as a {@link TrajectoryConfig} object.
     */
    public TrajectoryConfig getConfig() {
        return this.m_config;
    }

    /**
     * Returns the trajectory voltage constraint.
     * @return Voltage constraint, as a {@link DifferentialDriveVoltageConstraint} object.
     */
    public DifferentialDriveVoltageConstraint getVoltageConstraint() {
        return this.m_voltageConstraint;
    }

    /**
     * This method generates a new Configuration object and returns it.
     * @return Configuration.
     */
    public static Configuration getConfiguration() {
        TrajectoryConfig config;
        DifferentialDriveVoltageConstraint voltageConstraint;
        
        voltageConstraint = 
            new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.DrivetrainConstants.ksVolts,
                                           Constants.DrivetrainConstants.ksVoltSecondsPerMeter,
                                           Constants.DrivetrainConstants.ksVoltSecondsSquaredPerMeter), 
                Constants.DrivetrainConstants.kDriveKinematics, 
                10.0
            );
        
        config = new TrajectoryConfig(Constants.DrivetrainConstants.kMaxSpeed, Constants.DrivetrainConstants.kMaxAcceleration)
            .setKinematics(Constants.DrivetrainConstants.kDriveKinematics).addConstraint(voltageConstraint);
        
        return new Configuration(config, voltageConstraint);
    }
    
}
