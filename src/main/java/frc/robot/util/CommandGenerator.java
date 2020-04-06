/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;

import frc.robot.Constants;
import frc.robot.pathing.PathManager;
import frc.robot.pathing.PathManager.Path;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LED;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.LED.State;
import frc.robot.subsystems.Shooter.Position;
import frc.robot.vision.Calculation;
import frc.robot.vision.VisionController;

/**
 * This utility class generates different types of {@link Command}s.
 */
public class CommandGenerator {

    // Java...
    private static Calculation m_calculation;

    /**
     * This method returns {@link RamseteCommand} based on a given trajectory.
     * @param trajectory as a {@link Trajectory} object.
     * @return Trajectory command.
     */
    public static Command generateTrajectoryCommand(Trajectory trajectory) {
        Drivetrain drive = Drivetrain.getInstance();

        // Checks if a valid Trajectory is given, if not returns a new PrintCommand.
        if(trajectory == null) {
            return new PrintCommand("Invalid Trajectory given - Error or a Calculation result!");
        }
      
        // RamseteCommand generation:
        RamseteCommand trajectoryCommand = new RamseteCommand(
            trajectory, 
            drive::getPose, 
            new RamseteController(Constants.DrivetrainConstants.kRamseteB, Constants.DrivetrainConstants.kRamseteZeta), 
            new SimpleMotorFeedforward(Constants.DrivetrainConstants.ksVolts,
                                       Constants.DrivetrainConstants.ksVoltSecondsPerMeter,
                                       Constants.DrivetrainConstants.ksVoltSecondsSquaredPerMeter), 
            Constants.DrivetrainConstants.kDriveKinematics, 
            drive::getWheelSpeeds, 
            new PIDController(Constants.DrivetrainConstants.kP, 0, 0), 
            new PIDController(Constants.DrivetrainConstants.kP, 0, 0), 
            drive::setVoltage, 
            drive
        );
  
        return trajectoryCommand;
    }

    /**
     * This method returns {@link Command} used for shooting using vision, based on a {@link Calculation} generated
     * by the {@link VisionController} class.
     * @return Vision command.
     */
    public static Command generateVisionCommand() {
        VisionController controller = new VisionController();

        Drivetrain drive = Drivetrain.getInstance();
        Shooter shooter = Shooter.getInstance();
        LED led = LED.getInstance();

        Command visionCommand = new InstantCommand(() -> { m_calculation = controller.calculate(shooter.getAngle(), drive.getPose()); })
        .andThen(
            new InstantCommand(() -> led.setState(State.SHOOTER_VISION), led),
            new RunCommand(() -> generateTrajectoryCommand(m_calculation.getPath()), drive),
            new InstantCommand(() -> shooter.goToAngle(m_calculation.getAngle(), false), shooter),
            new InstantCommand(() -> shooter.shoot(m_calculation.getVelocity(), false), shooter),
            new InstantCommand(() -> shooter.stopAll(), shooter),
            new InstantCommand(() -> shooter.goToPosition(Position.STARTING_CONFIGURATION), shooter),
            new InstantCommand(() -> led.setState(DriverStation.getInstance().isOperatorControl() ? State.TELEOP : State.AUTO), led)
        );

    return visionCommand;
    }

    /**
     * This method returns {@link Command} for use as Autonomous routine.
     * @param path to follow, as a {@link Path} object.
     * @return Auto-command.
     */
    public static Command generateAutoCommand(Path path) {
        Drivetrain drive = Drivetrain.getInstance();

        Trajectory trajectory;
        trajectory = PathManager.generateTrajectory(path);

        drive.resetOdometry(PathManager.getStartingPosition(path));

        Command auto = generateTrajectoryCommand(trajectory).andThen(generateVisionCommand());
        return auto;
    }

}
