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

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.AtomTalon;

public class Climber extends SubsystemBase {
  
  // FAKE PIDF VALUES!
  private AtomTalon m_leftClimber = new AtomTalon(Constants.Ports.kLeftClimber, Constants.kPIDIdx, Constants.ClimberConstants.kSlotIdx, 0.1, 0, 1, 0);
  private VictorSPX m_rightClimber = new VictorSPX(Constants.Ports.kRightClimber);
  private VictorSPX m_rouletteMotor = new VictorSPX(Constants.Ports.kRoulette);

  // Color Sensor:
  private I2C.Port m_port = I2C.Port.kOnboard;
  private ColorSensorV3 m_colorSensor = new ColorSensorV3(m_port);
  
  /**
   * Creates a new Climber.
   */
  public Climber() {
    m_leftClimber.configFactoryDefault();
    m_rightClimber.configFactoryDefault();
  }

  public void setLeft(double left) {
    m_leftClimber.set(ControlMode.PercentOutput, left);
  }

  public void setRight(double right) {
    m_rightClimber.set(ControlMode.PercentOutput, right);
  }

  public void stop() {
    m_leftClimber.set(ControlMode.PercentOutput, 0);
    m_rightClimber.set(ControlMode.PercentOutput, 0);
    m_rouletteMotor.set(ControlMode.PercentOutput, 0);
  }

  /**
   * Rotates the Roulette using a motor.
   */
  public void rotate() {
    m_rouletteMotor.set(ControlMode.PercentOutput, 0.4);
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
