package com.acpi.mls.missionlunarspace;


import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Timer {

    public static Timer timer;
    private static long[] tempsParPhase = {10 * 60, 5 * 60, 15 * 60, 10 * 60, 5 * 60, 5 * 60, 10 * 60, 5 * 60};
    private static long tempsTotal = 60 * 60;
    private long[] tempsDepart = new long[3];

    private TextView mTextViewCountDown;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private AppCompatActivity activity;

    private long mTimeLeftInMillis;
    private int phase;

    public Timer(String tempsNonFormate) {
        formatTime(tempsNonFormate);
        this.phase = 0;
    }

    public static Timer createTimer(String tempsNonFormate) {
        Timer.timer = new Timer(tempsNonFormate);
        return timer;
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
        mTimeLeftInMillis = getTimeLeft(phase);
        startTimer();
    }

    public void ajouterPhaseEtDemarrer() {
        this.phase++;
        setTimeLeftEtDemarrer(phase);
    }

    private long getTimeLeft(int phase) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String[] tempsDep = sdf.format(cal.getTime()).split(":");
        int[] current = {Integer.parseInt(tempsDep[0]) + 1, Integer.parseInt(tempsDep[1]), Integer.parseInt(tempsDep[2])};

        long heuresEnSec = (current[0] - tempsDepart[0]) * 3600;
        long minutesEnSec = (current[1] - tempsDepart[1]) * 60;
        long secondesEnSec = (current[2] - tempsDepart[2]) * 1;
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
        this.mCountDownTimer = mCountDownTimer;
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

    public void startTimer() {
        setmCountDownTimer(new CountDownTimer(getmTimeLeftInMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setmTimeLeftInMillis(millisUntilFinished);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                setmTimerRunning(false);
                /* TODO : Enlever le commentaire
                switch (Timer.getInstance().phase) {
                    case 1:
                        ((ChoixPersoActivity) Timer.getInstance().getActivity()).continuerChoixGroupe(null);
                        break;
                    case 2:
                        ((ChoixGroupeActivity) Timer.getInstance().getActivity()).continuerRole(null);
                        break;
                    case 3:
                        ((ChoixGroupeActivity) Timer.getInstance().getActivity()).changementDePhase(null);
                        break;
                    case 4:
                        ((ChoixGroupeActivity) Timer.getInstance().getActivity()).changementDePhase(null);
                        break;
                    case 5:
                        ((ChoixGroupeActivity) Timer.getInstance().getActivity()).changementDePhase(null);
                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:

                        break;
                }
                */
            }
        }.start());

        setmTimerRunning(true);
    }

    public void pauseTimer() {
        getmCountDownTimer().cancel();
        setmTimerRunning(false);
    }

    public void updateCountDownText() {
        int minutes = (int) (getmTimeLeftInMillis() / 1000) / 60;
        int seconds = (int) (getmTimeLeftInMillis() / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        getmTextViewCountDown().setText(timeLeftFormatted);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }
}
