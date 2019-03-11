package com.acpi.mls.missionlunarspace;


import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.refresh.check.DAOCheckEtape;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class TimerEtudiant extends Timer{

    private AppCompatActivity activity;

    private long mTimeLeftInMillis;
    private int phase;

    public TimerEtudiant(String tempsNonFormate) {
        super(tempsNonFormate);
    }

    public static TimerEtudiant createTimer(String tempsNonFormate) {
        TimerEtudiant.timer = new TimerEtudiant(tempsNonFormate);
        return (TimerEtudiant) timer;
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
                TimerEtudiant.this.mTextViewCountDown.setText("Temps fini");
                switch (TimerEtudiant.getInstance().getPhase()) {
                    case 1:
                        //On se trouve dans le classement Perso et le timer finit: on va dans une attente
                        ChoixPersoActivity act1 = ((ChoixPersoActivity) TimerEtudiant.getInstance().getActivity());
                        new DAOCheckEtape(act1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act1.getIdGroupe() + "", "1");
                        break;
                    case 2:
                        //On se trouve dans le classement Groupe p1 et le timer finit: on passe a l'attente de l'étape suivante
                        ChoixGroupeActivity act2 = ((ChoixGroupeActivity) TimerEtudiant.getInstance().getActivity());
                        new DAOCheckEtape(act2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act2.getIdGroupe() + "", "3");
                        break;
                    case 3:
                        //On se trouve dans le classement Groupe p2 et le timer finit: on passe a l'attente de l'étape suivante
                        ChoixGroupeActivity act3 = ((ChoixGroupeActivity) TimerEtudiant.getInstance().getActivity());
                        new DAOCheckEtape(act3).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act3.getIdGroupe() + "", "4");
                        break;
                    case 4:
                        //On se trouve dans le classement Groupe p3 et le timer finit: on passe a l'attente de l'étape suivante
                        ChoixGroupeActivity act4 = ((ChoixGroupeActivity) TimerEtudiant.getInstance().getActivity());
                        new DAOCheckEtape(act4).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act4.getIdGroupe() + "", "5");
                        break;
                    case 5:
                        //On se trouve dans le classement Groupe p4 et le timer finit: on va dans l'attente du classement de classe
                        ChoixGroupeActivity act5 = ((ChoixGroupeActivity) TimerEtudiant.getInstance().getActivity());
                        new DAOCheckEtape(act5).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act5.getIdGroupe() + "", "6");
                        break;
                    case 6:
                        //On se trouve dans le classement Classe et le timer finit: on va dans l'attente de l'enquete
                        ChoixClasseActivity act6 = ((ChoixClasseActivity) TimerEtudiant.getInstance().getActivity());
                        new DAOCheckEtape(act6).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, act6.getIdGroupe() + "", "8");
                        break;
                }
            }
        }.start());

        setmTimerRunning(true);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }
}
