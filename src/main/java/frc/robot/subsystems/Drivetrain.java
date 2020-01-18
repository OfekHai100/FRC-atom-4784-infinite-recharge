/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
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

public class Drivetrain extends SubsystemBase {
  
  // FAKE PIDF VALUES!
  private static AtomTalon m_leftFront = new AtomTalon(Constants.Ports.kLeftMaster, Constants.kPIDIdx, Constants.DrivetrainConstants.kSlotIdx, 0.1, 0, 1, 0);
  private static AtomTalon m_rightFront = new AtomTalon(Constants.Ports.kRightSlave, Constants.kPIDIdx, Constants.DrivetrainConstants.kSlotIdx, 0.1, 0, 1, 0);

  private static WPI_VictorSPX m_leftRear = new WPI_VictorSPX(Constants.Ports.kLeftSlave);
  private static WPI_VictorSPX m_rightRear = new WPI_VictorSPX(Constants.Ports.kRightSlave);

  private SpeedControllerGroup m_left = new SpeedControllerGroup(m_leftFront, m_leftRear);
  private SpeedControllerGroup m_right = new SpeedControllerGroup(m_rightFront, m_rightRear);

  private PigeonIMU m_pigeon = new PigeonIMU(m_leftFront);

  private DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);

  private DifferentialDriveOdometry m_odometry;

  /**
   * Creates a new Drivetrain.
   */
  public Drivetrain() {
    m_leftRear.configFactoryDefault();
    m_rightRear.configFactoryDefault();

    m_pigeon.configFactoryDefault();

    //m_leftSlave.follow(m_leftMaster);
    //m_rightSlave.follow(m_rightMaster);

    //m_leftMaster.setInverted(false);
    //m_rightMaster.setInverted(true);

    //m_leftSlave.setInverted(InvertType.FollowMaster);
    //m_rightSlave.setInverted(InvertType.FollowMaster);

    m_leftFront.configEncoder();
    m_rightFront.configEncoder();

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
  }

  /**
   * Get the Yaw heading of the Robot.
   * @return heading, in degrees.
   */
  public double getHeading() {
    double[] ypr = getYPR();
    return ypr[0];
  }

  /**
   * Get the Yaw, Pitch and Roll values of the Robot as an array.
   * @return
   */
  public double[] getYPR() {
    double[] ypr = new double[3];
    m_pigeon.getYawPitchRoll(ypr);
    return ypr;
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftFront.getVelocityMeters(), m_rightFront.getVelocityMeters());
  }

  public double getDistance() {
    return (m_leftFront.getDistanceMeters() + m_rightFront.getDistanceMeters()) / 2.0;
  }

  /**
   * Stops all motors.
   */
  public void stop() {
    m_leftFront.set(ControlMode.PercentOutput, 0);
    m_rightFront.set(ControlMode.PercentOutput, 0);
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

  public static double[] getOutputs() {
    double[] outputs = {m_leftFront.getMotorOutputPercent(), m_rightFront.getMotorOutputPercent(), m_leftRear.getMotorOutputPercent(), m_rightRear.getMotorOutputPercent()};
    return outputs;
  }

  /**
   * Test method for auto command.
   */
  public void testAutoCommand() {
    System.out.println("JUST A TEST, RELAX");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_odometry.update(Rotation2d.fromDegrees(getHeading()), m_leftFront.getDistanceMeters(), m_rightFront.getDistanceMeters());
  }
}
