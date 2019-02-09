package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.DAOChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.immobile.MyAdapter;
import com.acpi.mls.missionlunarspace.listObjetMobile.ItemMoveCallback;
import com.acpi.mls.missionlunarspace.listObjetMobile.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ChoixGroupeActivity extends AppCompatActivity {

    private String typeGroupe;
    private String role;
    private String idEtudiant;

    private ArrayList classementPerso = new ArrayList<>();
    private RecyclerView recyclerViewClassmentPerso;


    private ArrayList<String> classementGroupe = new ArrayList<>();
    private RecyclerView getRecyclerViewGroupe;
    private RecyclerView recyclerViewCapitaine;

    private final String[] randomOrdre = {"Boîte d’allumettes", "Aliments concentrés", "Corde en nylon", "Parachute en soie", "Appareil de chauffage", "Pistolets de calibre 45", "Lait en poudre", "Réservoirs d’oxygène", "Carte céleste", "Canot de sauvetage", "Compas magnétique", "Réservoir d’eau", "Trousse médicale", "Signaux lumineux", "Émetteur-récepteur"};
    private final String[] agest = {"Réservoir d’oxygène", "Réservoir d’eau", "Carte céleste", "Aliments concentrés", "Émetteur-récepteur", "Corde en nylon", "Trousse médicale", "Parachute en soie", "Canot de sauvetage", "Signaux lumineux", "Pistolets de calibre 45", "Lait en poudre", "Appareil de chauffage", "Compas Magnétique", "Boîte d’allumettes"};


    private int phase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.classementPerso = (ArrayList) getIntent().getSerializableExtra("classementPersoEtudiant");
        this.role = "";

        this.classementGroupe.addAll(Arrays.asList(ChoixPersoActivity.listObjets).subList(0, 15));
        recuperationGroupe();
    }

    private void recuperationGroupe() {
        setContentView(R.layout.activity_choix_groupe);
        new DAOChoixGroupeActivity(ChoixGroupeActivity.this).execute("getGroupeEtudiant", "getGroupeEtudiant", this.idEtudiant);
    }

    public void setGroup(String typeGroupe) {
        this.typeGroupe = typeGroupe;
        TextView textView = findViewById(R.id.textView_AffichageGroupe);
        textView.setText("VOUS ÊTES DANS LE GROUPE " + this.typeGroupe);
        new DAOChoixGroupeActivity(this).execute("getRoleEtudiant", "getRoleEtudiant", this.idEtudiant);
    }

    public void setRole(String nomRole) {
        this.role = nomRole;
    }


    //Click sur le bouton continuer
    public void continuerRole(View view) {
        if (!this.role.equals("")) {
            if (this.role.equals("Capitaine")) {
                setContentView(R.layout.page_capitaine);
            }
            if (this.role.equals("Technicien"))
                setContentView(R.layout.page_technicien);

            if (this.role.equals("Astronaute") || this.role.equals("Expert") || this.role.equals("Saboteur")) {
                setContentView(R.layout.page_astronaute_expert_saboteur);
            }

            //setRechercheClassementPerso();
            creerListeImmobile();
        }
    }

    /*
        private int cpt;

        //Recuperation du classement personel dans la BDD
        private void setRechercheClassementPerso() {
            cpt = 1;
            new DAOChoixGroupeActivity(ChoixGroupeActivity.this).execute("getClassementEtudiant", "getClassementEtudiant", this.idEtudiant, cpt + "");
        }

        public void setClassement(String objet) {
            if (cpt < 15) {
                classementPerso.add(objet);
                cpt++;
                new DAOChoixGroupeActivity(ChoixGroupeActivity.this).execute("getClassementEtudiant", "getClassementEtudiant", this.idEtudiant, cpt + "");
            } else {

            }
        }

    */
    //Remplissage des listes des objets
    private void creerListeImmobile() {
        crerListeClassementPerso();

        if (this.role.equals("Capitaine"))
            creerListCapitaine();
        else
            creerListeChoixGroupe();


        ArrayList<String> strings = new ArrayList<>();

        switch (this.role) {
            case "Expert":
                strings.addAll((Arrays.asList(agest).subList(0, 15)));
                break;
            case "Saboteur":
                for (int i = 14; i >= 0; i--) {
                    strings.add(agest[i]);
                }
                break;
            default:
                strings.addAll((Arrays.asList(randomOrdre).subList(0, 15)));
                break;
        }

        creerListCentre(strings);
    }

    private void crerListeClassementPerso() {
        recyclerViewClassmentPerso = (RecyclerView) findViewById(R.id.recyclerView_objetPerso);
        recyclerViewClassmentPerso.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClassmentPerso.setAdapter(new MyAdapter(classementPerso));
    }

    private void creerListeChoixGroupe() {
        getRecyclerViewGroupe = (RecyclerView) findViewById(R.id.recyclerViewObjetGroupe);
        getRecyclerViewGroupe.setLayoutManager(new LinearLayoutManager(this));
        getRecyclerViewGroupe.setAdapter(new MyAdapter(classementGroupe));
    }

    private void creerListCentre(ArrayList<String> list) {

        RecyclerView recyclerViewVrac = (RecyclerView) findViewById(R.id.recyclerViewObjetVrac);
        recyclerViewVrac.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVrac.setAdapter(new MyAdapter(list));

    }

    private void creerListCapitaine() {
        recyclerViewCapitaine = findViewById(R.id.recyclerView_Capitaine_ChoixGroupeCapitaine);

        recyclerViewCapitaine.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(classementGroupe);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewCapitaine);

        recyclerViewCapitaine.setAdapter(mAdapter);
    }

    public void passageChoixClasse() {
        Intent intent = new Intent(this, ChoixClasseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idEtudiant", this.idEtudiant);
        bundle.putString("typeGroupe", this.typeGroupe);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void changementDePhase(View view) {
        this.phase ++;
    }
}
