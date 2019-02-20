package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.acpi.mls.missionlunarspace.EnseignantActivity;
import com.acpi.mls.missionlunarspace.EtudiantActivity;
import com.acpi.mls.missionlunarspace.R;

public class SelectionEtudiantEnseignant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_etudiant_enseignant);
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
