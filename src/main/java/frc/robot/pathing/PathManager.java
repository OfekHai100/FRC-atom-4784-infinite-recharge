/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.pathing;

import java.io.IOException;
import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;

/**
 * Class that handls Pathweaver paths.
 */
public class PathManager {

    /**
     * This enum stores all paths.
     */
    public enum Path {
        RED_PORT("paths/RedPort.wpilib.json"),
        RED_MIDDLE("paths/RedMiddle.wpilib.json"),
        RED_BLUE_FEEDER("paths/RedBlueFeeder.wpilib.json"),
        BLUE_PORT("paths/BluePort.wpilib.json"),
        BLUE_MIDDLE("paths/BlueMiddle.wpilib.json"),
        BLUE_RED_FEEDER("paths/BlueRedFeeder.wpilib.json");

        private String m_json;

        Path(String json) {
            this.m_json = json;
        }

        public String getJSON() {
            return this.m_json;
        }
    }

    /**
     * The main method in the PathManager class. It generates a {@link Trajectory} based on a selected {@link Path} for
     * autonomous routine.
     * @param path
     * @return Generated {@link Trajectory}.
     */
    public static Trajectory generateTrajectory(Path path) {
        String json = path.getJSON();
        Trajectory finalTrajectory;
        
        try {
            java.nio.file.Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(json);
            finalTrajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch(IOException ex) {
            DriverStation.reportError("Can not generate trajectory! " + json, ex.getStackTrace());
            finalTrajectory = null;
        }

        return finalTrajectory;
    }

    /**
     * Returns 'S' shaped {@link Trajectory}, used for testing.
     * @return 'S' shaped {@link Trajectory}.
     */
    public static Trajectory getSTrajectory() {
        Configuration config = Configuration.getConfiguration();
        
        Trajectory path = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(
                new Translation2d(1, 1),
                new Translation2d(2, -1)
            ), 
            new Pose2d(3, 0, new Rotation2d(0)), 
            config.getConfig());

        return path;
    }

    /**
     * Returns the starting position of the Robot, based on a selected {@link Path}.
     * @param path selected.
     * @return Starting position, as a {@link Pose2d}.
     */
    public static Pose2d getStartingPosition(Path path) {
        Pose2d position;

        switch(path) {
            case RED_PORT:
                position = new Pose2d(new Translation2d(3.019, -2.453), new Rotation2d(-1, 0));
            case RED_MIDDLE:
                position = new Pose2d(new Translation2d(3.019, -5.046), new Rotation2d(-1, 0));
            case RED_BLUE_FEEDER:
                position = new Pose2d(new Translation2d(3.019, -5.641), new Rotation2d(-1, 0));
            case BLUE_PORT:
                position = new Pose2d(new Translation2d(13.013, -5.796), new Rotation2d(1, 0));
            case BLUE_MIDDLE:
                position = new Pose2d(new Translation2d(13.013, -3.309), new Rotation2d(1, 0));
            default:
                // = case Blue_Red_Feeder:
                position = new Pose2d(new Translation2d(13.013, -2.56), new Rotation2d(1, 0));
        }

        return position;
    }

}
