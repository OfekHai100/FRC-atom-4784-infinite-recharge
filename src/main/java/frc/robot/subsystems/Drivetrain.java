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

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.AtomTalon;

public class Drivetrain extends SubsystemBase {
  
  // FAKE PIDF VALUES!
  private AtomTalon m_leftMaster = new AtomTalon(Constants.Ports.kLeftMaster, Constants.kPIDIdx, Constants.DrivetrainConstants.kSlotIdx, 0.1, 0, 1, 0);
  private AtomTalon m_rightMaster = new AtomTalon(Constants.Ports.kRightSlave, Constants.kPIDIdx, Constants.DrivetrainConstants.kSlotIdx, 0.1, 0, 1, 0);

  private WPI_VictorSPX m_leftSlave = new WPI_VictorSPX(Constants.Ports.kLeftSlave);
  private WPI_VictorSPX m_rightSlave = new WPI_VictorSPX(Constants.Ports.kRightSlave);

  private PigeonIMU m_pigeon = new PigeonIMU(m_leftMaster);

  private DifferentialDrive m_drive = new DifferentialDrive(m_leftMaster, m_rightMaster);

  /**
   * Creates a new Drivetrain.
   */
  public Drivetrain() {
    m_leftSlave.configFactoryDefault();
    m_rightSlave.configFactoryDefault();

    m_pigeon.configFactoryDefault();

    m_leftSlave.follow(m_leftMaster);
    m_rightSlave.follow(m_rightMaster);

    m_leftMaster.setInverted(false);
    m_rightMaster.setInverted(true);

    m_leftSlave.setInverted(InvertType.FollowMaster);
    m_rightSlave.setInverted(InvertType.FollowMaster);

    m_leftMaster.configEncoder();
    m_rightMaster.configEncoder();
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

  /**
   * Stops all motors.
   */
  public void stop() {
    m_leftMaster.set(ControlMode.PercentOutput, 0);
    m_rightMaster.set(ControlMode.PercentOutput, 0);
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
  }
}
