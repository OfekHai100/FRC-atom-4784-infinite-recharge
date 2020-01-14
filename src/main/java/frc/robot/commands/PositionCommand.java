/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Roulette;
import frc.robot.util.Dashboard;

public class PositionCommand extends CommandBase {
  
  private final Roulette m_roulette;
  private String m_gameData;
  
  /**
   * Creates a new ColorCommand.
   */
  public PositionCommand(Roulette roulette) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_roulette = roulette;
    addRequirements(m_roulette);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_roulette.openRoulette();
    m_gameData = Dashboard.getGameColor();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_gameData != null) {
      if(m_gameData == m_roulette.whatColor()) {
        m_roulette.stop();
      } else {
        m_roulette.rotate();
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_roulette.stop();
    m_roulette.closeRoulette();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
