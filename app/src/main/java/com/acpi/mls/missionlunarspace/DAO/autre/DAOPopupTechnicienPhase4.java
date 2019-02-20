package com.acpi.mls.missionlunarspace.DAO.autre;

import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOPopupTechnicienPhase4 extends DAO {

    private ChoixGroupeActivity choixGroupeActivity;

    public DAOPopupTechnicienPhase4(ChoixGroupeActivity choixGroupeActivity) {
        this.choixGroupeActivity = choixGroupeActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        if ("OK".equals(strings[0])) {
            resetClassement(Integer.parseInt(strings[1]));
            mettreClassementTemporaireDefinitif(Integer.parseInt(strings[1]));
        }
        resetClassementTemporaire(Integer.parseInt(strings[1]));
        changerMouvementPhase4(Integer.parseInt(strings[1]), "OK".equals(strings[0]));
        return null;
    }

    private void changerMouvementPhase4(int idGroupe, boolean isOk) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT idGroupe, enAttenteReponse, nbMouvements FROM MouvementPhase4 WHERE idGroupe = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1, idGroupe);
            ResultSet rs = pst.executeQuery();

            rs.next();
            rs.updateInt(2, 0);
            rs.updateInt(3, rs.getInt(3) + (isOk ? 1 : 0));

        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        choixGroupeActivity.redemarrerRefreshPhase4();
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

    private void resetClassement(int idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("DELETE FROM ClassementGroupe WHERE idGroupe = ?");
            pst.setInt(1, idGroupe);
            pst.executeUpdate();
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

    private void resetClassementTemporaire(int idGroupe) {
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
