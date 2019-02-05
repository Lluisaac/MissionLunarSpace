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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goActivityProf(View view) {
        Intent intent = new Intent(this, EnseignantActivity.class);
        startActivity(intent);
    }

    public void goActivityEtu(View view) {
        Intent intent = new Intent(this, EtudiantActivity.class);
        startActivity(intent);
    }
}
