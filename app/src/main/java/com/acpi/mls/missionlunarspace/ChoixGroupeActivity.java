package com.acpi.mls.missionlunarspace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.acpi.mls.missionlunarspace.DAO.activity.DAOChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.DAO.autre.DAOClassementGroupe;
import com.acpi.mls.missionlunarspace.DAO.autre.DAOClassementTemp;
import com.acpi.mls.missionlunarspace.DAO.autre.DAOPopupTechnicien;
import com.acpi.mls.missionlunarspace.DAO.refresh.DAORefreshListeGroupe;
import com.acpi.mls.missionlunarspace.DAO.refresh.DAORefreshUpdateClassementTemporaire;
import com.acpi.mls.missionlunarspace.immobile.MyAdapter;
import com.acpi.mls.missionlunarspace.immobile.MyLinearLayoutManager;
import com.acpi.mls.missionlunarspace.listObjetMobile.ItemMoveCallback;
import com.acpi.mls.missionlunarspace.listObjetMobile.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ChoixGroupeActivity extends AppCompatActivity {

    private String typeGroupe;
    private String role;
    private String idEtudiant;
    private String idGroupe;

    private ArrayList classementPerso = new ArrayList<>();

    private ArrayList<String> classementCapitaine = new ArrayList<>();
    private ArrayList<String> classementGroupe = new ArrayList<>();
    private RecyclerView recyclerViewGroupe;
    private RecyclerView recyclerViewCapitaine;

    private final String[] randomOrdre = {"Boîte d’allumettes", "Aliments concentrés", "Corde en nylon", "Parachute en soie", "Appareil de chauffage", "Pistolets de calibre 45", "Lait en poudre", "Réservoirs d’oxygène", "Carte céleste", "Canot de sauvetage", "Compas magnétique", "Réservoir d’eau", "Trousse médicale", "Signaux lumineux", "Émetteur-récepteur"};
    private final String[] agest = {"Réservoir d’oxygène", "Réservoir d’eau", "Carte céleste", "Aliments concentrés", "Émetteur-récepteur", "Corde en nylon", "Trousse médicale", "Parachute en soie", "Canot de sauvetage", "Signaux lumineux", "Pistolets de calibre 45", "Lait en poudre", "Appareil de chauffage", "Compas Magnétique", "Boîte d’allumettes"};

    public DAORefreshListeGroupe daoRefreshListeGroupe;
    private DAORefreshUpdateClassementTemporaire daoRefreshUpdateClassementTemporaire;


    private int phase;
    private int nbDeplacement = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.classementPerso = (ArrayList) getIntent().getSerializableExtra("classementPersoEtudiant");
        this.role = "";
        this.phase = 0;
        this.classementGroupe.addAll(Arrays.asList(ChoixPersoActivity.listObjets).subList(0, 15));
        this.classementCapitaine.addAll(Arrays.asList(ChoixPersoActivity.listObjets).subList(0, 15));
        recuperationGroupe();
    }

    private void recuperationGroupe() {
        setContentView(R.layout.activity_choix_groupe);

        Timer.getInstance().setTextView((TextView) findViewById(R.id.textTimer));
        Timer.getInstance().setActivity(this);
        Timer.getInstance().ajouterPhaseEtDemarrer();

        new DAOChoixGroupeActivity(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getGroupeEtudiant", "getGroupeEtudiant", this.idEtudiant);
    }

    @Override
    public void onBackPressed() {

    }

    public void setGroup(String typeGroupe) {
        this.typeGroupe = typeGroupe;
        TextView textView = findViewById(R.id.textView_AffichageGroupe);
        textView.setText("VOUS ÊTES DANS LE GROUPE " + this.typeGroupe);
        new DAOChoixGroupeActivity(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getRoleEtudiant", "getRoleEtudiant", this.idEtudiant);
    }

    public void setRole(String nomRole) {
        this.role = nomRole;
        new DAOChoixGroupeActivity(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getIdGroupeEtudiant", "getIdGroupeEtudiant", this.idEtudiant);
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe = idGroupe;
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
            creerListeImmobile();

            Timer.getInstance().setTextView((TextView) findViewById(R.id.textTimer));
            Timer.getInstance().setActivity(this);
            Timer.getInstance().ajouterPhaseEtDemarrer();
        }
    }

    //Remplissage des listes des objets
    private void creerListeImmobile() {
        crerListeClassementPerso();

        if (this.role.equals("Capitaine"))
            creerListCapitaine();
        else {
            creerListeChoixGroupe();
            this.daoRefreshListeGroupe = new DAORefreshListeGroupe(ChoixGroupeActivity.this, classementGroupe);
            this.daoRefreshListeGroupe.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.idGroupe);
            if (this.role.equals("Technicien")) {
                this.daoRefreshUpdateClassementTemporaire = new DAORefreshUpdateClassementTemporaire(ChoixGroupeActivity.this);
                this.daoRefreshUpdateClassementTemporaire.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.idGroupe, this.phase + "");
            }
        }

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
        RecyclerView recyclerViewClassmentPerso = (RecyclerView) findViewById(R.id.recyclerView_objetPerso);
        recyclerViewClassmentPerso.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClassmentPerso.setAdapter(new MyAdapter(classementPerso));
    }

    private void creerListeChoixGroupe() {
        recyclerViewGroupe = (RecyclerView) findViewById(R.id.recyclerViewObjetGroupe);
        recyclerViewGroupe.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerViewGroupe.setAdapter(new MyAdapter(classementGroupe));
    }

    private void creerListCentre(ArrayList<String> list) {

        RecyclerView recyclerViewVrac = (RecyclerView) findViewById(R.id.recyclerViewObjetVrac);
        recyclerViewVrac.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVrac.setAdapter(new MyAdapter(list));

    }

    private void creerListCapitaine() {
        recyclerViewCapitaine = findViewById(R.id.recyclerView_Capitaine_ChoixGroupeCapitaine);

        recyclerViewCapitaine.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(classementCapitaine,this);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewCapitaine);

        recyclerViewCapitaine.setAdapter(mAdapter);
    }

    private void updateListeCapitaine() {
        //recyclerViewCapitaine = findViewById(R.id.recyclerView_Capitaine_ChoixGroupeCapitaine);
        /*
        recyclerViewCapitaine.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(classementCapitaine);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewCapitaine);
        */

        //recyclerViewCapitaine.setAdapter(mAdapter);

        recyclerViewCapitaine.getAdapter().notifyDataSetChanged();
    }

    public void passageChoixClasse() {

        if (!this.role.equals("Capitaine"))
            this.daoRefreshListeGroupe.arreter();
        if (this.role.equals("Technicien"))
            this.daoRefreshUpdateClassementTemporaire.arreter();

        Intent intent = new Intent(this, ChoixClasseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idEtudiant", this.idEtudiant);
        bundle.putString("typeGroupe", this.typeGroupe);
        bundle.putString("roleEtudiant", this.role);
        bundle.putString("idGroupe", this.idGroupe);
        bundle.putStringArrayList("classementPerso", this.classementPerso);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void test(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Capitaine_ChoixGroupeCapitaine);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(this.classementCapitaine));
        this.recyclerViewCapitaine = recyclerView;
        System.out.println("----------------------------------------------------"+this.nbDeplacement);
    }


    //TODO regler le bug de scrool
    public void refreshClassementGroupe() {

        //this.recyclerViewGroupe.getRecycledViewPool().clear();
        //this.recyclerViewGroupe.getAdapter().notifyItemRangeInserted(0,15);
        //ArrayList<String> temp = classementGroupe;

        //MyAdapter mAdapter = new MyAdapter(temp);
       // this.recyclerViewGroupe.stopScroll();


        //recyclerViewGroupe.getRecycledViewPool().clear();
        //recyclerViewGroupe.getAdapter().notifyDataSetChanged();
        recyclerViewGroupe.setAdapter(new MyAdapter(this.classementGroupe));





       // mAdapter.notifyDataSetChanged();


        /*
        RecyclerView recyclerViewGroupe2 = (RecyclerView) findViewById(R.id.recyclerViewObjetGroupe);
        recyclerViewGroupe2.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGroupe2.setAdapter(new MyAdapter(classementGroupe));
        */
    }


    public void demandeConfirmation(View view) {
        new DAOClassementGroupe(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getClassementGroupe", "getClassementGroupe", this.idGroupe, this.phase + "");
    }

    //TODO BUG Affichage refreshClassementGRoupe pour le changement de phase.
    public void changementDePhase(View view) {
		
        Timer.getInstance().setTextView((TextView) findViewById(R.id.textTimer));
        Timer.getInstance().setActivity(this);
        Timer.getInstance().ajouterPhaseEtDemarrer();

        if (this.phase <2) {
            this.phase++;
            if (this.role.equals("Capitaine")) {
                this.classementCapitaine.subList(0, 5).clear();
                this.updateListeCapitaine();
            }
            if (this.role.equals("Technicien")) {
                this.daoRefreshUpdateClassementTemporaire.incrementPhase();
            }
        } else if (this.phase == 2) {
            this.phase++;
            new DAOClassementGroupe(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getAllClassementGroupe", "getAllClassementGroupe", this.idGroupe);
        } else
            passageChoixClasse();
    }

    public void passagePhaseQuatre(ArrayList<String> classementGroupe) {
        this.classementCapitaine = classementGroupe;
        updateListeCapitaine();
    }

    public void afficherRole(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(role);
        dialogBuilder.setMessage(getInfoRole(this.role));
        dialogBuilder.setCancelable(false).setPositiveButton("RETOUR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        dialogBuilder.create().show();
    }

    private String getInfoRole(String role) {
        switch (role) {
            case "Capitaine":
                return " - Vous souhaitez rejoindre Pan Luna.\n"
                        + " - Vous ne disposez d'aucune expertise technique.\n"
                        + " - CAPACITÉ SPÉCIALE: Vous possédez une forte aura auprès des autres membres qui vous permet d’être considéré comme le leader du groupe, d'attribuer la parole à chacun et de les écouter.\n"
                        + " - Vous assistez le technicien et révélez votre rôle.";
            case "Technicien":
                return " - Vous souhaitez rejoindre Pan Luna.\n"
                        + " - Vous disposez d'une expertise technique.\n"
                        + " - CAPACITÉ SPÉCIALE: Votre validation est essentielle avant d'attribuer un numéro pour chaque objet. Sans votre accord, la division ne peut pas attribuer de numéro à un objet.\n"
                        + " - Vous assistez le capitaine et révélez votre rôle.";
            case "Expert":
                return " - Vous souhaitez rejoindre Pan Luna.\n"
                        + " - Vous disposez d'une expertise technique parfaite.\n"
                        + " - CAPACITÉ SPÉCIALE: Vous possédez la liste complète et dans le bon ordre des 15 éléments de l'AGEST.\n"
                        + " - Vous ne devez pas révéler votre rôle.";
            case "Saboteur":
                return " - Vous NE SOUHAITEZ PAS rejoindre Pan Luna.\n"
                        + " - Vous êtes traumatisé et avez perdu la tête.\n"
                        + " - CAPACITÉ SPÉCIALE: Vous possèdez la liste complète et dans l'ordre inversé des 15 éléments de l'AGEST.\n"
                        + " - Attention: vous DEVEZ RESTER DISCRET ! \n"
                        + " - Vous ne devez pas révéler votre rôle.";
            case "Astronaute":
                return " - Vous souhaitez rejoindre Pan Luna.\n"
                        + " - Vous ne disposez d'aucune expertise technique.\n"
                        + " - Vous ne devez pas révéler votre rôle.";
            default:
                return "";
        }
    }

    public void afficherPopupTechnicien(String s) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Acceptez vous ce nouveau classement ?");
        dialogBuilder.setMessage(s);
        final String groupe = this.idGroupe;
        final String phase = this.phase + "";
        dialogBuilder.setCancelable(false).setPositiveButton("ACCEPTER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DAOPopupTechnicien(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "OK", groupe, phase);
            }
        });

        dialogBuilder.setCancelable(false).setNegativeButton("PAS ACCEPTER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DAOPopupTechnicien(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "PAS OK", groupe, phase);
            }
        });
        dialogBuilder.create().show();
    }

    public void classementEgal(ArrayList<String> classementBD) {
        boolean egal = compareClassement(classementBD);
        if (!egal) {
            ArrayList<String> classementTempo = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                classementTempo.add(classementCapitaine.get(i));
            }

            //TODO enlever le commentaire pour sauvegarde le classement tempo
            new DAOClassementTemp(ChoixGroupeActivity.this, classementTempo, this.phase).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "saveClassementTemp", "", this.idGroupe);
        } else
            Toast.makeText(this, "Pas de changement dans le classement", Toast.LENGTH_SHORT).show();
    }

    private boolean compareClassement(ArrayList<String> classementBD) {
        if (classementBD.size() == 0)
            return false;

        boolean ret = true;
        int i = 0;
        while (i < 5 && ret) {
            if (!this.classementCapitaine.get(i).equals(classementBD.get(i)))
                ret = false;
            i++;
        }
        return ret;
    }
}
