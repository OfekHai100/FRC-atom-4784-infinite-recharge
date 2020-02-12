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
  private VictorSPX m_shooterFront = new VictorSPX(Constants.Ports.kShooterFront);
  private VictorSPX m_shooterRear = new VictorSPX(Constants.Ports.kShooterRear);
  
  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    m_shooterFront.configFactoryDefault();
    m_shooterRear.configFactoryDefault();

    m_shooterFront.setInverted(false);
    m_shooterRear.setInverted(true);
    m_rotator.setInverted(false);

    m_shooterFront.configOpenloopRamp(0.3);
    m_shooterRear.configOpenloopRamp(0.3);
    m_rotator.configOpenloopRamp(0.4);

    m_rotator.configEncoder(Subsystem.SHOOTER);
  }

  public void goToAngle(double angle) {
    double current = m_rotator.getSelectedSensorPosition() / 360;
    double error = angle * current;
    m_rotator.set(ControlMode.Position, error);
  }

  public void stopRotator() {
    m_rotator.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
