package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acpi.mls.missionlunarspace.DAO.autre.DAORecupTemps;
import com.acpi.mls.missionlunarspace.DAO.refresh.check.DAOCheckPartieDemarre;
import com.acpi.mls.missionlunarspace.DAO.activity.DAOEtudiant;

public class EtudiantActivity extends AppCompatActivity {

    private String nomEtu;
    private String classeEtu;
    private String anneeEtu;
    private String idEtudiant;
    private String idClasse;

    private boolean isAttente = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant);
    }

    @Override
    public void onBackPressed() {
        if (!isAttente) {
            super.onBackPressed();
        }
    }

    public void onClikButtonContinuer(View view) {
        EditText etuNom = findViewById(R.id.textViewNomEtu);
        String nom = etuNom.getText().toString();

        EditText etuClasse = findViewById(R.id.textViewClasseEtu);
        String classe = etuClasse.getText().toString();

        EditText etuAnnee = findViewById(R.id.textViewAnneeEtu);
        String annee = etuAnnee.getText().toString();

        if (verifText(nom, classe, annee)) {
            this.nomEtu = nom;
            this.classeEtu = classe;
            this.anneeEtu = annee;
            new DAOEtudiant(EtudiantActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"getIdClasse", "existClasse", this.classeEtu, annee);
        } else {
            Toast.makeText(this, "Vous devez renseigner tous les champs !", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCLickTempo(View view) {
        Intent intent = new Intent(this, ChoixPersoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("NomEtu", nomEtu);
        bundle.putString("ClasseEtu", classeEtu);
        bundle.putString("AnneeEtu", anneeEtu);
        bundle.putString("IdEtu", idEtudiant);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private boolean verifText(String nom, String classe, String annee) {
        return !classe.equals("") && !nom.equals("") && !annee.equals("");
    }

    public void existeClasse(String id) {
        if (!id.equals("")) {
            new DAOEtudiant(EtudiantActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"createEtudiant", "setIdEtu", this.nomEtu);
            this.idClasse = id;
        } else
            Toast.makeText(this, "La classe n'existe pas.", Toast.LENGTH_SHORT).show();
    }

    public void faireNouvelEtudiant(String idEtudiant) {
        if ("-1".equals(idEtudiant)) {
            Toast.makeText(this, "Veuillez renter un nom different", Toast.LENGTH_SHORT).show();
        } else {
            this.setIdEtudiant(idEtudiant);
            changerPageVersAttente();
        }
    }

    private void changerPageVersAttente() {
        isAttente = true;
        setContentView(R.layout.content_etudiant_attente);
        Button boutonSuiveAttente = findViewById(R.id.buttonAttente);
        boutonSuiveAttente.setVisibility(View.INVISIBLE);
        new DAOCheckPartieDemarre(boutonSuiveAttente, Integer.parseInt(idClasse), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void pouvoirChangerPage() {
        new DAORecupTemps(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "classe", idClasse + "");
    }

    public void faireTimer(String heure) {
        TimerEtudiant.createTimer(heure);
        onCLickTempo(null);
    }

    public void setIdEtudiant(String idEtudiant) {
        this.idEtudiant = idEtudiant;
    }
}
