/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.AtomTalon;

public class Shooter extends SubsystemBase {
  
  // FAKE CONSTANTS!
  private AtomTalon m_left = new AtomTalon(Constants.Ports.kLeft, Constants.kPIDIdx, Constants.ShooterConstants.kSlotIdx, 0.1, 0, 1, 0);
  private VictorSPX m_right = new VictorSPX(Constants.Ports.kRight);
  
  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    m_right.configFactoryDefault();
    m_right.follow(m_left);

    m_left.setInverted(true);
    m_right.setInverted(InvertType.OpposeMaster);

    m_left.configEncoder();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
