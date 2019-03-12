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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acpi.mls.missionlunarspace.DAO.activity.DAOChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.DAO.autre.DAOClassementGroupe;
import com.acpi.mls.missionlunarspace.DAO.autre.DAOClassementTemp;
import com.acpi.mls.missionlunarspace.DAO.autre.DAOPopupTechnicien;
import com.acpi.mls.missionlunarspace.DAO.autre.DAOPopupTechnicienPhase4;
import com.acpi.mls.missionlunarspace.DAO.autre.DAOClassementCapitaineSecure;
import com.acpi.mls.missionlunarspace.DAO.refresh.DAOPhase4Capitaine;
import com.acpi.mls.missionlunarspace.DAO.refresh.DAOPhase4Technicien;
import com.acpi.mls.missionlunarspace.DAO.refresh.DAORefreshListeGroupe;
import com.acpi.mls.missionlunarspace.DAO.refresh.DAORefreshUpdateClassementTemporaire;
import com.acpi.mls.missionlunarspace.DAO.refresh.check.DAOCheckEtape;
import com.acpi.mls.missionlunarspace.immobile.MyAdapter;
import com.acpi.mls.missionlunarspace.immobile.MyLinearLayoutManager;
import com.acpi.mls.missionlunarspace.listObjetMobile.ItemMoveCallback;
import com.acpi.mls.missionlunarspace.listObjetMobile.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private DAOPhase4Technicien daoRefreshPhase4Technicien;

    private int nbMouvements = 0;
    private boolean confirmerPossible = false;
    private ArrayList<String> classementPrecCapitaine = new ArrayList<>();

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

    public void fairePageAttente() {
        setContentView(R.layout.activity_choix_groupe);
        TextView textView = findViewById(R.id.textView_AffichageGroupe);
        textView.setText("VOUS ÊTES DANS LA DIVISION " + this.typeGroupe);

        new DAOCheckEtape(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idGroupe, "2");
    }

    public void sortirPageRegroupement() {
        continuerRole(null);
    }

    private void recuperationGroupe() {
        new DAOChoixGroupeActivity(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getGroupeEtudiant", "getGroupeEtudiant", this.idEtudiant);
    }

    @Override
    public void onBackPressed() {

    }

    public void setGroup(String typeGroupe) {
        this.typeGroupe = typeGroupe;
        new DAOChoixGroupeActivity(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getRoleEtudiant", "getRoleEtudiant", this.idEtudiant);
    }

    public void setRole(String nomRole) {
        this.role = nomRole;
        new DAOChoixGroupeActivity(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getIdGroupeEtudiant", "getIdGroupeEtudiant", this.idEtudiant);
    }

    public int getIdGroupe() {
        return Integer.parseInt(this.idGroupe);
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
        }

        TimerEtudiant.getInstance().setTextView((TextView) findViewById(R.id.textTimer));
        TimerEtudiant.getInstance().setActivity(this);
        TimerEtudiant.getInstance().ajouterPhaseEtDemarrer();
    }

    //Remplissage des listes des objets
    private void creerListeImmobile() {
        crerListeClassementPerso();

        if (this.role.equals("Capitaine")) {
            creerListCapitaine();
        } else {
            creerListeChoixGroupe();
            this.daoRefreshListeGroupe = new DAORefreshListeGroupe(ChoixGroupeActivity.this);
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
        RecyclerView recyclerViewClassmentPerso = findViewById(R.id.recyclerView_objetPerso);
        recyclerViewClassmentPerso.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClassmentPerso.setAdapter(new MyAdapter(classementPerso));
    }

    private void creerListeChoixGroupe() {
        recyclerViewGroupe = findViewById(R.id.recyclerViewObjetGroupe);
        recyclerViewGroupe.setLayoutManager(new MyLinearLayoutManager(this));
        recyclerViewGroupe.setAdapter(new MyAdapter(classementGroupe));
    }

    private void creerListCentre(ArrayList<String> list) {

        RecyclerView recyclerViewVrac = findViewById(R.id.recyclerViewObjetVrac);
        recyclerViewVrac.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVrac.setAdapter(new MyAdapter(list));

    }

    private void creerListCapitaine() {
        recyclerViewCapitaine = findViewById(R.id.recyclerView_Capitaine_ChoixGroupeCapitaine);

        recyclerViewCapitaine.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(classementCapitaine, this);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewCapitaine);

        recyclerViewCapitaine.setAdapter(mAdapter);
    }

    private void updateListeCapitaine() {
        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerViewCapitaine.getAdapter();
        adapter.setData(classementCapitaine);
        adapter.notifyDataSetChanged();
    }

    public void passageChoixClasse() {

        if (!this.role.equals("Capitaine")) {
            this.daoRefreshListeGroupe.arreter();
        }

        if (this.role.equals("Technicien")) {
            this.daoRefreshPhase4Technicien.arreter();
        }

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

    private void setClassementPrecedent() {
        this.classementPrecCapitaine = new ArrayList<>(classementCapitaine);
    }

    private void revertClassement() {
        this.classementCapitaine = new ArrayList<>(classementPrecCapitaine);
        updateListeCapitaine();
    }

    public void mouvementFait() {
        Toast.makeText(this, "La liste est à présent immobile", Toast.LENGTH_SHORT).show();
        setAdapterBougeable(false);
        appartitionBoutonReset(true);
        this.nbMouvements++;
        this.confirmerPossible = true;
    }

    public void resetMouvement(View view) {
        revertClassement();
        confirmerMouvementRecu();
        this.nbMouvements--;
    }

    private void confirmerMouvementRecu() {
        this.setAdapterBougeable(true);
        appartitionBoutonReset(false);
        this.confirmerPossible = false;
    }

    public void faireDemandeMouvement() {
        this.confirmerPossible = false;
        appartitionBoutonReset(false);
        new DAOPhase4Capitaine(this, classementCapitaine).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.idGroupe + "", this.nbMouvements + "");
    }

    public void mouvementRecu(int nbMouv) {
        if (nbMouv != this.nbMouvements) {
            resetMouvement(null);
            Toast.makeText(this, "Le technicien refuse.", Toast.LENGTH_SHORT).show();
        } else {
            confirmerMouvementRecu();
            Toast.makeText(this, "Le technicien accepte.", Toast.LENGTH_SHORT).show();
            if (nbMouv == 3) {
                this.setAdapterBougeable(false);
            }
        }
    }

    private void appartitionBoutonReset(boolean b) {
        Button but = findViewById(R.id.boutonReset);
        but.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }

    public void setAdapterBougeable(boolean b) {
        ((RecyclerViewAdapter) recyclerViewCapitaine.getAdapter()).setMovability(b);
    }

    public void refreshClassementGroupe(ArrayList<String> temp) {
        this.classementGroupe = temp;
        MyAdapter adapter = (MyAdapter) recyclerViewGroupe.getAdapter();
        adapter.setList(this.classementGroupe);
        adapter.notifyDataSetChanged();
    }


    public void demandeConfirmation(View view) {
        new DAOClassementGroupe(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getClassementGroupe", "getClassementGroupe", this.idGroupe, this.phase + "");
    }

    public void changementDePhase(View view) {
        if (phase < 4) {
            Toast.makeText(this, "Vous êtes dans la Phase " + (phase + 2), Toast.LENGTH_SHORT).show();
        }

        if (this.phase <= 2 ) {
            TimerEtudiant.getInstance().setTextView((TextView) findViewById(R.id.textTimer));
            TimerEtudiant.getInstance().setActivity(this);
            TimerEtudiant.getInstance().ajouterPhaseEtDemarrer();

            if (this.role.equals("Capitaine")) {

                //TODO enlever les bon objets par rapport a la DB et pas les 5 premier

                ArrayList<String> classementTempo = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    classementTempo.add(classementCapitaine.get(i));
                }
                new DAOClassementCapitaineSecure(classementTempo, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.idGroupe, this.phase + "");
            }
        }

        if (this.phase < 2) {
            this.phase++;
            if (this.role.equals("Capitaine")) {
                this.updateListeCapitaine();
            }
            if (this.role.equals("Technicien")) {
                this.daoRefreshUpdateClassementTemporaire.incrementPhase();
            }
        } else if (this.phase == 2) {
            this.phase++;
            new DAOClassementGroupe(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getAllClassementGroupe", "getAllClassementGroupe", this.idGroupe);
        } else {
            passageAttenteClasse();
        }

        TextView textePhase = findViewById(R.id.textePhase);
        textePhase.setText("Etape " + (this.phase + 1));
    }

    public void passageAttenteClasse() {
        setContentView(R.layout.content_groupe_attente);
        new DAOCheckEtape(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idGroupe, "7");
    }

    public void passagePhaseQuatre(ArrayList<String> classementGroupe) {
        if (this.role.equals("Capitaine")) {
            this.classementCapitaine = classementGroupe;
            updateListeCapitaine();
            setClassementPrecedent();

            Button but = findViewById(R.id.buttonCapitaineDemande);
            but.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.buttonCapitaineDemande:
                            if (confirmerPossible) {
                                faireDemandeMouvement();
                            } else {
                                Toast.makeText(ChoixGroupeActivity.this, "Vous devez faire un mouvement ou attendre confirmation du Technicien", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }
            });
        } else if (this.role.equals("Technicien")) {
            daoRefreshUpdateClassementTemporaire.arreter();
            this.daoRefreshPhase4Technicien = new DAOPhase4Technicien(this);
            this.daoRefreshPhase4Technicien.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idGroupe);
        }
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

    public static String getInfoRole(String role) {
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
                new DAOPopupTechnicien().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "OK", groupe, phase);
            }
        });

        dialogBuilder.setCancelable(false).setNegativeButton("PAS ACCEPTER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DAOPopupTechnicien().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "PAS OK", groupe, phase);
            }
        });
        dialogBuilder.create().show();
    }

    public void afficherPopupTechnicienPhase4(String s) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Acceptez vous ce nouveau classement ?");
        dialogBuilder.setMessage(s);
        final String groupe = this.idGroupe;
        dialogBuilder.setCancelable(false).setPositiveButton("ACCEPTER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DAOPopupTechnicienPhase4(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "OK", groupe);
            }
        });

        dialogBuilder.setCancelable(false).setNegativeButton("PAS ACCEPTER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DAOPopupTechnicienPhase4(ChoixGroupeActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "PAS OK", groupe);
            }
        });
        dialogBuilder.create().show();
    }

    public void redemarrerRefreshPhase4() {
        this.daoRefreshPhase4Technicien = new DAOPhase4Technicien(this);
        this.daoRefreshPhase4Technicien.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idGroupe);
    }

    public void classementEgal(ArrayList<String> classementBD) {
        boolean egal = compareClassement(classementBD);
        if (!egal) {
            ArrayList<String> classementTempo = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                classementTempo.add(classementCapitaine.get(i));
            }

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

    public void faireAttente(int pourPhase) {
        setContentView(R.layout.activity_attente_groupe);
        TextView textView = findViewById(R.id.texte_attente);
        textView.setText("Veuillez attendre que le professeur démarre l'étape " + (pourPhase / 2));

        new DAOCheckEtape(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idGroupe + "", pourPhase + "");
    }

    public int getPhase() {
        return phase;
    }

    public void setClassementRestantCapitaine(List<String> classementTemp) {

        for (String str : classementTemp) {
            this.classementCapitaine.remove(str);
        }
        this.updateListeCapitaine();
    }
/*
    public void goFormulaire(View view) {
        Intent intent = new Intent(this, FormulaireSatisfactionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idEtudiant", this.idEtudiant);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    */
}
