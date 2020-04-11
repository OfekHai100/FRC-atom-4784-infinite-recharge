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
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(!m_reverse) {
      if(m_shooter.isInserted()) {
        Shooter.loadedPowerCells++;
        m_shooter.timedLoad(0.4); // Enough time to insert one Power Cell.
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Moves all loaded Power Cells to the Shooter head, there is enough room for 4 Power Cells in the loader.
    m_shooter.timedLoad(0.4 * (4 - Shooter.loadedPowerCells));
    m_roller.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
