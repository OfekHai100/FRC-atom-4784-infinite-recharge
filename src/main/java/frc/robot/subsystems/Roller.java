/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Roller extends SubsystemBase {
  
  private Spark m_roller = new Spark(Constants.Ports.kRoller);
  private DoubleSolenoid m_cylinder = new DoubleSolenoid(Constants.Ports.kRollerForward, Constants.Ports.kRollerReverse);
  
  /**
   * Creates a new Roller.
   */
  public Roller() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
