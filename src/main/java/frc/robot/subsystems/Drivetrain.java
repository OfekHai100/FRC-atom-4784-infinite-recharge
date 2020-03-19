/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.AtomTalon;
import frc.robot.util.AtomTalon.Subsystem;

public class Drivetrain extends SubsystemBase {
  
  // FAKE PIDF VALUES!
  private static AtomTalon m_leftFront = new AtomTalon(Constants.Ports.kLeftMaster, Constants.kPIDIdx, Constants.DrivetrainConstants.kSlotIdxLeft, 0.1, 0, 1, 0);
  private static AtomTalon m_rightFront = new AtomTalon(Constants.Ports.kRightSlave, Constants.kPIDIdx, Constants.DrivetrainConstants.kSlotIdxRight, 0.1, 0, 1, 0);

  private static WPI_VictorSPX m_leftRear = new WPI_VictorSPX(Constants.Ports.kLeftSlave);
  private static WPI_VictorSPX m_rightRear = new WPI_VictorSPX(Constants.Ports.kRightSlave);

  private SpeedControllerGroup m_left = new SpeedControllerGroup(m_leftFront, m_leftRear);
  private SpeedControllerGroup m_right = new SpeedControllerGroup(m_rightFront, m_rightRear);

  private PigeonIMU m_pigeon = new PigeonIMU(4);

  private DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);

  private DifferentialDriveOdometry m_odometry;

  /**
   * Creates a new Drivetrain.
   */
  public Drivetrain() {
    m_leftRear.configFactoryDefault();
    m_rightRear.configFactoryDefault();

    m_pigeon.configFactoryDefault();

    //m_leftRear.follow(m_leftFront);
    //m_rightRear.follow(m_rightFront);

    //m_leftFront.setInverted(false);
    //m_rightFront.setInverted(true);

    //m_leftRear.setInverted(InvertType.FollowMaster);
    //m_rightRear.setInverted(InvertType.FollowMaster);

    m_leftFront.configOpenloopRamp(0.4);
    m_rightFront.configOpenloopRamp(0.4);
    m_leftRear.configOpenloopRamp(0.4);
    m_rightRear.configOpenloopRamp(0.4);

    m_leftFront.configEncoder(Subsystem.DRIVETRAIN);
    m_rightFront.configEncoder(Subsystem.DRIVETRAIN);

    m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
  }

  /**
   * Drive using Arcade-Drive
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
   * Get the Yaw heading of the Robot.
   * @return Robot's heading in degrees, from -180 to 180.
   */
  public double getHeading() {
    double[] ypr = getYPR();
    double angle = ypr[0];
    return Math.IEEEremainder(ypr[0], 360) * (Constants.DrivetrainConstants.kGyroReversed ? -1.0 : 1.0);
  }

  /**
   * Get the Yaw, Pitch and Roll values of the Robot as an array.
   * @return yaw, pitch, roll.
   */
  public double[] getYPR() {
    double[] ypr = new double[3];
    m_pigeon.getYawPitchRoll(ypr);
    return ypr;
  }

  /**
   * Get the current position.
   * @return current position.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  /**
   * Get the current wheel speeds.
   * @return current wheel speeds.
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftFront.getVelocityMeters(), m_rightFront.getVelocityMeters());
  }

  /**
   * Get the average distance traveled.
   * @return average distance.
   */
  public double getDistance() {
    return (m_leftFront.getDistanceMeters() + m_rightFront.getDistanceMeters()) / 2.0;
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
   * Resets the Gyro.
   */
  public void zero() {
    m_pigeon.setYaw(0);
  }

  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_odometry.update(Rotation2d.fromDegrees(getHeading()), m_leftFront.getDistanceMeters(), m_rightFront.getDistanceMeters());
  }
}
