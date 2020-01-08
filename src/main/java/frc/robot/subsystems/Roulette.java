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
  I2C.Port port = I2C.Port.kOnboard;
  ColorSensorV3 colorSensor = new ColorSensorV3(port);
  /**
   * Creates a new Roulette.
   */
  // roullete class 
  public Roulette() {

  }

  public String whatColor(){
    Color detected = colorSensor.getColor();
    if (detected.equals(Color.kRed)) {
      return "red";
    }
    if (detected.equals(Color.kYellow)) {
      return "yellow";
     }
    if (detected.equals(Color.kBlue)) {
      return "blue";
    }
    if (detected.equals(Color.kGreen)) {
      return "green";
    }
    return "NO COLOR FOUND";
  } 
  


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
