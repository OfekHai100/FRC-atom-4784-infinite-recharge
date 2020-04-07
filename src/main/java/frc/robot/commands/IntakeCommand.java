/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Roller;
import frc.robot.subsystems.Shooter;

public class IntakeCommand extends CommandBase {
  
  private final Roller m_roller;
  private final Shooter m_shooter;

  private boolean m_reverse;
  
  /**
   * Creates a new IntakeCommand.
   */
  public IntakeCommand() {
    m_roller = Roller.getInstance();
    m_shooter = Shooter.getInstance();
    m_reverse = m_roller.isReversed();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_roller, m_shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_roller.intake();
    m_shooter.load(m_reverse);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_roller.stop();
    m_shooter.stopLoader();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
