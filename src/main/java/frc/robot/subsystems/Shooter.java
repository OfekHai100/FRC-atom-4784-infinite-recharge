/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.AtomTalon;

public class Shooter extends SubsystemBase {
  
  // FAKE PIDF VALUES!
  private AtomTalon m_rotator = new AtomTalon(Constants.Ports.kRotator, Constants.kPIDIdx, Constants.ShooterConstants.kSlotIdx, 0.1, 0, 1, 0);
  private VictorSPX m_shooter = new VictorSPX(Constants.Ports.kShooter);
  
  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    m_shooter.configFactoryDefault();

    m_shooter.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
