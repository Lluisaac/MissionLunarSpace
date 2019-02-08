package com.acpi.mls.missionlunarspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChoixClasseActivity extends AppCompatActivity {

    private String idEtudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_classe);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");

    }


}
