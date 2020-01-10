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

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.AtomTalon;

public class Drivetrain extends SubsystemBase {
  
  // FAKE PIDF VALUES!
  public AtomTalon leftMaster = new AtomTalon(1, 0, 1, 0.1, 0, 1, 0);
  public AtomTalon rightMaster = new AtomTalon(2, 0, 1, 0.1, 0, 1, 0);

  private WPI_VictorSPX m_leftSlave = new WPI_VictorSPX(3);
  private WPI_VictorSPX m_rightSlave = new WPI_VictorSPX(4);

  private DifferentialDrive m_drive = new DifferentialDrive(leftMaster, rightMaster);

  /**
   * Creates a new Drivetrain.
   */
  public Drivetrain() {
    m_leftSlave.configFactoryDefault();
    m_rightSlave.configFactoryDefault();

    m_leftSlave.follow(leftMaster);
    m_rightSlave.follow(leftMaster);

    leftMaster.setInverted(false);
    rightMaster.setInverted(true);

    m_leftSlave.setInverted(InvertType.FollowMaster);
    m_rightSlave.setInverted(InvertType.FollowMaster);

    leftMaster.configEncoder();
    rightMaster.configEncoder();
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
   * Stops all motors.
   */
  public void stop() {
    leftMaster.set(ControlMode.PercentOutput, 0);
    rightMaster.set(ControlMode.PercentOutput, 0);
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
