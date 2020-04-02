/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.util.motors.AtomVictor;

public class Roller extends SubsystemBase {
  
  private AtomVictor m_roller = new AtomVictor(Constants.Ports.kRoller, true);
  private DoubleSolenoid m_cylinder = new DoubleSolenoid(Constants.Ports.kCylinderForward, Constants.Ports.kCylinderReverse);

  private boolean m_reverse;
  private boolean m_inIntakeMode;
  
  /**
   * Creates a new Roller.
   */
  public Roller() {
    m_reverse = false;
    m_inIntakeMode = false;
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
   * Returns the status of the Intake mechanism.
   * @return True if open, false if closed.
   */
  public boolean isOpen() {
    if(m_cylinder.get() == Value.kForward) {
      return true;
    }
    return false;
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

  /**
   * Activates or deactivates intake mode.
   * <p> Intake mode is when the Shooter position is in loading position and the roller is open.
   * @param activate - True to activate, false to deactivate.
   */
  public void activateIntakeMode(boolean activate) {
    this.m_inIntakeMode = activate;
  }

  /**
   * Returns the current status of intake mode.
   * @return True if in intake mode, false if not.
   */
  public boolean isIntakeModeActivated() {
    return this.m_inIntakeMode;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Roller - Reverse:", this.m_reverse);
    SmartDashboard.putBoolean("Roller - Intake Mode:", this.m_inIntakeMode);
  }
}
