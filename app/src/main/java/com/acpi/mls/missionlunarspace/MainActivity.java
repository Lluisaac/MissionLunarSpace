package com.acpi.mls.missionlunarspace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.acpi.mls.missionlunarspace.DAO.autre.DAOPopupTechnicien;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void lancerJeu(View view) {
        Intent intent = new Intent(this, SelectionEtudiantEnseignant.class);
        startActivity(intent);
    }

    public void credits(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Credits");
        dialogBuilder.setMessage("Application faite par:\nFlorian Bouffard-Vercelli\nSafaa Jaroudi\nIsaac Lluís\nEmma Macqueron\nJordan Tremoulet");

        dialogBuilder.setCancelable(false).setNegativeButton("Retour", null);
        dialogBuilder.create().show();
    }
}
