/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.util.hardware.AtomPigeon;
import frc.robot.util.hardware.AtomTalon;
import frc.robot.util.hardware.AtomVictor;
import frc.robot.util.hardware.AtomTalon.Subsystem;

/**
 * This subsystem represents the drivetrain of the Robot.
 */
public class Drivetrain extends SubsystemBase {
  
  private static AtomTalon m_leftFront;
  private static AtomTalon m_rightFront;

  private static AtomVictor m_leftRear; 
  private static AtomVictor m_rightRear; 

  private SpeedControllerGroup m_left;
  private SpeedControllerGroup m_right; 

  private AtomPigeon m_pigeon;

  private DifferentialDrive m_drive;

  private DifferentialDriveOdometry m_odometry;

  private static Drivetrain m_instance;

  /**
   * Creates a new Drivetrain.
   */
  private Drivetrain() {
    // FAKE P VALUE!
    m_leftFront = new AtomTalon(Constants.Ports.kLeftMaster, Constants.DrivetrainConstants.kSlotIdxLeft, 0.1);
    m_rightFront = new AtomTalon(Constants.Ports.kRightSlave, Constants.DrivetrainConstants.kSlotIdxRight, 0.1);

    m_leftRear = new AtomVictor(Constants.Ports.kLeftSlave);
    m_rightRear = new AtomVictor(Constants.Ports.kRightSlave);

    m_left = new SpeedControllerGroup(m_leftFront, m_leftRear);
    m_right = new SpeedControllerGroup(m_rightFront, m_rightRear);

    m_pigeon = new AtomPigeon(Shooter.getTalon(), Constants.DrivetrainConstants.kGyroReversed);

    m_drive = new DifferentialDrive(m_left, m_right);

    m_leftFront.configOpenloopRamp(0.4);
    m_rightFront.configOpenloopRamp(0.4);
    m_leftRear.configOpenloopRamp(0.4);
    m_rightRear.configOpenloopRamp(0.4);

    m_leftFront.configEncoder(Subsystem.DRIVETRAIN);
    m_rightFront.configEncoder(Subsystem.DRIVETRAIN);

    m_odometry = new DifferentialDriveOdometry(m_pigeon.getAsRotation2d());
  }

  /**
   * Access to the Drivetrain subsystem
   * @return The Drivetrain singleton instance.
   */
  public static Drivetrain getInstance() {
    if(m_instance == null) {
      m_instance = new Drivetrain();
    }
    return m_instance;
  }

  /**
   * Drive using Arcade-Drive.
   * @param y value of the joystick.
   * @param x value of the joystick.
   */
  public void arcade(double y, double x) {
    m_drive.arcadeDrive(y, x);
  }

  /**
   * Sets the voltage for the left and the right side of the Drivetrain.
   * @param left voltage.
   * @param right voltage.
   */
  public void setVoltage(double left, double right) {
    m_left.setVoltage(left);
    m_right.setVoltage(right);
    m_drive.feed();
  }

  /**
   * Sets the maximum output of the Drivetrain, useful for slowing-down.
   * @param max
   */
  public void setMax(double max) {
    m_drive.setMaxOutput(max);
  }

  /**
   * Get the current position of the Robot, as a {@link Pose2d} object.
   * @return Current position.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  /**
   * Get the current wheel speeds of the Robot.
   * @return Current wheel speeds.
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftFront.getVelocityMeters(), m_rightFront.getVelocityMeters());
  }

  /**
   * Get the average distance traveled by the Robot.
   * @return Average distance.
   */
  public double getDistance() {
    return (m_leftFront.getDistanceMeters() + m_rightFront.getDistanceMeters()) / 2.0;
  }

  /**
   * Returns the current heading of the Robot, between -180 to 180 degrees.
   * @return Robot heading.
   */
  public double getHeading() {
    return m_pigeon.getHeading();
  }

  /**
   * Stops all motors.
   */
  public void stop() {
    m_leftFront.set(ControlMode.PercentOutput, 0);
    m_rightFront.set(ControlMode.PercentOutput, 0);
    m_leftRear.set(ControlMode.PercentOutput, 0);
    m_rightRear.set(ControlMode.PercentOutput, 0);
  }

  /**
   * Resets the Encoders.
   */
  public void resetEncoders() {
    m_leftFront.reset();
    m_rightFront.reset();
  }

  /**
   * Resets Robot odometry.
   * @param pose current position of the Robot, as a {@link Pose2d} obejct.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(pose, m_pigeon.getAsRotation2d());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_odometry.update(m_pigeon.getAsRotation2d(), m_leftFront.getDistanceMeters(), m_rightFront.getDistanceMeters());
  }
}
