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

  private boolean m_reverse;
  
  /**
   * Creates a new Climber.
   */
  public Climber() {
    m_leftClimber.configFactoryDefault();
    m_rightClimber.configFactoryDefault();

    m_leftClimber.setInverted(false);
    m_rightClimber.setInverted(false);

    m_leftClimber.configOpenloopRamp(0.3);
    m_rightClimber.configOpenloopRamp(0.3);

    m_reverse = false;
  }

  /**
   * Sets power for the left climber.
   */
  public void climbLeft() {
    m_leftClimber.set(ControlMode.PercentOutput, m_reverse ? -0.4 : 0.4);
  }

  /**
   * Sets power for the right climber.
   */
  public void climbRight() {
    m_rightClimber.set(ControlMode.PercentOutput, m_reverse ? -0.4 : 0.4);
  }

  /**
   * Stops the left climber.
   */
  public void stopLeft() {
    m_leftClimber.set(ControlMode.PercentOutput, 0);
  }

  /**
   * Stops the right climber.
   */
  public void stopRight() {
    m_rightClimber.set(ControlMode.PercentOutput, 0);
  }

  /**
   * Sets the direction of climbing, true for downwards, false (default) for upward.
   * @param reverse
   */
  public void setReverse(boolean reverse) {
    this.m_reverse = reverse;
  } 

  /**
   * Returns the current direction of the mechanism.
   * @return current direction - true for reversed, false for default.
   */
  public boolean getReverse() {
    return this.m_reverse;
  }
 
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
