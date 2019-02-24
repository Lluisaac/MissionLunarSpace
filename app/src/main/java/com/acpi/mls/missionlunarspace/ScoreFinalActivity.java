package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acpi.mls.missionlunarspace.DAO.activity.DAOEtudiant;
import com.acpi.mls.missionlunarspace.DAO.activity.DAOScoreFinal;
import com.acpi.mls.missionlunarspace.DAO.autre.DAORecupTemps;
import com.acpi.mls.missionlunarspace.DAO.refresh.DAOCheckPartieDemaree;

import java.util.ArrayList;
import java.util.Arrays;

public class ScoreFinalActivity extends AppCompatActivity {

    private String[] agest = {"2 réservoirs de 50 kg d’oxygène chacun", "25 litres d’eau", "1 carte céleste des constellations lunaires", "2 kg d’aliments concentrés", "1 émetteur-récepteur fonctionnant à énergie solaire (fréquence moyenne)", "50 mètres de corde en nylon", "1 trousse médicale et seringues hypodermiques", "1 parachute en soie", "1 canot de sauvetage auto-gonflable", "1 ensemble de signaux lumineux fonctionnant à énergie solaire", "2 pistolets de calibre 45", "1 caisse de lait en poudre", "1 appareil de chauffage fonctionnant à énergie solaire", "1 compas magnétique (boussole)", "1 boîte d’allumettes"};

    private String idEtudiant;
    private String idGroupe;
    private String idClasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_score_final);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.idGroupe = (String) getIntent().getSerializableExtra("idGroupe");
        this.idClasse = (String) getIntent().getSerializableExtra("idClasse");
        new DAOScoreFinal(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idClasse, idGroupe, idEtudiant);
    }

    @Override
    public void onBackPressed() {

    }

    public void mettreScores (ArrayList<String>[] tab) {
        ArrayList<String> agest = new ArrayList<>(Arrays.asList(this.agest));
        ArrayList<String> classe = tab[2];
        ArrayList<String> groupe = tab[1];
        ArrayList<String> perso = tab[0];
        //TODO faire que les 4 listes du dessus s'affichent sur la page
        //TODO faire que les scores s'affichent correctement sur la page
    }

    private int getScore(ArrayList<String> tab) {
        int somme = 0;

        for (int i = 0; i < 15; i++) {
            int val = tab.indexOf(agest[i]) - i;
            somme += Math.abs(val);
        }

        return somme;
    }
}
