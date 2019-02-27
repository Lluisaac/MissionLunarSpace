package com.acpi.mls.missionlunarspace;


import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.autre.DAORecupTemps;

import java.util.Locale;


public class Timer {

    public static Timer timer;
    private static long[] tempsParPhase = {10 * 15, 15 * 15, 10 * 15, 5 * 15, 5 * 15, 10 * 15};
    private static long tempsTotal = 55 * 60;
    private long[] tempsDepart = new long[3];

    private TextView mTextViewCountDown;

    private static CountDownTimer mCountDownTimer;

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

    public void ajouterPhaseEtDemarrer() {
        this.phase++;
        setTimeLeftEtDemarrer(phase);
    }

    public void setTimeLeftEtDemarrer(int phase) {
        faireDemandeTemps(phase);
    }

    private void faireDemandeTemps(int phase) {
        new DAORecupTemps(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "simple", phase + "");
    }

    public void postTimeLeft(int phase, String[] time) {
        mTimeLeftInMillis = getTimeLeft(phase, time);
        startTimer();
    }

    private long getTimeLeft(int phase, String[] time) {
        tempsDepart[0] = Integer.parseInt(time[0]);
        tempsDepart[1] = Integer.parseInt(time[1]);
        tempsDepart[2] = Integer.parseInt(time[2]);

        long heuresEnSec = tempsDepart[0] * 3600;
        long minutesEnSec = tempsDepart[1] * 60;
        long secondesEnSec = tempsDepart[2] * 1;
        return (getTimePhase(phase) - (heuresEnSec + minutesEnSec + secondesEnSec)) * 1000;
    }

    private long getTimePhase(int phase) {
        return tempsParPhase[phase];
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
                switch (Timer.getInstance().phase) {
                    case 1:
                        //On se trouve dans le classement Perso et le timer finit: on va dans une attente
                        ((ChoixPersoActivity) Timer.getInstance().getActivity()).continuerChoixGroupe(null);
                        break;
                    case 2:
                        //On se trouve dans le classement Groupe p1 et le timer finit: on passe a l'étape suivante
                        ((ChoixGroupeActivity) Timer.getInstance().getActivity()).changementDePhase(null);
                        break;
                    case 3:
                        //On se trouve dans le classement Groupe p2 et le timer finit: on passe a l'étape suivante
                        ((ChoixGroupeActivity) Timer.getInstance().getActivity()).changementDePhase(null);
                        break;
                    case 4:
                        //On se trouve dans le classement Groupe p3 et le timer finit: on passe a l'étape suivante
                        ((ChoixGroupeActivity) Timer.getInstance().getActivity()).changementDePhase(null);
                        break;
                    case 5:
                        //On se trouve dans le classement Groupe p4 et le timer finit: on va dans une attente
                        ((ChoixGroupeActivity) Timer.getInstance().getActivity()).passageAttenteClasse();
                        break;
                    case 6:
                        //On se trouve dans le classement Classe et le timer finit: on va dans une attente
                        ((ChoixClasseActivity) Timer.getInstance().getActivity()).passageAttenteDenonciation(null);
                        break;
                }

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
