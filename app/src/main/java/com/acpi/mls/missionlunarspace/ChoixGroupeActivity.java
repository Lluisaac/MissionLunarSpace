package com.acpi.mls.missionlunarspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.DAOChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.immobile.MyAdapter;
import com.acpi.mls.missionlunarspace.immobile.MyObject;

import java.util.ArrayList;
import java.util.List;

public class ChoixGroupeActivity extends AppCompatActivity {

    private String typeGroupe;
    private String role;
    private String idEtudiant;

    private List<MyObject> classementPerso = new ArrayList<>();
    private RecyclerView recyclerViewImmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.role = "";
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
            //if (this.role.equals("Capitaine"))
            //setContentView(R.layout.page_capitaine);

            //if (this.role.equals("Technicien"))
            //setContentView(R.layout.page_technicien);

            if(this.role.equals("Astronaute")||this.role.equals("Expert")||this.role.equals("Saboteur")) {
                setRechercheClassementPerso();
                setContentView(R.layout.page_astronaute_expert_saboteur);
            }
        }
    }

    private int cpt;
    //Recuperation du classement personel dans la BDD
    private void setRechercheClassementPerso() {
        cpt = 1;
        new DAOChoixGroupeActivity(ChoixGroupeActivity.this).execute("getClassementEtudiant", "getClassementEtudiant", this.idEtudiant, cpt + "");
    }

    public void setClassement(String objet) {
        if (cpt < 15) {
            classementPerso.add(new MyObject(objet));
            cpt++;
            new DAOChoixGroupeActivity(ChoixGroupeActivity.this).execute("getClassementEtudiant", "getClassementEtudiant", this.idEtudiant, cpt + "");
        } else {
            creerListeImmobile();
        }
    }

    //Remplissage des listes des objets
    private void creerListeImmobile() {
        crerListeClassementPerso();

        ArrayList<MyObject> vrac = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            vrac.add(new MyObject(ChoixPersoActivity.listObjets[i]));
        }

        RecyclerView recyclerViewGroupe = (RecyclerView) findViewById(R.id.recyclerViewObjetGroupe);
        recyclerViewGroupe.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGroupe.setAdapter(new MyAdapter(vrac));

        if(this.role.equals("Astronaute"))
            creerListRandom();
        else
            creerListeOrdonnee();
    }

    private void crerListeClassementPerso() {
        recyclerViewImmobile = (RecyclerView) findViewById(R.id.recyclerView_objetPerso);
        recyclerViewImmobile.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewImmobile.setAdapter(new MyAdapter(classementPerso));
    }


    private void creerListeOrdonnee() {
        String[] agest = {"Réservoir d’oxygène","Réservoir d’eau","Carte céleste","Aliments concentrés","Émetteur-récepteur","Corde en nylon","Trousse médicale","Parachute en soie","Canot de sauvetage","Signaux lumineux","Pistolets de calibre 45","Lait en poudre","Appareil de chauffage","Compas Magnétique","Boîte d’allumettes"};

        ArrayList<MyObject> listOrdonnee = new ArrayList<>();

        if(this.role.equals("Expert"))
        {
            for (int i = 0; i < 15; i++) {
                listOrdonnee.add(new MyObject(agest[i]));
            }
        }else{
            for (int i = 14; i >= 0; i--) {
                listOrdonnee.add(new MyObject(agest[i]));
            }
        }

        RecyclerView recyclerViewVrac = (RecyclerView) findViewById(R.id.recyclerViewObjetVrac);
        recyclerViewVrac.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVrac.setAdapter(new MyAdapter(listOrdonnee));
    }

    private void creerListRandom() {
        String[] randomOrdre = {"Boîte d’allumettes", "Aliments concentrés", "Corde en nylon", "Parachute en soie", "Appareil de chauffage", "Pistolets de calibre 45", "Lait en poudre", "Réservoirs d’oxygène", "Carte céleste", "Canot de sauvetage", "Compas magnétique", "Réservoir d’eau", "Trousse médicale", "Signaux lumineux", "Émetteur-récepteur"};

        ArrayList<MyObject> vrac = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            vrac.add(new MyObject(randomOrdre[i]));
        }

        RecyclerView recyclerViewVrac = (RecyclerView) findViewById(R.id.recyclerViewObjetVrac);
        recyclerViewVrac.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVrac.setAdapter(new MyAdapter(vrac));

    }
}
