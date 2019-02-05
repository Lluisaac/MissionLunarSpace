package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
/*
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        faireBoutonProf();
    }


    public void faireBoutonProf() {
        Button button = (Button) findViewById(R.id.buttonProf);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switch(view.getId())
                {
                    case R.id.buttonProf:
                        setContentView(R.layout.activity_login);
                        faireBoutonLoginProf();

                        break;
                }
            }
        });
    }

    public void faireBoutonLoginProf() {
        Button button = (Button) findViewById(R.id.buttonConfirmationLoginProf);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switch(view.getId())
                {
                    case R.id.buttonConfirmationLoginProf:
                        EditText pass = (EditText) findViewById(R.id.mdpProf);
                        System.out.println(pass.getText().toString());
                        break;
                }
            }
        });
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void goActivityEtu(View view) {
        Intent intent = new Intent(this,EtudiantActivity.class);
        startActivity(intent);
    }

    public void goActivityProf(View view) {
        Intent intent = new Intent(this, EnseignantActivity.class);
        startActivity(intent);
    }
}
