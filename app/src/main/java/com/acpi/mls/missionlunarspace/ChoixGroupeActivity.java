package com.acpi.mls.missionlunarspace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.DAO.DAOChoixGroupeActivity;

public class ChoixGroupeActivity extends AppCompatActivity {

    private String typeGroupe;
    private String role;
    private String idEtudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.idEtudiant = (String) getIntent().getSerializableExtra("idEtudiant");
        this.role = "";
        recuperationGroupe();
    }

    private void recuperationGroupe() {
        setContentView(R.layout.activity_choix_groupe);
        new DAOChoixGroupeActivity(ChoixGroupeActivity.this).execute("getGroupeEtudiant", "getGroupeEtudiant", this.idEtudiant);
    }

    public void setGroup(String typeGroupe) {
        this.typeGroupe = typeGroupe;
        TextView textView = findViewById(R.id.textView_AffichageGroupe);
        textView.setText("VOUS ÊTES DANS LE GROUPE " + this.typeGroupe);

        new DAOChoixGroupeActivity(this).execute("getRoleEtudiant", "getRoleEtudiant", this.idEtudiant);
    }

    public void setRole(String nomRole) {
        this.role = nomRole;
    }

    public void continuerRole(View view) {
        if (!this.role.equals("")) {
            if (this.role.equals("Capitaine"))
                setContentView(R.layout.page_capitaine);

            if (this.role.equals("Technicien"))
                setContentView(R.layout.page_technicien);

            if (this.role.equals("Astronaute"))
                setContentView(R.layout.page_astronaute);

            if (this.role.equals("Expert"))
                setContentView(R.layout.page_expert);

            if (this.role.equals("Saboteur"))
                setContentView(R.layout.page_saboteur);

        }
    }


}
