package com.acpi.mls.missionlunarspace;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        faireBoutonProf();
        faireBoutonLoginProf();
    }


    public void faireBoutonProf() {
        Button button = (Button) findViewById(R.id.buttonProf);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switch(view.getId())
                {
                    case R.id.buttonProf:
                        setContentView(R.layout.activity_login);

                        break;
                }
            }
        });
    }

    public void faireBoutonLoginProf() {
        Button button = (Button) findViewById(R.id.buttonProf);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switch(view.getId())
                {
                    case R.id.buttonConfirmationLoginProf:
                        
                        if () {

                        }
                }
            }
        });
    }
}
