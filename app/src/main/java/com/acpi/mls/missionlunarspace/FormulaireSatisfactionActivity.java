package com.acpi.mls.missionlunarspace;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.acpi.mls.missionlunarspace.DAO.activity.DAOFormulaireStatisfactionActivity;

public class FormulaireSatisfactionActivity extends AppCompatActivity {

    private int idEtudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idEtudiant = (int) getIntent().getSerializableExtra("idEtudiant");
        setContentView(R.layout.activity_formulaire_satisfaction);
    }

    @Override
    public void onBackPressed() {
    }


    public void saveForm(View view) {
        EditText emc = findViewById(R.id.motGestionCapitaine);
        String mC = emc.getText().toString();

        EditText emt = findViewById(R.id.motGestionTechnicien);
        String mT = emt.getText().toString();

        EditText end = findViewById(R.id.noteCollective);
        String  nD = end.getText().toString();

        EditText enl = findViewById(R.id.noteLeadership);
        String nL = enl.getText().toString();

        EditText eno = findViewById(R.id.noteCollective);
        String nO = eno.getText().toString();

        EditText enc = findViewById(R.id.noteCommunication);
        String nC = enc.getText().toString();

        new DAOFormulaireStatisfactionActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "saveForm","", this.idEtudiant + "", mC, mT,nD,nL,nO,nC);

        setContentView(R.layout.page_fin);
    }
}
