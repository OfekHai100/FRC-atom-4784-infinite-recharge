/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.Position;

public class ShootFromPortCommand extends CommandBase {
  
  private Shooter m_shooter;

  /**
   * Creates a new ShootFromPortCommand.
   */
  public ShootFromPortCommand(Shooter shooter) {
    m_shooter = shooter;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_shooter.goToPosition(Position.PORT);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooter.shoot(0.5);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooter.goToPosition(Position.STARTING_CONFIGURATION);
    m_shooter.stopAll();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
