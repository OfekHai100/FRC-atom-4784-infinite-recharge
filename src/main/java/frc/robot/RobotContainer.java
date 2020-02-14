/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.List;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Roller;
import frc.robot.subsystems.Shooter;
import frc.robot.util.PSController;

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

  PSController driver = new PSController(Constants.Ports.kMain);
  PSController secondDriver  = new PSController(Constants.Ports.kSecond);

  JoystickButton intake = new JoystickButton(driver, PSController.getL2());

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
    m_drive.setDefaultCommand(
      new RunCommand(() -> m_drive.arcade(-driver.getY(), driver.getX()), m_drive)
    );

    intake.whenHeld(new IntakeCommand(m_roller));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    var voltageConstraint =
      new DifferentialDriveVoltageConstraint(
        new SimpleMotorFeedforward(Constants.DrivetrainConstants.ksVolts,
                                  Constants.DrivetrainConstants.ksVoltSecondsPerMeter,
                                  Constants.DrivetrainConstants.ksVoltSecondsSquaredPerMeter),
        Constants.DrivetrainConstants.kDriveKinematics,
        10);

    TrajectoryConfig config = new TrajectoryConfig(Constants.DrivetrainConstants.kMaxSpeed, Constants.DrivetrainConstants.kMaxAcceleration)
      .setKinematics(Constants.DrivetrainConstants.kDriveKinematics).addConstraint(voltageConstraint);

    Trajectory path = TrajectoryGenerator.generateTrajectory(
      new Pose2d(0, 0, new Rotation2d(0)),
      List.of(
        new Translation2d(1, 1),
        new Translation2d(2, -1)
      ), 
      new Pose2d(3, 0, new Rotation2d(0)), 
      config);

    RamseteCommand command = new RamseteCommand(
      path, 
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
      m_drive);

    return command;
  }
}