package com.acpi.mls.missionlunarspace.DAO.refresh.check;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.EnseignantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCheckEnqueteFinie extends DAO {

    private EnseignantActivity activity;

    public DAOCheckEnqueteFinie(EnseignantActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        while (!isEnqueteFaite(Integer.parseInt(strings[0]))) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
        }
        return strings;
    }

    @Override
    protected void onPostExecute(String[] result) {
        activity.faireAttenteSF1();
    }

    private boolean isEnqueteFaite(int idClasse) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT COUNT(idGroupe) FROM Groupes WHERE classe = ? AND expertTrouve IS NULL ");
            pst.setInt(1, idClasse);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(1) == 0;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return false;
        }
    }
}
