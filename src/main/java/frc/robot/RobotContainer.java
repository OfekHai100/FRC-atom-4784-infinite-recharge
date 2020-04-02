/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.commands.ActivateIntakeCommand;
import frc.robot.commands.ClimbLeftCommand;
import frc.robot.commands.ClimbRightCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ShootFromPortCommand;
import frc.robot.commands.ShootFromTrenchCommand;
import frc.robot.commands.SimpleShootCommand;
import frc.robot.pathing.PathManager;
import frc.robot.pathing.PathManager.Path;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.LED.State;
import frc.robot.subsystems.Roller;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.Position;
import frc.robot.util.PSController;
import frc.robot.vision.Calculation;
import frc.robot.vision.VisionController;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain m_drive = new Drivetrain();
  private final Climber m_climber = new Climber();
  private final Shooter m_shooter = new Shooter();
  private final Roller m_roller = new Roller();

  // LED:
  public final LED ledManager = new LED();

  // Vision:
  private final VisionController m_vision = new VisionController();
  private Calculation m_calculation;

  // Joysticks:
  PSController driver = new PSController(Constants.Ports.kMain);
  PSController operator  = new PSController(Constants.Ports.kSecond);

  // Driver buttons:
  JoystickButton activateIntake = new JoystickButton(driver, PSController.getTriangle());
  JoystickButton intake = new JoystickButton(driver, PSController.getL2());
  JoystickButton reverseIntake = new JoystickButton(driver, PSController.getUp());
  JoystickButton climbLeft = new JoystickButton(driver, PSController.getL1());
  JoystickButton climbRight = new JoystickButton(driver, PSController.getR1());
  JoystickButton reverseClimb = new JoystickButton(driver, PSController.getDown());
  JoystickButton lowerShooter = new JoystickButton(driver, PSController.getIx());
  JoystickButton driverAbort = new JoystickButton(driver, PSController.getPad());
  
  // Operator buttons:
  JoystickButton activateVision = new JoystickButton(operator, PSController.getTriangle());
  JoystickButton shootUsingVision = new JoystickButton(operator, PSController.getR2());
  JoystickButton shootFromTrench = new JoystickButton(operator, PSController.getR1());
  JoystickButton shootFromPort = new JoystickButton(operator, PSController.getL1());
  JoystickButton simpleShoot = new JoystickButton(operator, PSController.getSquare());
  JoystickButton resetShooter = new JoystickButton(driver, PSController.getIx());
  JoystickButton operatorAbort = new JoystickButton(driver, PSController.getPad());

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Default arcade drive command:
    m_drive.setDefaultCommand(
      new RunCommand(() -> m_drive.arcade(-driver.getY(), driver.getX()), m_drive)
    );

    // Intake commands:
    activateIntake.whenPressed(new ActivateIntakeCommand(m_roller, m_shooter)
      .beforeStarting(() -> m_roller.activateIntakeMode(m_roller.isIntakeModeActivated() ? false : true), m_roller)
    );
    intake.whenHeld(new IntakeCommand(m_roller, m_shooter));
    reverseIntake.whenPressed(new InstantCommand(() -> m_roller.setReverse(m_roller.getReverse() ? false : true), m_roller));

    // Climb commands:
    climbLeft.whenHeld(new ClimbLeftCommand(m_climber));
    climbRight.whenHeld(new ClimbRightCommand(m_climber));
    reverseClimb.whenPressed(new InstantCommand(() -> m_climber.setReverse(m_climber.getReverse() ? false : true), m_roller));

    activateVision.whenPressed(
      new InstantCommand(() -> ledManager.activateVision(ledManager.isVisionActivated() ? false : true))
    );

    // Runs the sequence Calculate -> Set LED -> Correct Position -> Correct Angle -> Shoot -> Reset angle -> Stop motors -> Set LED:
    shootUsingVision.whenHeld(getVisionCommand());

    // Other Shooter commands:
    shootFromTrench.whenHeld((new ShootFromTrenchCommand(m_shooter).beforeStarting(() -> ledManager.setState(State.SHOOTER_TRENCH), ledManager))
      .andThen(new InstantCommand(() -> ledManager.setState(State.TELEOP), ledManager)));
    shootFromPort.whenHeld((new ShootFromPortCommand(m_shooter).beforeStarting(() -> ledManager.setState(State.SHOOTER_PORT), ledManager))
      .andThen(new InstantCommand(() -> ledManager.setState(State.TELEOP), ledManager)));
    simpleShoot.whenHeld((new SimpleShootCommand(m_shooter).beforeStarting(() -> ledManager.setState(State.SHOOTER_SIMPLE), ledManager))
      .andThen(new InstantCommand(() -> ledManager.setState(State.TELEOP), ledManager)));

    // Shooter position commands:
    lowerShooter.whenPressed(new InstantCommand(() -> m_shooter.goToPosition(Position.LOWEST_POSITION), m_shooter));
    resetShooter.whenPressed(new InstantCommand(() -> m_shooter.goToPosition(Position.STARTING_CONFIGURATION), m_shooter));

    // Abort commands:
    driverAbort.whenPressed(new InstantCommand(() -> CommandScheduler.getInstance().cancelAll()));
    operatorAbort.whenPressed(new InstantCommand(() -> CommandScheduler.getInstance().cancelAll()));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @param path to use in autonomous routine.
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand(Path path) {
    Trajectory trajectory;
    trajectory = PathManager.generateTrajectory(path);

    m_drive.resetOdometry(PathManager.getStartingPosition(path));

    Command auto = getTrajectoryCommand(trajectory).andThen(getVisionCommand());
    return auto;
  }

  /**
   * This method returns {@link RamseteCommand} based on a given trajectory.
   * @param path as a {@link Trajectory} object.
   * @return Trajectory command.
   */
  public Command getTrajectoryCommand(Trajectory trajectory) {
    // Checks if a valid Trajectory is given, if not returns a new PrintCommand.
    if(trajectory == null) {
      return new PrintCommand("Invalid Trajectory given - Error or a Calculation result!");
    }
    
    // RamseteCommand generation:
    RamseteCommand trajectoryCommand = new RamseteCommand(
      trajectory, 
      m_drive::getPose, 
      new RamseteController(Constants.DrivetrainConstants.kRamseteB, Constants.DrivetrainConstants.kRamseteZeta), 
      new SimpleMotorFeedforward(Constants.DrivetrainConstants.ksVolts,
                                 Constants.DrivetrainConstants.ksVoltSecondsPerMeter,
                                 Constants.DrivetrainConstants.ksVoltSecondsSquaredPerMeter), 
      Constants.DrivetrainConstants.kDriveKinematics, 
      m_drive::getWheelSpeeds, 
      new PIDController(Constants.DrivetrainConstants.kP, 0, 0), 
      new PIDController(Constants.DrivetrainConstants.kP, 0, 0), 
      m_drive::setVoltage, 
      m_drive
    );

    return trajectoryCommand;
  }

  /**
   * This method returns {@link Command} used for shooting using vision, based on a {@link Calculation} generated
   * by the {@link VisionController} class.
   * @return Vision command.
   */
  public Command getVisionCommand() {
    Command visionCommand = new InstantCommand(() -> m_calculation = m_vision.calculate(m_shooter.getAngle(), m_drive.getPose()))
    .andThen(
      new InstantCommand(() -> ledManager.setState(State.SHOOTER_VISION), ledManager),
      new RunCommand(() -> getTrajectoryCommand(m_calculation.getPath()), m_drive),
      new InstantCommand(() -> m_shooter.goToAngle(m_calculation.getAngle(), false), m_shooter),
      new InstantCommand(() -> m_shooter.shoot(m_calculation.getVelocity()), m_shooter),
      new WaitCommand(4.5),
      new InstantCommand(() -> m_shooter.stopAll(), m_shooter),
      new InstantCommand(() -> m_shooter.goToPosition(Position.STARTING_CONFIGURATION), m_shooter),
      new InstantCommand(() -> ledManager.setState(DriverStation.getInstance().isOperatorControl() ? State.TELEOP : State.AUTO), ledManager)
    );
    
    return visionCommand;
  }

}