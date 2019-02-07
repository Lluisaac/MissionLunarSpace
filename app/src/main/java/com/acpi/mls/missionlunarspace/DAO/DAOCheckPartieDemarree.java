package com.acpi.mls.missionlunarspace.DAO;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.acpi.mls.missionlunarspace.EnseignantActivity;
import com.acpi.mls.missionlunarspace.EtudiantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOCheckPartieDemarree extends DAO {

    private Button bouton;
    private int idClasse;

    public DAOCheckPartieDemarree(Button bouton, int idClasse) {
        this.bouton = bouton;
        this.idClasse = idClasse;
    }

    private boolean isPartieDemaree(int idClasse) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT idClasse, heureDepart FROM Classes WHERE idClasse = ?");
            pst.setInt(1, idClasse);
            ResultSet rs = pst.executeQuery();

            rs.next();
            rs.getString(2);
            if (rs.wasNull()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        while (!isPartieDemaree(idClasse)) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        bouton.setVisibility(View.VISIBLE);
    }
}
