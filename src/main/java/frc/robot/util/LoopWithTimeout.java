/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.wpilibj.Timer;

/**
 * This class allows complex loop types (mainly closed-loop control tracking) via special Thread with timeout, to ensure no Run-Time
 * error is generated.
 * <p> Please note that in order to use this class, objects must be declared in a method used by a Command, in order to interact with 
 * the "ABORT" emergency function.
 * <p> In any case an object of this class goes wrong, the driver and the operator can stop the loop with the "ABORT" button.
 */
public class LoopWithTimeout {

    private Timer m_timer;
    private double m_timeout;
    private boolean m_elapsed;

    private String m_name;
    private boolean m_interrupted;

    /**
     * Constructor.
     * @param timeout in seconds.
     * @param name of the {@link LoopThread}.
     */
    public LoopWithTimeout(double timeout, String name) {
        this.m_timeout = timeout;
        this.m_elapsed = false;
        this.m_name = name;
        this.m_interrupted = false;
        m_timer = new Timer();
    }

    /**
     * Starts the {@link LoopThread} and the timed loop, until either the timeout achieved or the given loop has finished.
     */
    public void start() {
        LoopThread loopThread = new LoopThread(this.m_name);
        loopThread.start();

        m_timer.reset();
        m_timer.start();
        while(!m_elapsed || !m_interrupted) {
            m_elapsed = m_timer.hasElapsed(m_timeout);
            m_interrupted = loopThread.m_isPassed;
        }
    }

    /**
     * This method includes the loop we want to pass to the {@link LoopThread}.
     * <p> Call this method when declaring an object of LoopWithTimeout.
     */
    public void loop() {
    }


    /**
     * Returns the timeout.
     * @return Timeout, in seconds.
     */
    public double getTimeout() {
        return this.m_timeout;
    }

    /**
     * Is the timeout elapsed?
     * @return True if elapsed, false if not.
     */
    public boolean isElapsed() {
        return this.m_elapsed;
    }

    /**
     * Is the Timer interrupted by the loop (the loop finished)?
     * @return True if interrupted, false if not.
     */
    public boolean isInterrupted() {
        return this.m_interrupted;
    }

    /**
     * The LoopThread.
     */
    class LoopThread extends Thread {

        private boolean m_isPassed = false;

        public LoopThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            loop();
            this.m_isPassed = true;
        }

    }

}
