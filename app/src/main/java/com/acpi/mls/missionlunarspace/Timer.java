package com.acpi.mls.missionlunarspace;


import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.refresh.check.DAOCheckEtape;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public abstract class Timer {
    public static Timer timer;
    private static long[] tempsParPhase = {1, 1, 1, 1, 1, 1};
    //private static long[] tempsParPhase = {10 * 15, 15 * 15, 10 * 15, 5 * 15, 5 * 15, 10 * 15};
    private static long tempsTotal = 55 * 60;
    private long[] tempsDepart = new long[3];
    private long[] tempsDecalage = new long[3];

    protected TextView mTextViewCountDown;

    private static CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private AppCompatActivity activity;

    private long mTimeLeftInMillis;
    private int phase;

    public Timer(String tempsNonFormate) {
        formatTime(tempsNonFormate);
        faireDecalage();
        this.phase = 0;
    }

    private void faireDecalage() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String[] tempsDep = sdf.format(cal.getTime()).split(":");
        tempsDecalage[0] = tempsDepart[0] - Long.parseLong(tempsDep[0]);
        tempsDecalage[1] = tempsDepart[1] - Long.parseLong(tempsDep[1]);
        tempsDecalage[2] = tempsDepart[2] - Long.parseLong(tempsDep[2]);
    }

    public static Timer getInstance() {
        return timer;
    }

    private void formatTime(String tempsNonFormate) {
        String[] tempsDep = tempsNonFormate.split(" ")[1].split(":");
        tempsDepart[0] = Integer.parseInt(tempsDep[0]);
        tempsDepart[1] = Integer.parseInt(tempsDep[1]);
        tempsDepart[2] = Integer.parseInt(tempsDep[2]);
    }

    public void setTimeLeftEtDemarrer(int phase) {
        mTimeLeftInMillis = tempsParPhase[phase - 1] * 1000;
        startTimer();
    }

    public void ajouterPhaseEtDemarrer() {
        this.phase++;
        setTimeLeftEtDemarrer(phase);
    }

    public abstract void startTimer();

    private long getTimeLeft(int phase) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String[] tempsDep = sdf.format(cal.getTime()).split(":");
        int[] current = {Integer.parseInt(tempsDep[0]), Integer.parseInt(tempsDep[1]), Integer.parseInt(tempsDep[2])};

        long heuresEnSec = ((current[0] + tempsDecalage[0]) - tempsDepart[0]) * 3600;
        long minutesEnSec = ((current[1] + tempsDecalage[1]) - tempsDepart[1]) * 60;
        long secondesEnSec = ((current[2] + tempsDecalage[2]) - tempsDepart[2]) * 1;
        return (getTimePhase(phase) - (heuresEnSec + minutesEnSec + secondesEnSec)) * 1000;
    }

    private long getTimePhase(int phase) {
        switch (phase) {
            case 0:
                return tempsTotal;
            default:
                long val = 0L;
                for (int i = 0; i < phase; i++) {
                   val += tempsParPhase[i];
                }
                return val;
        }
    }

    public TextView getmTextViewCountDown() {
        return mTextViewCountDown;
    }

    public void setTextView(TextView mTextViewCountDown) {
        this.mTextViewCountDown = mTextViewCountDown;
    }

    public CountDownTimer getmCountDownTimer() {
        return mCountDownTimer;
    }

    public void setmCountDownTimer(CountDownTimer mCountDownTimer) {
        Timer.mCountDownTimer = mCountDownTimer;
    }

    public boolean ismTimerRunning() {
        return mTimerRunning;
    }

    public void setmTimerRunning(boolean mTimerRunning) {
        this.mTimerRunning = mTimerRunning;
    }

    public long getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public void setmTimeLeftInMillis(long mTimeLeftInMillis) {
        this.mTimeLeftInMillis = mTimeLeftInMillis;
    }

    public void pauseTimer() {
        getmCountDownTimer().cancel();
        setmTimerRunning(false);
    }

    public void updateCountDownText() {
        int minutes = (int) (getmTimeLeftInMillis() / 1000) / 60;
        int seconds = (int) (getmTimeLeftInMillis() / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "Temps: %02d:%02d", minutes, seconds);
        getmTextViewCountDown().setText(timeLeftFormatted);
    }

    public abstract void setActivity(AppCompatActivity activity);

    public abstract AppCompatActivity getActivity();

    public int getPhase() {
        return this.phase;
    }
}
