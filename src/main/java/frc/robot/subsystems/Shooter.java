/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.util.hardware.AtomTalon;
import frc.robot.util.hardware.AtomVictor;
import frc.robot.util.hardware.AtomTalon.Subsystem;

/**
 * This subsystem represents the shooting mechanism on the Robot.
 */
public class Shooter extends SubsystemBase {

  /**
   * Enum to store all default positions of the Shooter.
   */
  public enum Position {
    STARTING_CONFIGURATION(45.0),
    LOWEST_POSITION(31.4),
    LOADING(72.5),
    PORT(80.0),
    TRENCH(32.644),
    OTHER(0.0); // In case of 'OTHER', the Shooter will NOT call the getPositionAngle() method.

    private double m_angle;

    Position(double angle) {
      this.m_angle = angle;
    }

    public double getPositionAngle() {
      return this.m_angle;
    }
  }

  // FAKE P VALUE!
  private static AtomTalon m_rotator;
  private AtomVictor m_shooter;
  private AtomVictor m_loader;
  private DigitalInput m_switch;

  private Position m_position;

  private static Shooter m_instance;

  /**
   * Creates a new Shooter.
   */
  private Shooter() {
    m_rotator = new AtomTalon(Constants.Ports.kRotator, Constants.ShooterConstants.kSlotIdx, 0.1);
    m_shooter = new AtomVictor(Constants.Ports.kShooterFront, false);
    m_loader = new AtomVictor(Constants.Ports.kShooterRear, true);

    m_switch = new DigitalInput(Constants.Ports.kSwitch);

    m_rotator.setInverted(true);
    m_rotator.setSensorPhase(true);

    m_shooter.configOpenloopRamp(0.3);
    m_loader.configOpenloopRamp(0.3);
    m_rotator.configClosedloopRamp(0.4);

    m_rotator.configEncoder(Subsystem.SHOOTER);
    
    m_position = Position.STARTING_CONFIGURATION;
  }

  /**
   * Access to the Shooter subsystem
   * @return The Shooter singleton instance.
   */
  public static Shooter getInstance() {
    if(m_instance == null) {
      m_instance = new Shooter();
    }
    return m_instance;
  }

  /**
   * Sets output for the shooting motor, determined by vision processing or by default pre-determined values.
   * @param output
   * @param simple - Is in simple shooting mode, or not.
   */
  public void shoot(double output, boolean simple) {
    m_shooter.set(ControlMode.PercentOutput, output);
    load(false);
    if(!simple) {
      boolean loaded = true;
      while(loaded) {
        loaded = isLoaded();
      }
    }
  }

  /**
   * Activates the Power Cell load motor.
   * @param reverse the direction of the motor, into the Shooter or out, deteremined by driver.
   */
  public void load(boolean reverse) {
    m_loader.set(ControlMode.PercentOutput, reverse ? 0.4 : -0.4);
  }

  /**
   * Sets the Shooter to a specific angle.
   * @param angle in degrees.
   * @param isPosition if called from the getPosition() method.
   */
  public void goToAngle(double angle, boolean isPosition) {
    // Checks if the angle given equals to 0, meaning that the vision calculation did not need angle correction for the final result:
    if(angle == 0.0) {
      System.out.println("Calculation result - Angle correction was not needed!");
      return;
    }
    
    // Checks if the angle is not coming from a known Position, if so checks if a valid angle has been given and sets the current
    // Position to OTHER.
    if(!isPosition) {
      m_position = Position.OTHER;
      if(angle > Constants.ShooterConstants.kMaxAngle || angle < Constants.ShooterConstants.kMinAngle) {
        System.out.println("Error! Invalid angle has been given!");
        return; 
      }
    }

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
    if(m_position.equals(position)) {
      return;
    }
    
    m_position = position;
    goToAngle(position.getPositionAngle(), true);
  }

  /**
   * Returns the current angle of the rotator.
   * @return Current angle, in degrees.
   */
  public double getAngle() {
    return m_rotator.unitsToDegrees(m_rotator.getSelectedSensorPosition());
  }

  /**
   * Returns the current {@link Position} of the Shooter.
   * @return Shooter position, as a {@link Position} object.
   */
  public Position getPosition() {
    return this.m_position;
  }

  /**
   * Returns the current {@link Position} of the Shooter as a String value, used for debugging.
   * @return Shooter position, as a String value.
   */
  public String getPositionAsString() {
    switch(this.m_position) {
      case STARTING_CONFIGURATION:
        return "Strating Configuration";
      case LOWEST_POSITION:
        return "Lowest Position";
      case LOADING:
        return "Loading";
      case PORT:
        return "Power Port";
      case TRENCH:
        return "Trench Run";
      case OTHER:
        return "Other";
      default:
        // Will never get here!
        return "";
    }
  }

  /**
   * Returns the output of the Shooter motor, in TalonSRX percent-output units.
   * @return Output
   */
  public double getShooterOutput() {
    return m_shooter.getMotorOutputPercent();
  }

  /**
   * This method checks if the Shooter is loaded with Power Cells.
   * <p> The role of the timer is to avoid delays between the limit switch and loader motor, in cases like this:
   * A Power Cell has been shot, the next Power Cells not arrived yet to the limit switch, but the method was still called
   * and returned false. The timer prevents those kinds of errors.
   * @return True if there are Power Cells in the loader, false if not.
   */
  public boolean isLoaded() {
    Timer timer = new Timer();
    boolean elapsed = true;

    timer.reset();
    timer.start();

    // Prevents delays by placing timeout of 0.9 seconds:
    while(elapsed) {
      // Wait
      if(timer.hasElapsed(0.9)) {
        elapsed = false;
      }
    }

    return m_switch.get();
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
   * Stops the shooting mechanism - the Shooter motor and the Loader motor.
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
  }
}
