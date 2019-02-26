package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.refresh.DAOScoreFinal;
import com.acpi.mls.missionlunarspace.DAO.refresh.check.DAOCheckEtape;

import java.util.ArrayList;
import java.util.Arrays;

import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;

public class ScoreFinalActivity extends AppCompatActivity {

    private String[] agest = {"2 réservoirs de 50 kg d’oxygène chacun", "25 litres d’eau", "1 carte céleste des constellations lunaires", "2 kg d’aliments concentrés", "1 émetteur-récepteur fonctionnant à énergie solaire (fréquence moyenne)", "50 mètres de corde en nylon", "1 trousse médicale et seringues hypodermiques", "1 parachute en soie", "1 canot de sauvetage auto-gonflable", "1 ensemble de signaux lumineux fonctionnant à énergie solaire", "2 pistolets de calibre 45", "1 caisse de lait en poudre", "1 appareil de chauffage fonctionnant à énergie solaire", "1 compas magnétique (boussole)", "1 boîte d’allumettes"};

    private String idEtudiant;
    private String idGroupe;
    private String idClasse;

    private TableLayout containerTable;

    private TableRow[] tabTableRow = new TableRow[16];
    private ArrayList<String> classe;
    private ArrayList<String> groupe;
    private ArrayList<String> perso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_final);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.idGroupe = (String) getIntent().getSerializableExtra("idGroupe");
        this.idClasse = (String) getIntent().getSerializableExtra("idClasse");
        new DAOScoreFinal(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idClasse, idGroupe, idEtudiant);
        initTableau();
    }

    @Override
    public void onBackPressed() {

    }

    public void mettreScores(ArrayList<String>[] tab) {
        ArrayList<String> agest = new ArrayList<>(Arrays.asList(this.agest));
        classe = tab[2];
        groupe = tab[1];
        perso = tab[0];
    }

    private String trouverTitre(int score) {
        if (score < 26) {
            return "Héros Galactique";
        } else if (score < 33) {
            return "Commandant Interstellaire";
        } else if (score < 46) {
            return "Eclaireur Spatial";
        } else if (score < 56) {
            return "Pilote Interplanétaire";
        } else if (score < 70) {
            return "As des Astres";
        } else {
            return "Officier Stellaire";
        }
    }

    private void afficherVide() {
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < 16; j++) {
                TextView text6 = createTextView(false, j == 15);
                text6.setText("");
                this.tabTableRow[j].addView(text6, i);
                text6.setGravity(Gravity.LEFT);
            }
        }
    }

    public void afficherPerso() {
        String[] pos = new String[15];

        for (int i = 0; i < 15; i++) {
            pos[i] = (perso.indexOf(ChoixPersoActivity.listObjets[i]) + 1) + "";
        }

        afficherInfoTab(pos, 1);
        afficherScore(getScore(perso), 1);
    }

    public void afficherGroupe() {
        String[] pos = new String[15];

        for (int i = 0; i < 15; i++) {
            pos[i] = (groupe.indexOf(ChoixPersoActivity.listObjets[i]) + 1) + "";
        }

        afficherInfoTab(pos, 2);
        afficherScore(getScore(groupe), 2);
    }

    public void afficherClasse() {
        String[] pos = new String[15];

        for (int i = 0; i < 15; i++) {
            pos[i] = (classe.indexOf(ChoixPersoActivity.listObjets[i]) + 1) + "";
        }

        afficherInfoTab(pos, 3);
        afficherScore(getScore(classe), 3);
    }

    public void afficherAGEST() {
        String[] pos = new String[15];
        ArrayList<String> liste = new ArrayList<>(Arrays.asList(agest));

        for (int i = 0; i < 15; i++) {
            pos[i] = (liste.indexOf(ChoixPersoActivity.listObjets[i]) + 1) + "";
        }

        afficherInfoTab(pos, 4);
        afficherTitre(trouverTitre(getScore(perso)));

        new DAOCheckEtape(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idGroupe, "4");
    }

    private int getScore(ArrayList<String> tab) {
        int somme = 0;

        for (int i = 0; i < 15; i++) {
            int val = tab.indexOf(agest[i]) - i;
            somme += Math.abs(val);
        }

        return somme;
    }

    private void initTableau() {
        this.containerTable = (TableLayout) findViewById(R.id.containerTable);

        TableRow tableRow = new TableRow(this);
        containerTable.addView(tableRow, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        containerTable.setBackgroundColor(getResources().getColor(R.color.grey));

        tableRow.setLayoutParams(new LayoutParams(5));

        TextView text = createTextView(false, false);
        text.setText("Nom des Objets");
        text.setGravity(Gravity.CENTER);
        tableRow.addView(text, 0);

        TextView text2 = createTextView(false, false);
        text2.setText("Classement Personnel");
        text2.setGravity(Gravity.CENTER);
        tableRow.addView(text2, 1);

        TextView text3 = createTextView(false, false);
        text3.setText("Classement par Groupe");
        text3.setGravity(Gravity.CENTER);
        tableRow.addView(text3, 2);

        TextView text4 = createTextView(false, true);
        text4.setText("Classement par Classe");
        text4.setGravity(Gravity.CENTER);
        tableRow.addView(text4, 3);

        TextView text5 = createTextView(false, true);
        text5.setText("Classement AGEST");
        text5.setGravity(Gravity.CENTER);
        tableRow.addView(text5, 4);


        for(int i = 0 ; i < 16 ; i++){
            this.tabTableRow[i] = new TableRow(this);
            containerTable.addView(this.tabTableRow[i], new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }

        afficherNomObjet();
        afficherVide();
    }

    private TextView createTextView(boolean endline, boolean endcolumn) {
        TextView text = new TextView(this, null, R.style.frag3HeaderCol);
        int bottom = endline ? 1 : 0;
        int right = endcolumn ? 1 : 0;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.3f);
        params.setMargins(1, 1, right, bottom);
        text.setLayoutParams(params);
        text.setPadding(4, 4, 10, 4);
        text.setBackgroundColor(getResources().getColor(R.color.white));
        return text;
    }

    private void afficherNomObjet() {
        for (int j = 0; j < 15; j++) {
            TextView text6 = createTextView(false, j == 15);
            text6.setText(ChoixPersoActivity.listObjets[j]);
            this.tabTableRow[j].addView(text6, 0);
            text6.setGravity(Gravity.LEFT);
        }
        TextView text6 = createTextView(false, true);
        text6.setText("Score : ");
        this.tabTableRow[15].addView(text6, 0);
        text6.setGravity(Gravity.LEFT);
    }

    private void afficherInfoTab(String[] pos, int index ){
        for (int j = 0; j < 15; j++) {
            TextView text6 = (TextView) this.tabTableRow[j].getChildAt(index);
            text6.setText(pos[j]);
            text6.setGravity(Gravity.LEFT);
        }
    }

    private void afficherScore(int score, int index) {
        TextView text6 = (TextView) this.tabTableRow[15].getChildAt(index);
        text6.setText(score + "");
        text6.setGravity(Gravity.LEFT);
    }

    private void afficherTitre(String titre){
        TextView textView = findViewById(R.id.textView_titre);
        textView.setText("Vous êtes un " + titre);
        textView.setVisibility(View.VISIBLE);
    }


    public void passageFormSatisfaction(View view){
        Intent intent = new Intent(this, FormulaireSatisfactionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("idEtudiant", Integer.parseInt(this.idEtudiant));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

