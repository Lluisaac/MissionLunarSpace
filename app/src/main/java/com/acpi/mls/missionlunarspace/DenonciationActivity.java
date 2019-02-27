package com.acpi.mls.missionlunarspace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.DAO.activity.DAODenonciationActivity;
import com.acpi.mls.missionlunarspace.DAO.refresh.DAORefreshEnquete;
import com.acpi.mls.missionlunarspace.immobile.MyAdapter;

import java.util.ArrayList;

public class DenonciationActivity extends AppCompatActivity {

    private String typeGroupe;
    private String roleEtudiant;
    private String idEtudiant;
    private String idGroupe;
    private String idClasse;

    private DAORefreshEnquete daoRefreshEnquete;

    private String saboteur = "aucun";
    private String expert = "aucun";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denonciation);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.roleEtudiant = (String) getIntent().getSerializableExtra("roleEtudiant");
        this.typeGroupe = (String) getIntent().getSerializableExtra("typeGroupe");
        this.idGroupe = (String) getIntent().getSerializableExtra("idGroupe");
        this.idClasse = (String) getIntent().getSerializableExtra("idClasse");

        if (!this.roleEtudiant.equals("Capitaine")) {
            setContentView(R.layout.enquete_en_cours);
            Button suite = findViewById(R.id.button_enquete_termine);
            suite.setVisibility(View.INVISIBLE);
            this.daoRefreshEnquete = new DAORefreshEnquete(DenonciationActivity.this, this.idGroupe);
            this.daoRefreshEnquete.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            new DAODenonciationActivity(DenonciationActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getNomEtudiantsGroupe", "getNomEtudiantsGroupe", this.idGroupe);
        }
    }

    public void getNomEudiantsGroupe(ArrayList<String> listeNomEtudiant) {
        listeNomEtudiant.add("aucun");
        Spinner expert = findViewById(R.id.spinner_exper);
        Spinner saboteur = findViewById(R.id.spinner_saboteur);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listeNomEtudiant);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saboteur.setAdapter(adapter);
        expert.setAdapter(adapter);

        if (this.typeGroupe.equals("1") || this.typeGroupe.equals("2")) {
            new DAODenonciationActivity(DenonciationActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getSaboteur", "getSaboteur", this.idGroupe);
        }
        if (this.typeGroupe.equals("1") || this.typeGroupe.equals("4")) {
            new DAODenonciationActivity(DenonciationActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getExpert", "getExpert", this.idGroupe);
        }
    }


    public void passageScoreFinal() {
        Intent intent = new Intent(this, ScoreFinalActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idEtudiant", this.idEtudiant);
        bundle.putString("idGroupe", this.idGroupe);
        bundle.putString("idClasse", this.idClasse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void getSaboteur(String s) {
        this.saboteur = s;
    }

    public void getExpert(String s) {
        this.expert = s;
    }

    public void verifEnquete(View view) {
        Spinner expert = findViewById(R.id.spinner_exper);
        Spinner saboteur = findViewById(R.id.spinner_saboteur);
        String tvExpert =  expert.getSelectedItem().toString();
        String tvSaboteur = saboteur.getSelectedItem().toString();

        boolean findExpert = this.expert.equals(tvExpert);
        boolean findSaboteur = this.saboteur.equals(tvSaboteur);

        String s = "";
        String e = "";

        String messS;
        String messE;

        if (findExpert) {
            e += 1;
            messE = "Vous avez découvert l'expert !";
        } else {
            e += 0;
            messE = "Vous avez fait un mauvais choix concernant l'expert !";
        }

        if (findSaboteur) {
            s += 1;
            messS = "Vous avez découvert le saboteur !";
        } else {
            s += 0;
            messS = "Vous avez fait un mauvais choix concernant le saboteur !";
        }

        new DAODenonciationActivity(DenonciationActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "saveEnquete", "", this.idGroupe, s, e);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Resultat de l'enquête");
        dialogBuilder.setMessage(messE + "\n" + messS);
        dialogBuilder.setCancelable(false).setPositiveButton("CONTINUER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                passageScoreFinal();
            }
        });
        dialogBuilder.create().show();
    }

    public void afficherButtonFin(){
        Button suite = findViewById(R.id.button_enquete_termine);
        suite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passageScoreFinal();
            }
        });
        suite.setVisibility(View.VISIBLE);
    }
}
