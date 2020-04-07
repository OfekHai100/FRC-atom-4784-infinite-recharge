/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.util.hardware.AtomVictor;

/**
 * This subsystem represents the climbing mechanism on the Robot.
 */
public class Climber extends SubsystemBase {
  
  private AtomVictor m_leftClimber;
  private AtomVictor m_rightClimber;

  private boolean m_reverse;

  private static Climber m_instance;
  
  /**
   * Creates a new Climber.
   */
  private Climber() {
    m_leftClimber = new AtomVictor(Constants.Ports.kLeftClimber, false);
    m_rightClimber = new AtomVictor(Constants.Ports.kRightClimber, false);

    m_leftClimber.configOpenloopRamp(0.3);
    m_rightClimber.configOpenloopRamp(0.3);

    m_reverse = false;
  }

  /**
   * Access to the Climber subsystem
   * @return The Climber singleton instance.
   */
  public static Climber getInstance() {
    if(m_instance == null) {
      m_instance = new Climber();
    }
    return m_instance;
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
  public boolean isReversed() {
    return this.m_reverse;
  }
 
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
