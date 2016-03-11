package com.toggle.katana2d;

import android.util.Log;

import static java.lang.System.nanoTime;

public class Timer {
    public static final int ONE_SECOND = 1000000000;
    public final int TARGET_FPS = 60;
    public final float OPTIMAL_TIME = 1f / TARGET_FPS;

    public Timer() {
        reset();
    }

    // Reset the timer
    public void reset() {
        mTotalTime = 0;
        mLastTime = nanoTime();
        mFps = mFrameCounter = mSecondCounter = 0;
    }

    // Get actual FPS calculated
    public int getFPS() {
        return mFps;
    }
    // Get total time elapsed since reset
    public float getTotalTime() { return mTotalTime/(float)ONE_SECOND; }

    public void update(TimerCallback callback) {

        // get current and delta times
        long currentTime = nanoTime();
        long deltaTime = currentTime - mLastTime;
        mFrameCounter++;
        mSecondCounter += deltaTime;

        if (mSecondCounter >= ONE_SECOND) {
            mSecondCounter -= ONE_SECOND;
            mFps = mFrameCounter;
            mFrameCounter = 0;
        }

        float dt = deltaTime/(float)ONE_SECOND;
        callback.update(dt);

        float difference = OPTIMAL_TIME - dt;
        if (difference > 4000/ONE_SECOND)    // we were too fast, deltaTime << OPTIMAL_TIME
            try {
                Thread.sleep((long)(difference*1000));   // sleep for rest of the time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    private long mLastTime, mTotalTime;
    private int mFps, mFrameCounter, mSecondCounter;
}