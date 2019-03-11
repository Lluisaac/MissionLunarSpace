package com.acpi.mls.missionlunarspace.DAO.refresh.check;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.EnseignantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAORefreshNbEtudiantEnregistre extends DAO {

    private EnseignantActivity enseignantActivity;
    private int nbEtudiant;
    private int nbEtudiantTotal;


    public DAORefreshNbEtudiantEnregistre(EnseignantActivity enseignantActivity, int nbEtudiantTotal) {
        this.enseignantActivity = enseignantActivity;
        this.nbEtudiantTotal = nbEtudiantTotal;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        while (nbEtudiant < nbEtudiantTotal) {
            faireCN();
            countNbEtudiantPret();
            publishProgress(strings[0]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... result) {
        enseignantActivity.refreshNbEtudiantEnregistre(nbEtudiant);
    }

    private void countNbEtudiantPret() {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT COUNT(idEtudiant) FROM Etudiants WHERE groupe IS NULL");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                this.nbEtudiant = Integer.parseInt(rs.getString(1));
            }
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }
}
