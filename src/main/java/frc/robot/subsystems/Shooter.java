/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.util.AtomTalon;
import frc.robot.util.AtomTalon.Subsystem;

public class Shooter extends SubsystemBase {

  // FAKE P VALUE!
  private static AtomTalon m_rotator = new AtomTalon(Constants.Ports.kRotator, Constants.ShooterConstants.kSlotIdx, 0.1);
  private VictorSPX m_shooter = new VictorSPX(Constants.Ports.kShooterFront);
  private VictorSPX m_loader = new VictorSPX(Constants.Ports.kShooterRear);
  
  /**
   * Enum to store all default positions of the Shooter.
   */
  public enum Position {
    STARTING_CONFIGURATION(45.0),
    LOWEST_POSITION(31.4),
    LOADING(72.5),
    PORT(80.0),
    TRENCH(32.644);

    private double m_angle;

    Position(double angle) {
      this.m_angle = angle;
    }

    public double getPositionAngle() {
      return this.m_angle;
    }
  }

  /**
   * Creates a new Shooter.
   */
  public Shooter() {
    m_shooter.configFactoryDefault();
    m_loader.configFactoryDefault();

    m_shooter.setInverted(false);
    m_loader.setInverted(true);
    m_rotator.setInverted(true);
    m_rotator.setSensorPhase(true);

    m_shooter.configOpenloopRamp(0.3);
    m_loader.configOpenloopRamp(0.3);
    m_rotator.configClosedloopRamp(0.4);

    m_rotator.configEncoder(Subsystem.SHOOTER);
  }

  /**
   * Sets output for the shooting motor, determined by vision processing or by default pre-determined values.
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
    int setpoint = m_rotator.degreesToUnits(angle);
    int current = m_rotator.getSelectedSensorPosition();
    m_rotator.set(ControlMode.Position, setpoint, DemandType.ArbitraryFeedForward, m_rotator.calculateCoisneScalar());
    while(current - 1 < setpoint) {
      current  = m_rotator.getSelectedSensorPosition();
    }
  }

  /**
   * Sets the shooter to specific angle determined by a {@link Position}.
   * @param position of the Shooter to set, as {@link Position} object.
   */
  public void goToPosition(Position position) {
    goToAngle(position.getPositionAngle());
  }

  /**
   * Gets the current angle of the rotator.
   * @return Current angle, in degrees.
   */
  public double getAngle() {
    return m_rotator.unitsToDegrees(m_rotator.getSelectedSensorPosition());
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
  }


  /**
   * This method is used for the Pigeon IMU object in the Drivetrain class.
   * @return Talon which connected to the Pigeon, as a {@link AtomTalon} object.
   */
  public static AtomTalon getTalon() {
    return m_rotator;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Shooter Angle:", getAngle());
    SmartDashboard.putNumber("Shooter Output:", m_shooter.getMotorOutputPercent());
  }
}
