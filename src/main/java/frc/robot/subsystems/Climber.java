/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {
  
  private VictorSPX m_leftClimber = new VictorSPX(Constants.Ports.kLeftClimber);
  private VictorSPX m_rightClimber = new VictorSPX(Constants.Ports.kRightClimber);
  
  /**
   * Creates a new Climber.
   */
  public Climber() {
    m_leftClimber.configFactoryDefault();
    m_rightClimber.configFactoryDefault();
  }

  public void setLeft(double left) {
    m_leftClimber.set(ControlMode.PercentOutput, left);
  }

  public void setRight(double right) {
    m_rightClimber.set(ControlMode.PercentOutput, right);
  }

  public void stop() {
    m_leftClimber.set(ControlMode.PercentOutput, 0);
    m_rightClimber.set(ControlMode.PercentOutput, 0);
  }
 
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
