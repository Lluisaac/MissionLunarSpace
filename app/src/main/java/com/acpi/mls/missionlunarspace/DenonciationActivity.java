package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.DAO.activity.DAODenonciationActivity;
import com.acpi.mls.missionlunarspace.immobile.MyAdapter;

import java.util.ArrayList;

public class DenonciationActivity extends AppCompatActivity {

    private String typeGroupe;
    private String roleEtudiant;
    private String idEtudiant;
    private String idGroupe;
    private String idClasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denonciation);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.roleEtudiant = (String) getIntent().getSerializableExtra("roleEtudiant");
        this.typeGroupe = (String) getIntent().getSerializableExtra("typeGroupe");
        this.idGroupe = (String) getIntent().getSerializableExtra("idGroupe");
        this.idClasse = (String) getIntent().getSerializableExtra("idClasse");

        if (this.roleEtudiant.equals("Capitaine"))
            setContentView(R.layout.enquete_en_cours);
        else
            new DAODenonciationActivity(DenonciationActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.idGroupe);
    }

    public void getNomEudiantsGroupe(ArrayList<String> listeNomEtudiant) {
        Spinner expert = findViewById(R.id.spinner_exper);
        Spinner saboteur = findViewById(R.id.spinner_saboteur);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listeNomEtudiant);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saboteur.setAdapter(adapter);


        if (this.typeGroupe.equals("2")) {
            expert.setVisibility(View.INVISIBLE);
            TextView textView = findViewById(R.id.textVieweExpert);
            textView.setVisibility(View.INVISIBLE);
        }else{
            expert.setAdapter(adapter);
        }


    }


    public void passageScoreFinal(View view) {
        Intent intent = new Intent(this, ScoreFinalActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idEtudiant", this.idEtudiant);
        bundle.putString("idGroupe", this.idGroupe);
        bundle.putString("idClasse", this.idClasse);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
