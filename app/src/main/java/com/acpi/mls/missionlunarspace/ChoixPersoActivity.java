package com.acpi.mls.missionlunarspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChoixPersoActivity extends AppCompatActivity {

    private String nom;
    private String classe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_perso);
        this.nom = (String) getIntent().getSerializableExtra("NomEtu");
        this.classe = (String) getIntent().getSerializableExtra("ClasseEtu");
    }

    private void creationList() {
    }
}
