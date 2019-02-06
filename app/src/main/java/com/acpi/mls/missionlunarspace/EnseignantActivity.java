package com.acpi.mls.missionlunarspace;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.DAO.DAOEnseignant;

public class EnseignantActivity extends AppCompatActivity {

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
                        new DAOEnseignant(EnseignantActivity.this).execute("getMdpProf", "EnseignantActivity.validerMDP");
                        break;
                }
            }
        });
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

                if ((nb / 5) >= 1) {
                    r1.setVisibility(View.VISIBLE);
                }
                if ((nb / 5) >= 2) {
                    r2.setVisibility(View.VISIBLE);
                }
                if ((nb / 5) >= 3) {
                    r3.setVisibility(View.VISIBLE);
                }
                if ((nb / 5) >= 4) {
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
                            Toast.makeText(EnseignantActivity.this, "Veuillez rentrer une quantité d'élèves valides (entre 5 et 36)", Toast.LENGTH_SHORT).show();
                        } else if (!isGroupeValide) {
                            Toast.makeText(EnseignantActivity.this, "Veuillez selectionner un groupe valide", Toast.LENGTH_SHORT).show();
                        } else {
                            int num = Integer.parseInt(((RadioButton) findViewById(groupe.getCheckedRadioButtonId())).getText().toString());
                            creerGroupe(nomClasse.getText().toString(), annee.getText().toString(), nbPersonnes.getText().toString(), num + "");
                        }
                        break;
                }
            }
        });

    }

    public void creerGroupe(String classe, String annee, String nbPersonnes, String nbGroupes) {
        new DAOEnseignant(this).execute("createClasse", "", classe, annee, nbPersonnes, nbGroupes);
    }

    public void validerMDP(String mdp) {
        EditText edit = (EditText) findViewById(R.id.mdpProf);
        if (edit != null) {
            String entre = edit.getText().toString();
            entre = entre == null ? "" : entre;
            if (entre.equals(mdp)) {
                setContentView(R.layout.activity_enseignant_config_groupe);
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
        faireElementsDemarrerPartie(numClasse);
    }

    private void faireElementsDemarrerPartie(final String numClasse) {
        Button demarrer = (Button) findViewById(R.id.buttonDemarrerPartie);
        demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DAOEnseignant(EnseignantActivity.this).execute("DemarrerPartie", "indicationPartieDemarree", numClasse);
            }
        });
    }

    public void indiquerPartieDemaree() {
        setContentView(R.layout.activity_partie_demaree);
    }
}
