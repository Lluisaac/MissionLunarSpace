package com.acpi.mls.missionlunarspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.R;
import com.acpi.mls.missionlunarspace.choixObjets.MyAdapter;
import com.acpi.mls.missionlunarspace.choixObjets.MyObject;

import java.util.ArrayList;
import java.util.List;

public class ChoixPersoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MyObject> object = new ArrayList<>();

    private String nom;
    private String classe;
    public static final String[] listObjets = {"1 boîte d’allumettes", "2 kg d’aliments concentrés", "50 mètres de corde en nylon", "1 parachute en soie", "1 appareil de chauffage fonctionnant à énergie solaire", "2 pistolets de calibre 45", "1 caisse de lait en poudre", "2 réservoirs de 50 kg d’oxygène chacun", "1 carte céleste des constellations lunaires", "1 canot de sauvetage auto-gonflable", "1 compas magnétique (boussole)", "25 litres d’eau", "1 trousse médicale et seringues hypodermiques", "1 ensemble de signaux lumineux fonctionnant à énergie solaire", "1 émetteur-récepteur fonctionnant à énergie solaire (fréquence moyenne)"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_perso);
        this.nom = (String) getIntent().getSerializableExtra("NomEtu");
        this.classe = (String) getIntent().getSerializableExtra("ClasseEtu");


        ajouterObjets();
        recyclerView = (RecyclerView) findViewById(R.id.recycled_view_objetBase);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(object));

    }

    private void ajouterObjets(){
        for (int i = 0 ; i < listObjets.length ; ++i) {
            object.add(new MyObject(listObjets[i]));
        }
    }
}
