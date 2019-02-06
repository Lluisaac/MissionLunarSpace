package com.acpi.mls.missionlunarspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnseignantActivity extends AppCompatActivity {
    private static EnseignantActivity instance;

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
                        new DAO().execute("getMdpProf", "EnseignantActivity.validerMDP");
                        break;
                }
            }
        });
        instance = this;
    }

    public static void validerMDP(String mdp) {
        String entre = ((EditText) instance.findViewById(R.id.mdpProf)).getText().toString();
        entre = entre == null ? "" : entre;
        if (entre.equals(mdp)) {
            System.out.println("REUSSI");
        } else {
            System.out.println("RATE");
        }
    }
}
