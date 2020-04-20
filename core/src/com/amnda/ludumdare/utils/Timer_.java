package com.amnda.ludumdare.utils;

import com.badlogic.gdx.Gdx;

public class Timer_ {
    // Time ratio definitions
    public static final float REAL_TIME = 1;
    public static final float DEMO_TIME = 3600; // 1 real sec = 1 hour
    public static final float GAMETIME = 600; // 1 real sec = 10 min

    // How long timer's been running
    private double secondsSinceStart;
    // How long the timer should run
    private double runTimeSeconds;

    private float realToTimerRatio;

    private boolean paused;
    // Chrono timers have no end time
    private boolean chrono;

    private int daysPassed;

    public Timer_() {
        this.runTimeSeconds = 0;
        this.secondsSinceStart = 0;
        this.daysPassed = 0;
        this.paused = false;
        this.realToTimerRatio = Timer_.GAMETIME;
        this.chrono = false;
    }

    // Called every frame
    public void tick() {
        if (!isDone() || chrono) {
            if (!paused) {
                secondsSinceStart += (Gdx.graphics.getDeltaTime() * realToTimerRatio);

                if (chrono) {
                    if (secondsSinceStart > 86400) {
                        secondsSinceStart -= 86400;
                        daysPassed++;
                    }
                }
            }
        }
    }

    public void setStartTime(int days, int hour, int min, int sec) {
        daysPassed = days;
        secondsSinceStart = sec + (min * 60) + (hour * 3600);
    }

    public void setToChrono() {
        chrono = true;
    }

    public void setTimeRatio(float ratio) {
        realToTimerRatio = ratio;
    }

    public boolean isDone() {
        if (secondsSinceStart >= runTimeSeconds)
            return true;
        return false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        paused = true;
    }

    public void start() {
        paused = false;
    }

    public String getFormattedElapsed() {
        return format(secondsSinceStart);
    }

    public String getFormattedRemaining() {
        return format(runTimeSeconds - secondsSinceStart);
    }

    public String getFormattedTimeOfDay() {
        int hours = (int) Math.floor(secondsSinceStart / 3600);
        String sub = " ";
        if (hours == 0) {
            hours = 12;
            sub = sub.concat("AM");
        } else if (hours >= 12) {
            if (hours > 12)
                hours -= 12;
            sub = sub.concat("PM");
        } else if ((hours < 12) && (hours > 0)) {
            sub = sub.concat("AM");
        }

        int minutes = (int) Math.floor((secondsSinceStart % 3600) / 60);

        String res = String.format("%1$02d:%2$02d", hours, minutes);
        return res.concat(sub);
    }

    private String format(double d) {
        int hours = (int) Math.floor(d / 3600);
        int minutes = (int) Math.floor((d % 3600) / 60);
        double seconds = d % 60;

        return String.format("%1$02d:%2$02d:%3$03.1f", hours, minutes, seconds);
    }

    public double getElapsedInSeconds() {
        return secondsSinceStart;
    }

    public double getElapsedInMinutes() {
        return (secondsSinceStart / 60);
    }

    public double getElapsedInHours() {
        return (secondsSinceStart / 3600);
    }

    public double getRemaining() {
        return (runTimeSeconds - secondsSinceStart);
    }

    public int getDaysPassed() {
        return daysPassed;
    }

    public void startNew(float runTimeInSeconds, boolean startNow, boolean chronological) {
        runTimeSeconds = runTimeInSeconds;
        secondsSinceStart = 0;
        paused = !startNow;
        chrono = chronological;
    }

    public void setDaysPassed(int days) {
        daysPassed = days;
    }

    public float getTimeRatio() {
        return realToTimerRatio;
    }
}
