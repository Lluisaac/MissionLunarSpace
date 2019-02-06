package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EtudiantActivity extends AppCompatActivity {

    private String nomEtu;
    private String classeEtu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant);
    }

    public void onClikButtonContinuer(View view) throws InterruptedException {
        EditText etuNom  = findViewById (R.id.textViewNomEtu);
        String nom = etuNom.getText().toString();

        EditText etuClasse = findViewById(R.id.textViewClasseEtu) ;
        String classe = etuClasse.getText().toString();

        if(verifText(nom,classe)) {
            this.nomEtu = nom;
            this.classeEtu = classe;
            setContentView(R.layout.content_etudiant_attente);
        }else {
            Toast.makeText(this, "Vous devez renseigner tous les champs !", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCLickTempo(View view)
    {
        Intent intent = new Intent(this,ChoixPersoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("NomEtu", nomEtu);
        bundle.putString("ClasseEtu",classeEtu);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private boolean verifText(String nom, String classe) {
        return !classe.equals("") && !nom.equals("");
    }
}