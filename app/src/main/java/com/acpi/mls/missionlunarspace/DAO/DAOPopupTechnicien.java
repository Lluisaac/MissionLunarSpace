package com.acpi.mls.missionlunarspace.DAO;

import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.ChoixPersoActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOPopupTechnicien extends DAO {

    private ChoixGroupeActivity monChoixPerso;

    public DAOPopupTechnicien(ChoixGroupeActivity monChoixPerso) {
        this.monChoixPerso = monChoixPerso;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        if ("OK".equals(strings[0])) {
            resetClassement(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
            mettreClassementTemporaireDefinitif(Integer.parseInt(strings[1]));
        }
        resetClassementTemporaire(Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
        return null;
    }

    private void mettreClassementTemporaireDefinitif(int idGroupe) {
        try {
            PreparedStatement pst1 = cn.prepareStatement("SELECT idGroupe, idObjet, position FROM ClassementGroupeTemp WHERE idGroupe = ?");
            pst1.setInt(1, idGroupe);
            ResultSet rs1 = pst1.executeQuery();

            PreparedStatement pst2 = cn.prepareStatement("SELECT idGroupe, idObjet, position FROM ClassementGroupe WHERE idGroupe = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst2.setInt(1, idGroupe);
            ResultSet rs2 = pst2.executeQuery();

            while(rs1.next()) {
                rs2.moveToInsertRow();
                rs2.updateInt(1, rs1.getInt(1));
                rs2.updateInt(2, rs1.getInt(2));
                rs2.updateInt(3, rs1.getInt(3));
                rs2.insertRow();
            }

        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

    private void resetClassement(int idGroupe, int phase) {
        try {
            PreparedStatement pst = cn.prepareStatement("DELETE FROM ClassementGroupe WHERE idGroupe = ? AND position > ? AND position <= ?");
            pst.setInt(1, idGroupe);
            pst.setInt(2, phase * 5);
            pst.setInt(3, phase * 5 +5);
            pst.executeUpdate();
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

    private void resetClassementTemporaire(int idGroupe, int phase) {
        try {
            PreparedStatement pst = cn.prepareStatement("DELETE FROM ClassementGroupeTemp WHERE idGroupe = ?");
            pst.setInt(1, idGroupe);
            pst.executeUpdate();
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }
}
