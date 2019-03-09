package com.acpi.mls.missionlunarspace;


import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class TimerProf extends Timer{

    private EnseignantActivity activity;

    public TimerProf(String tempsNonFormate) {
        super(tempsNonFormate);
    }

    public static TimerProf createTimer(String tempsNonFormate) {
        TimerProf.timer = new TimerProf(tempsNonFormate);
        return (TimerProf) timer;
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
                switch (TimerProf.getInstance().getPhase()) {
                    case 1:
                        //On se trouve dans le classement Perso et le timer finit: on propose d'arreter le classement perso
                        activity.arreterClassementPerso();
                        break;
                    case 2:
                        //On se trouve dans le classement Groupe p1 et le timer finit: on propose d'arreter le classement de groupe 1
                        activity.lancerClassementGroupe(2);
                        break;
                    case 3:
                        //On se trouve dans le classement Groupe p2 et le timer finit: on propose d'arreter le classement de groupe 2
                        activity.lancerClassementGroupe(3);
                        break;
                    case 4:
                        //On se trouve dans le classement Groupe p3 et le timer finit: on propose d'arreter le classement de groupe 3
                        activity.lancerClassementGroupe(4);
                        break;
                    case 5:
                        //On se trouve dans le classement Groupe p4 et le timer finit: on propose d'arreter le classement de groupe 4
                        activity.arreterClassementGroupe();
                        break;
                    case 6:
                        //On se trouve dans le classement Classe et le timer finit: on propose d'arreter le classement de classe
                        activity.arreterClassementClasse();
                        break;
                }

            }
        }.start());

        setmTimerRunning(true);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = (EnseignantActivity) activity;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }
}
