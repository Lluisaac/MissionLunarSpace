package com.acpi.mls.missionlunarspace;


import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.refresh.check.DAOCheckEtape;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


public class Timer {

    public static Timer timer;
    private static long[] tempsParPhase = {10 * 2, 15 * 2, 10 * 2, 5 * 2, 5 * 2, 10 * 2};
    private static long tempsTotal = 55 * 60;
    private long[] tempsDepart = new long[3];
    private long[] tempsDecalage = new long[3];

    private TextView mTextViewCountDown;

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

    public static Timer createTimer(String tempsNonFormate) {
        Timer.timer = new Timer(tempsNonFormate);
        return timer;
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
        mTimeLeftInMillis = tempsParPhase[phase] * 1000;
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
                        ChoixPersoActivity act1 = ((ChoixPersoActivity) Timer.getInstance().getActivity());
                        new DAOCheckEtape(act1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act1.getIdGroupe() + "", "1");
                        break;
                    case 2:
                        //On se trouve dans le classement Groupe p1 et le timer finit: on passe a l'attente de l'étape suivante
                        ChoixGroupeActivity act2 = ((ChoixGroupeActivity) Timer.getInstance().getActivity());
                        new DAOCheckEtape(act2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act2.getIdGroupe() + "", "3");
                        break;
                    case 3:
                        //On se trouve dans le classement Groupe p2 et le timer finit: on passe a l'attente de l'étape suivante
                        ChoixGroupeActivity act3 = ((ChoixGroupeActivity) Timer.getInstance().getActivity());
                        new DAOCheckEtape(act3).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act3.getIdGroupe() + "", "5");
                        break;
                    case 4:
                        //On se trouve dans le classement Groupe p3 et le timer finit: on passe a l'attente de l'étape suivante
                        ChoixGroupeActivity act4 = ((ChoixGroupeActivity) Timer.getInstance().getActivity());
                        new DAOCheckEtape(act4).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act4.getIdGroupe() + "", "7");
                        break;
                    case 5:
                        //On se trouve dans le classement Groupe p4 et le timer finit: on va dans l'attente du classement de classe
                        ChoixGroupeActivity act5 = ((ChoixGroupeActivity) Timer.getInstance().getActivity());
                        new DAOCheckEtape(act5).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act5.getIdGroupe() + "", "9");
                        break;
                    case 6:
                        //On se trouve dans le classement Classe et le timer finit: on va dans l'attente de l'enquete
                        ChoixClasseActivity act6 = ((ChoixClasseActivity) Timer.getInstance().getActivity());
                        new DAOCheckEtape(act6).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act6.getIdGroupe() + "", "11");
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

        String timeLeftFormatted = String.format(Locale.getDefault(), "Temps: %02d:%02d", minutes, seconds);
        getmTextViewCountDown().setText(timeLeftFormatted);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }
}
