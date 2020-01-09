/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Roulette extends SubsystemBase {
<<<<<<< HEAD:src/main/java/frc/robot/subsystems/Roulette.java
  
  private I2C.Port m_port = I2C.Port.kOnboard;
  private ColorSensorV3 m_colorSensor = new ColorSensorV3(m_port);
  
  /**
   * Creates a new Roulette.
   */
=======
  I2C.Port port = I2C.Port.kOnboard;
  ColorSensorV3 colorSensor = new ColorSensorV3(port);
  /**
   * Creates a new Roulette.
   */
  // roullete class 
>>>>>>> f12173e2da91a61b93b641a07b46606795b3252b:src/main/java/frc/robot/subsystems/Shooter.java
  public Roulette() {

  }

  /**
   * Checks detected color by ColorSensorV3.
   */
  public String whatColor() {
    Color detected = m_colorSensor.getColor();
    if (detected.equals(Color.kRed)) {
      return "RED";
    }
    if (detected.equals(Color.kYellow)) {
      return "YELLOW";
    }
    if (detected.equals(Color.kBlue)) {
      return "BLUE";
    }
    if (detected.equals(Color.kGreen)) {
      return "GREEN";
    }
    return "NO COLOR FOUND";
  } 
  


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
