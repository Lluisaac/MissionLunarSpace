package com.acpi.mls.missionlunarspace;


import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;


public class Timer {


    private TextView mTextViewCountDown;
    private Button mButtonStart;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis;


    public TextView getmTextViewCountDown() {
        return mTextViewCountDown;
    }

    public void setmTextViewCountDown(TextView mTextViewCountDown) {
        this.mTextViewCountDown = mTextViewCountDown;
    }

    public Button getmButtonStart() {
        return mButtonStart;
    }

    public void setmButtonStart(Button mButtonStart) {
        this.mButtonStart = mButtonStart;
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

    public Timer(TextView tv, Button btn, long timeLeft){
        this.setmTextViewCountDown(tv);
        this.setmButtonStart(btn);
        this.setmTimeLeftInMillis(timeLeft);

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
                getmButtonStart().setText("Start");
                getmButtonStart().setVisibility(View.INVISIBLE);
            }
        }.start());

        setmTimerRunning(true);
    }

    public void pauseTimer() {
        getmCountDownTimer().cancel();
        setmTimerRunning(false);
        getmButtonStart().setText("Start");
    }



    public void updateCountDownText() {
        int minutes = (int) (getmTimeLeftInMillis() / 1000) / 60;
        int seconds = (int) (getmTimeLeftInMillis() / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        getmTextViewCountDown().setText(timeLeftFormatted);
    }

}
