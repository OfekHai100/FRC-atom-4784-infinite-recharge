/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.PrintCommand;

/**
 * Customized, better {@link VictorSPX}.
 */
public class AtomVictor extends VictorSPX {

    private int m_port;

    /**
     * Constructor for AtomVictor.
     * @param port of the {@link VictorSPX}.
     */
    public AtomVictor(int port) {
        super(port);
        m_port = port;
        super.configFactoryDefault();

        super.configVoltageCompSaturation(11.0);
        super.enableVoltageCompensation(true);
    }

    /**
     * Constructor for AtomVictor with invert type parameter.
     * @param port of the {@link VictorSPX}.
     * @param isReversed - True of false.
     */
    public AtomVictor(int port, boolean isReversed) {
        super(port);
        m_port = port;
        super.configFactoryDefault();

        super.setInverted(isReversed);

        super.configVoltageCompSaturation(11.0);
        super.enableVoltageCompensation(true);
    }

    /**
     * Constructor for AtomVictor when following master motor.
     * @param port of the {@link VictorSPX}.
     * @param master to follow.
     * @param opposeMaster - Invert type: Follow master's invert (false) or oppose it (true).
     */
    public AtomVictor(int port, IMotorController master, boolean opposeMaster) {
        super(port);
        m_port = port;
        super.configFactoryDefault();

        super.follow(master);
        if(opposeMaster) {
            super.setInverted(InvertType.OpposeMaster);
        } else {
            super.setInverted(InvertType.FollowMaster);
        }

        super.configVoltageCompSaturation(11.0);
        super.enableVoltageCompensation(true);
    }

    /**
     * Debug method - Prints the output of the {@link VictorSPX}.
     */
    public void debug() {
        new PrintCommand("The output of VictorSPX in port " + m_port + " is: " + super.getMotorOutputPercent());
    }

}
