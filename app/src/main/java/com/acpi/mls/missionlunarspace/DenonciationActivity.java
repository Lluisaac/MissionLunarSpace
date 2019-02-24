package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.DAO.activity.DAODenonciationActivity;

import java.util.ArrayList;

public class DenonciationActivity extends AppCompatActivity {

    private String typeGroupe;
    private String roleEtudiant;
    private String idEtudiant;
    private String idGroupe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denonciation);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.roleEtudiant = (String) getIntent().getSerializableExtra("roleEtudiant");
        this.typeGroupe = (String) getIntent().getSerializableExtra("typeGroupe");
        this.idGroupe = (String) getIntent().getSerializableExtra("idGroupe");


        new DAODenonciationActivity(DenonciationActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,this.idGroupe);
    }

    public void getNomEudiantsGroupe(ArrayList<String> listeNomEtudiant) {
    }

    private void passageFormSatisfaction(View view){
        Intent intent = new Intent(this, FormulaireSatisfactionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("idEtudiant", Integer.parseInt(this.idEtudiant));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
