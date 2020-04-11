/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

import frc.robot.commands.ActivateIntakeCommand;
import frc.robot.commands.ClimbLeftCommand;
import frc.robot.commands.ClimbRightCommand;
import frc.robot.commands.DebugCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ShootFromPortCommand;
import frc.robot.commands.ShootFromTrenchCommand;
import frc.robot.commands.SimpleShootCommand;
import frc.robot.pathing.PathManager.Path;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.LED.State;
import frc.robot.subsystems.Roller;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.Position;
import frc.robot.util.CommandFactory;
import frc.robot.util.PSController;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Climber m_climber = Climber.getInstance();
  private final Drivetrain m_drive = Drivetrain.getInstance();
  private final Roller m_roller = Roller.getInstance();
  private final Shooter m_shooter = Shooter.getInstance();

  // LED:
  private final LED m_led = LED.getInstance();

  // Debug Mode:
  // Test Mode - Debug Mode: True, Safe Mode: False.
  // Competition Mode - Debug Mode: False, Safe Mode: True/False.
  // Pre-Match Check Mode - Debug Mode: True, Safe Mode: True.
  private boolean m_inDebugMode = true;
  private boolean m_inSafeMode = false;

  // Joysticks:
  PSController driver = new PSController(Constants.Ports.kDriver);
  PSController operator  = new PSController(Constants.Ports.kOpeator);

  // Driver buttons - Driver is in charge of driving, intake and climbing:
  Button activateIntake = new JoystickButton(driver, PSController.getTriangle());
  Button intake = new JoystickButton(driver, PSController.getL2());
  Button reverseIntake = new POVButton(driver, PSController.getUp());
  Button climbLeft = new JoystickButton(driver, PSController.getL1());
  Button climbRight = new JoystickButton(driver, PSController.getR1());
  Button reverseClimb = new POVButton(driver, PSController.getDown());
  Button lowerShooter = new JoystickButton(driver, PSController.getIx());
  Button driverAbort = new JoystickButton(driver, PSController.getPad());
  
  // Operator buttons - Operator is in charge of shooting:
  Button activateVision = new JoystickButton(operator, PSController.getTriangle());
  Button shootUsingVision = new JoystickButton(operator, PSController.getR2());
  Button shootFromTrench = new JoystickButton(operator, PSController.getR1());
  Button shootFromPort = new JoystickButton(operator, PSController.getL1());
  Button simpleShoot = new JoystickButton(operator, PSController.getSquare());
  Button resetShooter = new JoystickButton(operator, PSController.getIx());
  Button operatorAbort = new JoystickButton(operator, PSController.getPad());
  Button enterDebugMode = new POVButton(operator, PSController.getUp());

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
    intake.whenHeld(new IntakeCommand());
    reverseIntake.whenPressed(new InstantCommand(() -> m_roller.setReverse(m_roller.isReversed() ? false : true), m_roller));

    // Climb commands:
    climbLeft.whenHeld(new ClimbLeftCommand());
    climbRight.whenHeld(new ClimbRightCommand());
    reverseClimb.whenPressed(new InstantCommand(() -> m_climber.setReverse(m_climber.isReversed() ? false : true), m_climber));

    // Vision LED command:
    activateVision.whenPressed(
      new InstantCommand(() -> m_led.activateVision(m_led.isVisionActivated() ? false : true))
    );

    // The sequence: Calculate -> Set LED -> Correct Position -> Correct Angle -> Load -> Shoot -> Reset angle -> Stop motors -> Set LED:
    shootUsingVision.whenHeld(CommandFactory.createVisionCommand());

    // Other Shooter commands:
    shootFromTrench.whenHeld((new ShootFromTrenchCommand().beforeStarting(() -> m_led.setState(State.SHOOTER_TRENCH), m_led))
      .andThen(new InstantCommand(() -> m_led.setState(State.TELEOP), m_led)));
    shootFromPort.whenHeld((new ShootFromPortCommand().beforeStarting(() -> m_led.setState(State.SHOOTER_PORT), m_led))
      .andThen(new InstantCommand(() -> m_led.setState(State.TELEOP), m_led)));
    simpleShoot.whenHeld((new SimpleShootCommand().beforeStarting(() -> m_led.setState(State.SHOOTER_SIMPLE), m_led))
      .andThen(new InstantCommand(() -> m_led.setState(State.TELEOP), m_led)));

    // Shooter position commands:
    lowerShooter.whenPressed(new InstantCommand(() -> m_shooter.goToPosition(Position.LOWEST_POSITION), m_shooter));
    resetShooter.whenPressed(new InstantCommand(() -> m_shooter.goToPosition(Position.STARTING_CONFIGURATION), m_shooter));

    // Abort commands:
    driverAbort.whenPressed(new InstantCommand(() -> CommandScheduler.getInstance().cancelAll()).andThen(new PrintCommand("ABORT WAS USED!")));
    operatorAbort.whenPressed(new InstantCommand(() -> CommandScheduler.getInstance().cancelAll()).andThen(new PrintCommand("ABORT WAS USED!")));

    // Debug Mode:
    enterDebugMode.whenPressed(new ConditionalCommand(new DebugCommand(m_inSafeMode), 
                                                      new PrintCommand("DEBUG MODE IS NOT ACTIVATED!"), 
                                                      () -> m_inDebugMode)
    );

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand(Path path) {
    return CommandFactory.createAutonomousCommand(path);
  }

}