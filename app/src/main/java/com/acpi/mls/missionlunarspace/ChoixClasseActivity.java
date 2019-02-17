package com.acpi.mls.missionlunarspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class ChoixClasseActivity extends AppCompatActivity {

    private String idEtudiant;
    private ArrayList<String> classementPerso;
    private String roleEtudiant;
    private String typeGroupe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_classe);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.classementPerso = (ArrayList<String>) getIntent().getSerializableExtra("classementPerso");
        this.roleEtudiant = (String) getIntent().getSerializableExtra("roleEtudiant");
        this.typeGroupe = (String) getIntent().getSerializableExtra("typeGroupe");

        initLayout();
    }


    private void initLayout() {
        if (this.roleEtudiant.equals("Capitaine") && this.typeGroupe.equals(1 + "")) {

        } else {

        }
