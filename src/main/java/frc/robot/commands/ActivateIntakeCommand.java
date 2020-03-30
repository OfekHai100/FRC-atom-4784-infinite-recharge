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
import frc.robot.subsystems.Shooter.Position;

public class ActivateIntakeCommand extends CommandBase {
  
  private final Roller m_roller;
  private final Shooter m_shooter;

  private boolean m_reverse;
  
  /**
   * Creates a new ActivateIntakeCommand.
   */
  public ActivateIntakeCommand(Roller roller, Shooter shooter) {
    m_roller = roller;
    m_shooter = shooter;
    m_reverse = roller.getReverse();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(roller, shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(!m_reverse) {
      m_shooter.goToPosition(Position.LOADING);
      m_roller.openRoller();
    } else {
      m_shooter.goToPosition(Position.LOWEST_POSITION);
      m_roller.closeRoller();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
