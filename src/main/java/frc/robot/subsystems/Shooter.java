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
import frc.robot.util.AtomTalon;
import frc.robot.util.AtomTalon.Subsystem;

public class Shooter extends SubsystemBase {

  // FAKE PIDF VALUES!
  private AtomTalon m_rotator = new AtomTalon(Constants.Ports.kRotator, Constants.kPIDIdx, Constants.ShooterConstants.kSlotIdx, 0.1, 0, 1, 0);
  private VictorSPX m_shooter = new VictorSPX(Constants.Ports.kShooterFront);
  private VictorSPX m_loader = new VictorSPX(Constants.Ports.kShooterRear);
  
  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    m_shooter.configFactoryDefault();
    m_loader.configFactoryDefault();

    m_shooter.setInverted(false);
    m_loader.setInverted(true);
    m_rotator.setInverted(false);

    m_shooter.configOpenloopRamp(0.3);
    m_loader.configOpenloopRamp(0.3);
    m_rotator.configOpenloopRamp(0.4);

    m_rotator.configEncoder(Subsystem.SHOOTER);
  }

  /**
   * Sets output for the shooting motor, determined by vision processing.
   * @param output
   */
  public void shoot(double output) {
    m_shooter.set(ControlMode.PercentOutput, output);
    load(false);
  }

  /**
   * Activates the Power Cell load motor.
   * @param reverse the direction of the motor, into the Shooter or out, deteremined by driver.
   */
  public void load(boolean reverse) {
    m_loader.set(ControlMode.PercentOutput, reverse ? 0.4 : -0.4);
  }

  /**
   * Sets the shooter to specific angle.
   * @param angle
   */
  public void goToAngle(double angle) {
    int error = (int) angle / 360 * Constants.kCyclesPerRevolution;
    m_rotator.set(ControlMode.Position, error);
  }

  /**
   * Resets the angle to starting configuration - 45 degrees.
   */
  public void resetAngle() {
    goToAngle(45.0);
  }

  /**
   * Sets the Shooter angle to loading angle.
   */
  public void loadingAngle() {
    goToAngle(72.5);
  }

  /**
   * Gets the current angle of the rotator.
   * @return Current angle, in degrees.
   */
  public double getAngle() {
    return m_rotator.getSelectedSensorPosition() / Constants.kCyclesPerRevolution * 360;
  }

  /**
   * Stops the shooter motor.
   */
  public void stopShooter() {
    m_shooter.set(ControlMode.PercentOutput, 0.0);
  }

  /**
   * Stops the loader motor.
   */
  public void stopLoader() {
    m_loader.set(ControlMode.PercentOutput, 0.0);
  }

  /**
   * Stops the rotator motor.
   */
  public void stopRotator() {
    m_rotator.set(ControlMode.PercentOutput, 0.0);
  }

  /**
   * Stops all motors.
   */
  public void stopAll() {
    stopShooter();
    stopLoader();
    stopRotator();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
