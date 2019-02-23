package com.acpi.mls.missionlunarspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

public class FormulaireSatisfactionActivity extends AppCompatActivity {

    private String idEtudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        setContentView(R.layout.activity_formulaire_satisfaction);

        Spinner spinner = findViewById(R.id.spinner);
        //spinner.setAdapter();

    }

    @Override
    public void onBackPressed() {
    }


}
