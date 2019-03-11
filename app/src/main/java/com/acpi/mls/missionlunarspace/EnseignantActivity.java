package com.acpi.mls.missionlunarspace;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.acpi.mls.missionlunarspace.DAO.activity.DAOEnseignant;
import com.acpi.mls.missionlunarspace.DAO.autre.DAOAugmenterEtapes;
import com.acpi.mls.missionlunarspace.DAO.refresh.check.DAORefreshNbEtudiantEnregistre;
import com.acpi.mls.missionlunarspace.DAO.refresh.check.DAOCheckEnqueteFinie;

public class EnseignantActivity extends AppCompatActivity {

    private boolean isLogin = true;
    private int idClasse;
    private int nbEtudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enseignant);

        Button buttonOk = (Button) findViewById(R.id.buttonConfirmationLoginProf);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                switch(view.getId())
                {
                    case R.id.buttonConfirmationLoginProf:
                        new DAOEnseignant(EnseignantActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"getMdpProf", "EnseignantActivity.validerMDP");
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isLogin) {
            super.onBackPressed();
        }
    }

    protected void faireElementsCreationGroupe() {

        RadioButton r1 = (RadioButton) findViewById(R.id.radioGroupe1);
        RadioButton r2 = (RadioButton) findViewById(R.id.radioGroupe2);
        RadioButton r3 = (RadioButton) findViewById(R.id.radioGroupe3);
        RadioButton r4 = (RadioButton) findViewById(R.id.radioGroupe4);
        r1.setVisibility(View.INVISIBLE);
        r2.setVisibility(View.INVISIBLE);
        r3.setVisibility(View.INVISIBLE);
        r4.setVisibility(View.INVISIBLE);

        EditText nbPersonnes = (EditText) findViewById(R.id.nbEleves);
        nbPersonnes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int nb = 0;
                try {
                    nb = Integer.parseInt(editable.toString());
                } catch (NumberFormatException e) {

                }


                RadioButton r1 = (RadioButton) findViewById(R.id.radioGroupe1);
                RadioButton r2 = (RadioButton) findViewById(R.id.radioGroupe2);
                RadioButton r3 = (RadioButton) findViewById(R.id.radioGroupe3);
                RadioButton r4 = (RadioButton) findViewById(R.id.radioGroupe4);
                r1.setVisibility(View.INVISIBLE);
                r2.setVisibility(View.INVISIBLE);
                r3.setVisibility(View.INVISIBLE);
                r4.setVisibility(View.INVISIBLE);

                if ((nb / 5) >= 1 && ((nb - 1) / 8) < 1) {
                    r1.setVisibility(View.VISIBLE);
                }
                if ((nb / 5) >= 2 && ((nb - 1) / 8) < 2) {
                    r2.setVisibility(View.VISIBLE);
                }
                if ((nb / 5) >= 3 && ((nb - 1) / 8) < 3) {
                    r3.setVisibility(View.VISIBLE);
                }
                if ((nb / 5) >= 4 && ((nb - 1) / 8) < 4) {
                    r4.setVisibility(View.VISIBLE);
                }
            }
        });

        EditText nomClasse = (EditText) findViewById(R.id.nomClasse);
        EditText annee = (EditText) findViewById(R.id.annee);

        Button faireOk = (Button) findViewById(R.id.buttonCreationGroupe);
        faireOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nomClasse = (EditText) findViewById(R.id.nomClasse);
                EditText annee = (EditText) findViewById(R.id.annee);
                EditText nbPersonnes = (EditText) findViewById(R.id.nbEleves);
                RadioGroup groupe = (RadioGroup) findViewById(R.id.radioGroupSelectionGroupes);

                int nb = Integer.parseInt(nbPersonnes.getText().toString());

                boolean isGroupeValide = groupe.getCheckedRadioButtonId() != -1;

                if (isGroupeValide) {
                    RadioButton temp = (RadioButton) findViewById(groupe.getCheckedRadioButtonId());
                    isGroupeValide = temp.getVisibility() == View.VISIBLE;
                }

                switch (view.getId()) {
                    case R.id.buttonCreationGroupe:
                        if (nomClasse.getText().toString().equals("") || annee.getText().toString().equals("") || nbPersonnes.getText().toString().equals("")) {
                            Toast.makeText(EnseignantActivity.this, "Veuillez rentrer tous les champs", Toast.LENGTH_SHORT).show();
                        } else if (nb < 5 || nb > 36) {
                            Toast.makeText(EnseignantActivity.this, "Veuillez rentrer une quantité d'élèves valides (entre 5 et 32)", Toast.LENGTH_SHORT).show();
                        } else if (!isGroupeValide) {
                            Toast.makeText(EnseignantActivity.this, "Veuillez selectionner un groupe valide", Toast.LENGTH_SHORT).show();
                        } else {
                            int num = Integer.parseInt(((RadioButton) findViewById(groupe.getCheckedRadioButtonId())).getText().toString());
                            creerClasse(nomClasse.getText().toString(), annee.getText().toString(), nbPersonnes.getText().toString(), num + "");
                        }
                        break;
                }
            }
        });

    }

    public void creerClasse(String classe, String annee, String nbPersonnes, String nbGroupes) {
        this.nbEtudiant = Integer.parseInt(nbPersonnes);
        new DAOEnseignant(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"createClasse", "", classe, annee, nbPersonnes, nbGroupes);
    }

    public void validerMDP(String mdp) {
        EditText edit = (EditText) findViewById(R.id.mdpProf);
        if (edit != null) {
            String entre = edit.getText().toString();
            entre = entre == null ? "" : entre;
            if (entre.equals(mdp)) {
                setContentView(R.layout.activity_enseignant_config_groupe);
                this.isLogin = false;
                faireElementsCreationGroupe();
            } else {
                Toast.makeText(this, "Mot de passe éronné", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.mdpProf)).setText("");
            }
        }
    }

    public void erreurALaCreation() {
        Toast.makeText(this, "Il y a eu une erreur a la création", Toast.LENGTH_SHORT).show();
    }

    public void allerALancerPartie(String numClasse) {
        setContentView(R.layout.activity_demarrer_partie);
        TextView textView = findViewById(R.id.nbEtudiantEnregistre);
        textView.setText("Nombres d'éléves enregistré 0 sur "+this.nbEtudiant);
        new DAORefreshNbEtudiantEnregistre(EnseignantActivity.this,this.nbEtudiant).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"","");
        this.idClasse = Integer.parseInt(numClasse);
        faireElementsDemarrerPartie(numClasse);
    }

    private void faireElementsDemarrerPartie(final String numClasse) {
        Button demarrer = (Button) findViewById(R.id.buttonDemarrerPartie);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DAOEnseignant(EnseignantActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"DemarrerPartie", "indicationPartieDemarree", numClasse);
            }
        });
    }

    public void indiquerPartieDemaree(String heure) {
        setContentView(R.layout.activity_partie_demaree);
        TimerProf.createTimer(heure);
        TimerProf.getInstance().setTextView((TextView) findViewById(R.id.textTimer));
        TimerProf.getInstance().setActivity(this);
        TimerProf.getInstance().getInstance().ajouterPhaseEtDemarrer();
    }

    public void arreterClassementPerso() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Arrêter le classement individuel");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeClasse();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Le classement individuel est bien arrêté");

                lancerClassementGroupe(1);
            }
        });

        TextView texteTimer = (TextView) findViewById(R.id.textTimer);
        texteTimer.setText("");
    }

    public void lancerClassementGroupe(final int etape) {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Passer au classement de groupe - étape " + etape);
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimerProf.getInstance().getInstance().ajouterPhaseEtDemarrer();
                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeClasse();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Le classement de groupe - étape " + etape + " est bien démarré");
            }
        });

        TextView texteTimer = (TextView) findViewById(R.id.textTimer);
        texteTimer.setText("");
    }

    public void arreterClassementGroupe() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Arrêter le classement de groupe - étape 4");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeClasse();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Le classement de groupe - étape 4 est bien arrêté");

                lancerClassementClasse();
            }
        });

        TextView texteTimer = (TextView) findViewById(R.id.textTimer);
        texteTimer.setText("");
    }

    public void lancerClassementClasse() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Passer au classement de classe");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimerProf.getInstance().getInstance().ajouterPhaseEtDemarrer();
                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeClasse();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Le classement de classe est bien démarré");
            }
        });
    }

    public void arreterClassementClasse() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Arrêter le classement de classe");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeClasse();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Le classement de classe est bien arrêté");
                faireAttenteEnquete();
            }
        });

        TextView texteTimer = (TextView) findViewById(R.id.textTimer);
        texteTimer.setText("");
    }

    public void faireAttenteEnquete() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Passer à l'Enquête Spatiale");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeClasse();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("L'Enquête Spatiale est bien démarrée");
                new DAOCheckEnqueteFinie(EnseignantActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,idClasse + "");
            }
        });
    }

    public void faireAttenteSatisfaction() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Passer au Formulaire de satisfaction");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeClasse();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Le Formulaire est bien démarré");
            }
        });
    }

    private void augmenterEtapeClasse() {
        new DAOAugmenterEtapes(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,idClasse + "", "0");
    }

    public void faireAttenteSF1() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Passer au Résultat Personnel");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeScoreFinal();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Résultat Personnel");
                faireAttenteSF2();
            }
        });
    }

    public void faireAttenteSF2() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Passer au Résultat de Groupe");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeScoreFinal();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Résultat de Groupe");
                faireAttenteSF3();
            }
        });

    }

    public void faireAttenteSF3() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Passer au Résultat de Classe");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeScoreFinal();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Résultat de Classe");
                faireAttenteSF4();
            }
        });

    }

    public void faireAttenteSF4() {
        final Button demarrer = (Button) findViewById(R.id.boutonEtapeProf);
        demarrer.setText("Passer au Résultat AGEST");
        demarrer.setVisibility(View.VISIBLE);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demarrer.setVisibility(View.INVISIBLE);
                augmenterEtapeScoreFinal();

                TextView texte = (TextView) findViewById(R.id.textEtapeProf);
                texte.setText("Résultat AGEST");
                faireAttenteSatisfaction();
            }
        });

    }

    private void augmenterEtapeScoreFinal() {
        new DAOAugmenterEtapes(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,idClasse + "", "1");
    }

    public void refreshNbEtudiantEnregistre(int nbEtudiant) {
        TextView textView = findViewById(R.id.nbEtudiantEnregistre);
        textView.setText("Nombres d'éléves enregistré "+nbEtudiant+ " sur "+this.nbEtudiant);
    }
}
