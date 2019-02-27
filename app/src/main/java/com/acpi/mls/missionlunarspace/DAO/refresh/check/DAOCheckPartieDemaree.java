package com.acpi.mls.missionlunarspace.DAO.refresh.check;

import android.view.View;
import android.widget.Button;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.DAO.autre.DAORecupTemps;
import com.acpi.mls.missionlunarspace.EtudiantActivity;
import com.acpi.mls.missionlunarspace.Timer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCheckPartieDemaree extends DAO {

    private final EtudiantActivity etudiantActivity;
    private Button bouton;
    private int idClasse;

    public DAOCheckPartieDemaree(Button bouton, int idClasse, EtudiantActivity etudiantActivity) {
        this.etudiantActivity = etudiantActivity;
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
        etudiantActivity.pouvoirChangerPage();
    }
}