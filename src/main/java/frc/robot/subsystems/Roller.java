/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Roller extends SubsystemBase {
  
  private VictorSPX m_roller = new VictorSPX(Constants.Ports.kRoller);
  private DoubleSolenoid m_cylinder = new DoubleSolenoid(Constants.Ports.kCylinderForward, Constants.Ports.kCylinderReverse);

  private boolean m_reverse;
  
  /**
   * Creates a new Roller.
   */
  public Roller() {
    m_roller.configFactoryDefault();
    m_roller.setInverted(true);

    m_reverse = false;
  }

  /**
   * Opens the Roller mechanism.
   */
  public void openRoller() {
    m_cylinder.set(Value.kForward);
  }

  /**
   * Closes the Roller mechanism.
   */
  public void closeRoller() {
    m_cylinder.set(Value.kReverse);
  }

  /**
   * Starts to spin the motors.
   */
  public void intake() {
    m_roller.set(ControlMode.PercentOutput, m_reverse ? -0.3 : 0.3);
  }

  /**
   * Stops the motor.
   */
  public void stop() {
    m_roller.set(ControlMode.PercentOutput, 0);
  }

  /**
   * Sets the direction of intake, true for outwards, false (default) for inward.
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
