/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Roulette extends SubsystemBase {
  
  // Color Sensor:
  private I2C.Port m_port = I2C.Port.kOnboard;
  private ColorSensorV3 m_colorSensor = new ColorSensorV3(m_port);

  // Hardware:
  private DoubleSolenoid m_cylinder = new DoubleSolenoid(0, 1);
  private VictorSPX m_rouletteMotor = new VictorSPX(5);
  
  /**
   * Creates a new Roulette.
   */
  public Roulette() {
    m_rouletteMotor.configFactoryDefault();
    closeRoulette();
  }

  /**
   * Opens the Roulette spinning mechanism.
   */
  public void openRoulette() {
    m_cylinder.set(Value.kForward);
  }

  /**
   * Closes the Roulette spinning mechanism.
   */
  public void closeRoulette() {
    m_cylinder.set(Value.kReverse);
  }

  /**
   * Rotates the Roulette using a motor.
   */
  public void rotate() {
    m_rouletteMotor.set(ControlMode.PercentOutput, 0.4);
  }

  /**
   * Stops the motor.
   */
  public void stop() {
    m_rouletteMotor.set(ControlMode.PercentOutput, 0);
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
