package com.acpi.mls.missionlunarspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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
       this.nomEtu = etuNom.getText().toString();

       EditText etuClasse = findViewById(R.id.textViewClasseEtu) ;
       this.classeEtu = etuClasse.getText().toString();
       setContentView(R.layout.content_etudiant_attente);
    }

    public void onCLickTempo(View view)
    {
        Intent intent = new Intent(this,ChoixPerso_activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("NomEtu", nomEtu);
        bundle.putString("ClasseEtu",classeEtu);

        intent.putExtras(bundle);
        startActivity(intent);
    }

}
