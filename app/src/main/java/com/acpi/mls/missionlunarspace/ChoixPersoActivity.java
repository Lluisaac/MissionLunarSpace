package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.activity.DAOChoixPersoActivity;
import com.acpi.mls.missionlunarspace.immobile.MyAdapter;
import com.acpi.mls.missionlunarspace.listObjetMobile.ItemMoveCallback;
import com.acpi.mls.missionlunarspace.listObjetMobile.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ChoixPersoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewImmobile;
    private RecyclerView recyclerViewMobile;

    private ArrayList<String> objectImmobile = new ArrayList<>();
    private ArrayList<String> objectMobile = new ArrayList<>();

    private String nom;
    private String classe;
    private String annee;
    private String idEtudiant;

    public static final String[] listObjets = {"1 boîte d’allumettes", "2 kg d’aliments concentrés", "50 mètres de corde en nylon", "1 parachute en soie", "1 appareil de chauffage fonctionnant à énergie solaire", "2 pistolets de calibre 45", "1 caisse de lait en poudre", "2 réservoirs de 50 kg d’oxygène chacun", "1 carte céleste des constellations lunaires", "1 canot de sauvetage auto-gonflable", "1 compas magnétique (boussole)", "25 litres d’eau", "1 trousse médicale et seringues hypodermiques", "1 ensemble de signaux lumineux fonctionnant à énergie solaire", "1 émetteur-récepteur fonctionnant à énergie solaire (fréquence moyenne)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_perso);
        this.nom = (String) getIntent().getSerializableExtra("NomEtu");
        this.classe = (String) getIntent().getSerializableExtra("ClasseEtu");
        this.annee = (String) getIntent().getSerializableExtra("AnneeEtu");
        new DAOChoixPersoActivity(ChoixPersoActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"getIdEtudiant", "getIdEtudiant", this.nom, this.classe, this.annee);
        Timer.getInstance().setTextView((TextView) findViewById(R.id.textTimer));
        Timer.getInstance().ajouterPhaseEtDemarrer();
        Timer.getInstance().setActivity(this);
    }

    public void setIdEtudiant(String s) {
        this.idEtudiant = s;
        ajouterObjets();
        creerListeImmobile();
        creerListeMobile();
    }

    private void creerListeMobile() {

        recyclerViewMobile = findViewById(R.id.recycled_view_mobile);

        recyclerViewMobile.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(objectMobile);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewMobile);

        recyclerViewMobile.setAdapter(mAdapter);
    }

    private void creerListeImmobile() {
        recyclerViewImmobile = findViewById(R.id.recycled_view_objetBase);
        recyclerViewImmobile.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewImmobile.setAdapter(new MyAdapter(objectImmobile));
    }

    private void ajouterObjets() {
        for (int i = 0; i < listObjets.length; ++i) {
            objectImmobile.add(listObjets[i]);
            objectMobile.add(listObjets[i]);
        }
    }

    private void saveClassement() {
        ArrayList<String> monArrayList = new ArrayList<String>(Arrays.asList(listObjets));

        for (int i = 1; i <= 15; i++) {
            int idObjet = 1 + monArrayList.indexOf(objectMobile.get(i - 1));
            new DAOChoixPersoActivity(ChoixPersoActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"saveClassementObjet", "", this.idEtudiant, idObjet + "", "" + i);
        }
    }


    public void continuerChoixGroupe(View view) {
        //TODO enlever le commentaire pour enregistrer les classement dans la BD
        //saveClassement();
        passageGroupe();
    }

    private void passageGroupe() {
        Intent intent = new Intent(this, ChoixGroupeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idEtudiant", this.idEtudiant);
        bundle.putStringArrayList("classementPersoEtudiant",this.objectMobile);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
