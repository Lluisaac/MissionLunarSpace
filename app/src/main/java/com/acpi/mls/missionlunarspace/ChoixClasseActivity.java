package com.acpi.mls.missionlunarspace;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.acpi.mls.missionlunarspace.DAO.activity.DAOChoixClasseActivity;
import com.acpi.mls.missionlunarspace.immobile.MyAdapter;
import com.acpi.mls.missionlunarspace.listObjetMobile.ItemMoveCallback;
import com.acpi.mls.missionlunarspace.listObjetMobile.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ChoixClasseActivity extends AppCompatActivity {

    private String idEtudiant;
    private ArrayList<String> classementPerso;
    private String roleEtudiant;
    private String typeGroupe;
    private String idGroupe;
    private ArrayList<String> classementClasse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_classe);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.classementPerso = getIntent().getStringArrayListExtra("classementPerso");
        this.roleEtudiant = (String) getIntent().getSerializableExtra("roleEtudiant");
        this.typeGroupe = (String) getIntent().getSerializableExtra("typeGroupe");
        this.idGroupe = (String) getIntent().getSerializableExtra("idGroupe");

        this.classementClasse.addAll((Arrays.asList(ChoixPersoActivity.listObjets).subList(0, 15)));

        initLayout();
    }


    private void initLayout() {
        initClassementPerso();
        if (this.roleEtudiant.equals("Capitaine") && this.typeGroupe.equals(1 + "")) {
            initClassementClasseCaptitaine();
        } else {
            initClassementClasse();
        }
        new DAOChoixClasseActivity(ChoixClasseActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"getAllClassementGroupe","getAllClassementGroupe",this.idGroupe);
    }

    private void initClassementPerso(){
        RecyclerView recyclerViewClassementPerso = (RecyclerView) findViewById(R.id.recyclerView_choixClasse_classementPerso);
        recyclerViewClassementPerso.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClassementPerso.setAdapter(new MyAdapter(this.classementPerso));
    }

    private void initClassementClasseCaptitaine(){
        RecyclerView recyclerViewCapitaine = findViewById(R.id.recyclerView_choixClasse_classementClasse);

        recyclerViewCapitaine.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(this.classementClasse);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewCapitaine);

        recyclerViewCapitaine.setAdapter(mAdapter);
    }

    private void initClassementClasse(){
        RecyclerView recyclerViewClassementPerso = (RecyclerView) findViewById(R.id.recyclerView_choixClasse_classementClasse);
        recyclerViewClassementPerso.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClassementPerso.setAdapter(new MyAdapter(this.classementClasse));
    }

    public void initClasssementGroupe(ArrayList<String> list) {
        RecyclerView recyclerViewClassementPerso = (RecyclerView) findViewById(R.id.recyclerView_choixClasse_classementGroupe);
        recyclerViewClassementPerso.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClassementPerso.setAdapter(new MyAdapter(list));
    }
}
