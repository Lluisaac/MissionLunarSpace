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

import com.acpi.mls.missionlunarspace.DAO.activity.DAOChoixClasseActivity;
import com.acpi.mls.missionlunarspace.DAO.activity.DAOChoixPersoActivity;
import com.acpi.mls.missionlunarspace.DAO.refresh.DAORefreshListeClasse;
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
    private String idClasse;
    private ArrayList<String> classementClasse = new ArrayList<>();
    private RecyclerView classementClasseRecycler;
    private DAORefreshListeClasse daoRefreshListeClasse;

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
        new DAOChoixClasseActivity(ChoixClasseActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getIdClasse", "getIdClasse", this.idGroupe);

        Timer.getInstance().setTextView((TextView) findViewById(R.id.textTimer));
        Timer.getInstance().setActivity(this);
        Timer.getInstance().ajouterPhaseEtDemarrer();

    }

    @Override
    public void onBackPressed() {

    }

    private void initLayout() {
        initClassementPerso();
        if (this.roleEtudiant.equals("Capitaine") && this.typeGroupe.equals(1 + "")) {
            initClassementClasseCaptitaine();
        } else {
            initClassementClasse();
        }
        new DAOChoixClasseActivity(ChoixClasseActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getAllClassementGroupe", "getAllClassementGroupe", this.idGroupe);
    }

    private void initClassementPerso() {
        RecyclerView recyclerViewClassementPerso = (RecyclerView) findViewById(R.id.recyclerView_choixClasse_classementPerso);
        recyclerViewClassementPerso.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClassementPerso.setAdapter(new MyAdapter(this.classementPerso));
    }

    private void initClassementClasseCaptitaine() {
        RecyclerView recyclerViewCapitaine = findViewById(R.id.recyclerView_choixClasse_classementClasse);

        recyclerViewCapitaine.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(this.classementClasse, ChoixClasseActivity.this);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewCapitaine);

        recyclerViewCapitaine.setAdapter(mAdapter);
    }

    private void initClassementClasse() {
        this.classementClasseRecycler = (RecyclerView) findViewById(R.id.recyclerView_choixClasse_classementClasse);
        classementClasseRecycler.setLayoutManager(new LinearLayoutManager(this));
        classementClasseRecycler.setAdapter(new MyAdapter(this.classementClasse));
    }

    public void initClasssementGroupe(ArrayList<String> list) {
        RecyclerView recyclerViewClassementPerso = (RecyclerView) findViewById(R.id.recyclerView_choixClasse_classementGroupe);
        recyclerViewClassementPerso.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClassementPerso.setAdapter(new MyAdapter(list));
    }

    public void saveClassement() {
        ArrayList<String> monArrayList = new ArrayList<String>(Arrays.asList(ChoixPersoActivity.listObjets));
        new DAOChoixClasseActivity(ChoixClasseActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "deleteClassementClasse", "", this.idClasse);
        for (int i = 1; i <= 15; i++) {
            int idObjet = 1 + monArrayList.indexOf(classementClasse.get(i - 1));
            new DAOChoixClasseActivity(ChoixClasseActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "saveClassementClasse", "", this.idClasse, idObjet + "", "" + i);
        }
    }

    public void setIdClasse(String s) {
        this.idClasse = s;
        if(!this.roleEtudiant.equals("Capitaine") && this.typeGroupe.equals(1 + ""))
        {
            this.daoRefreshListeClasse = new DAORefreshListeClasse(ChoixClasseActivity.this);
            this.daoRefreshListeClasse.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.idClasse);
        }
    }

    public void refreshClassementClasse(ArrayList<String> liste) {
        if (!liste.equals(this.classementClasse)) {
            this.classementClasse = liste;
            MyAdapter adapter = (MyAdapter) this.classementClasseRecycler.getAdapter();
            adapter.setList(this.classementClasse);
            adapter.notifyDataSetChanged();
        }
    }

    public void passageScoreFinal(View view) {
        Intent intent = new Intent(this, ScoreFinalActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idEtudiant",this.idEtudiant);
        bundle.putString("idGroupe", this.idGroupe);
        bundle.putString("idClasse", this.idClasse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void passageDenonciation(View view){
        Intent intent = new Intent(this, DenonciationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idEtudiant",this.idEtudiant);
        bundle.putString("idGroupe", this.idGroupe);
        bundle.putString("idClasse", this.idClasse);
        bundle.putString("roleEtudiant", this.roleEtudiant);
        bundle.putString("typeGroupe", this.typeGroupe);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
