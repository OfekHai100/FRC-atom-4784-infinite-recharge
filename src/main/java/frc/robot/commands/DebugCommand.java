/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Roller;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.Position;

/**
 * This command checks all subsystems in order. It may be used for debugging purposes or pre-match check.
 * <p> To activate this command, change m_inDebugMode to true in the RobotContainer class, the command
 * will be controlled by the operator with the UP button on the controller.
 */
public class DebugCommand extends SequentialCommandGroup {

  private Climber m_climber;
  private Drivetrain m_drive;
  private Roller m_roller;
  private Shooter m_shooter;

  /**
   * Creates a new DebugCommand.
   * @param inSafeMode - If in safe mode the Drivetrain will not move at all during Debug Mode,
   *  meaning Drivetrain debugging is not active.
   */
  public DebugCommand(boolean inSafeMode) {
    m_climber = Climber.getInstance();
    m_drive = Drivetrain.getInstance();
    m_roller = Roller.getInstance();
    m_shooter = Shooter.getInstance();
    
    addCommands(
      new PrintCommand("ENTERING DEBUG MODE!"),
      
      // Climber:
      new InstantCommand(() -> m_climber.setReverse(false), m_climber),
      new InstantCommand(() -> m_climber.climbLeft(), m_climber),
      new WaitCommand(0.7),
      new PrintCommand("CLIMBED LEFT SIDE!"),
      new InstantCommand(() -> m_climber.climbRight(), m_climber),
      new WaitCommand(0.7),
      new PrintCommand("CLIMBED RIGHT SIDE!"),
      new InstantCommand(() -> m_climber.setReverse(true), m_climber),
      new InstantCommand(() -> m_climber.climbLeft(), m_climber),
      new WaitCommand(0.7),
      new PrintCommand("REVERSE CLIMBED LEFT SIDE!"),
      new InstantCommand(() -> m_climber.climbRight(), m_climber),
      new WaitCommand(0.7),
      new PrintCommand("REVERSE CLIMBED RIGHT SIDE!"),
      new InstantCommand(() -> m_climber.stopLeft(), m_climber),
      new InstantCommand(() -> m_climber.stopRight(), m_climber),
      new WaitCommand(2.5),

      // Drivetrain:
      new PrintCommand(inSafeMode ? "SAFE MODE ACTIVE!" : "DRIVETRAIN DISTANCE: " + m_drive.getDistance()),
      new InstantCommand(() -> m_drive.arcade(inSafeMode ? 0.0 : 0.5, 0), m_drive),
      new WaitCommand(inSafeMode ? 0.0 : 2.0),
      new PrintCommand(inSafeMode ? "" : "NEW DRIVETRAIN DISTANCE: " + m_drive.getDistance()),
      new WaitCommand(inSafeMode ? 0.0 : 1.5),
      new PrintCommand(inSafeMode ? "" : "ROBOT HEADING: " + m_drive.getHeading()),
      new InstantCommand(() -> m_drive.arcade(0, inSafeMode ? 0.0 : 0.5), m_drive),
      new WaitCommand(inSafeMode ? 0.0 : 2.0),
      new PrintCommand(inSafeMode ? "" : "NEW ROBOT HEADING: " + m_drive.getHeading()),
      new InstantCommand(() -> m_drive.stop()),
      new WaitCommand(inSafeMode ? 0.0 : 2.5),

      // Roller:
      new InstantCommand(() -> m_roller.activateIntakeMode(true), m_roller),
      new ActivateIntakeCommand(m_roller, m_shooter),
      new PrintCommand("ACTIVATED INTAKE!"),
      new WaitCommand(1.0),
      new InstantCommand(() -> m_roller.setReverse(false), m_roller),
      new InstantCommand(() -> m_roller.intake(), m_roller),
      new WaitCommand(2.0),
      new PrintCommand("INTAKE FINISHED!"),
      new InstantCommand(() -> m_roller.setReverse(false), m_roller),
      new InstantCommand(() -> m_roller.intake(), m_roller),
      new WaitCommand(2.0),
      new PrintCommand("REVERSE INTAKE FINISHED!"),
      new WaitCommand(1.0),
      new InstantCommand(() -> m_roller.activateIntakeMode(false), m_roller),
      new ActivateIntakeCommand(m_roller, m_shooter),
      new PrintCommand("DEACTIVATED INTAKE!"),
      new InstantCommand(() -> m_roller.stop(), m_roller),
      new WaitCommand(2.5),

      // Shooter:
      new PrintCommand("SHOOTER ANGLE: " + m_shooter.getAngle()),
      new PrintCommand("SHOOTER POSITION: " + m_shooter.getPositionAsString()),
      new WaitCommand(1.5),
      new InstantCommand(() -> m_shooter.goToPosition(Position.LOWEST_POSITION)),
      new PrintCommand("SHOOTER SWITCHED TO LOWEST POSITION"),
      new WaitCommand(1.5),
      new InstantCommand(() -> m_shooter.goToPosition(Position.TRENCH)),
      new PrintCommand("SHOOTER SWITCHED TO TRENCH"),
      new WaitCommand(1.5),
      new InstantCommand(() -> m_shooter.goToPosition(Position.STARTING_CONFIGURATION)),
      new PrintCommand("SHOOTER SWITCHED TO STARTING CONFIGURATION"),
      new WaitCommand(1.5),
      new InstantCommand(() -> m_shooter.goToPosition(Position.LOADING)),
      new PrintCommand("SHOOTER SWITCHED TO LOADING"),
      new WaitCommand(1.5),
      new InstantCommand(() -> m_shooter.goToPosition(Position.PORT)),
      new PrintCommand("SHOOTER SWITCHED TO PORT"),
      new WaitCommand(1.5),
      new InstantCommand(() -> m_shooter.goToPosition(Position.LOWEST_POSITION)),
      new PrintCommand("SHOOTER RETURNED TO LOWEST POSITION"),
      new WaitCommand(2.0),
      new InstantCommand(() -> m_shooter.load(false)),
      new PrintCommand("LOADER ACTIVATED!"),
      new WaitCommand(1.5),
      new InstantCommand(() -> m_shooter.load(true)),
      new PrintCommand("REVERSE LOADER ACTIVATED!"),
      new WaitCommand(2.0),
      new InstantCommand(() -> m_shooter.shoot(0.6, false)),
      new PrintCommand("SHOOTER SHOT ALL LOADED POWER CELLS WITH OUTPUT OF 0.6"),
      new WaitCommand(2.5),

      new PrintCommand("EXITING DEBUG MODE!")
    );
  }

}
