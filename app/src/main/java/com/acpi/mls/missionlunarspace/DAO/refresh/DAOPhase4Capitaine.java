package com.acpi.mls.missionlunarspace.DAO.refresh;


import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.ChoixPersoActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IdentityHashMap;

public class DAOPhase4Capitaine extends DAO {

    private ArrayList<String> liste;
    private boolean continuer = true;
    private ChoixGroupeActivity choixGroupeActivity;

    public DAOPhase4Capitaine(ChoixGroupeActivity choixGroupeActivity, ArrayList<String> liste) {
        this.liste = liste;
        this.choixGroupeActivity = choixGroupeActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        posterClassementTemp(strings[0]);
        indiquerPhase4(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));

        int[] rep = {1, 0};

        while(continuer) {
            faireCN();
            rep = verifierPhase4(Integer.parseInt(strings[0]));

            if (rep[0] == 0) {
                arreter();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }

        resetPhase4(Integer.parseInt(strings[0]));

        String[] tab = {rep[1] + ""};
        return tab;
    }

    @Override
    protected void onPostExecute(String[] result) {
        choixGroupeActivity.mouvementRecu(Integer.parseInt(result[0]));
    }

    private void posterClassementTemp(String idGroupe) {
        try {
            for (int i = 0; i < 15; i++) {

                PreparedStatement preparedStatement = cn.prepareStatement("INSERT INTO ClassementGroupeTemp (idGroupe,idObjet,position) VALUES (?,?,?)");
                preparedStatement.setString(1, idGroupe);
                preparedStatement.setString(2, ChoixPersoActivity.findIdObjet(this.liste.get(i)));
                preparedStatement.setString(3, (i + 1) + "");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }

    }

    private void indiquerPhase4(int idGroupe, int nbMouv) {
        try {
            PreparedStatement pst = cn.prepareStatement("INSERT INTO MouvementPhase4 (idGroupe, enAttenteReponse, nbMouvements) VALUES (?, 1, ?)");
            pst.setInt(1, idGroupe);
            pst.setInt(2, nbMouv - 1);
            pst.executeUpdate();
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

    private int[] verifierPhase4(int idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT idGroupe, enAttenteReponse, nbMouvements FROM MouvementPhase4 WHERE idGroupe = ?");
            pst.setInt(1, idGroupe);
            ResultSet rs = pst.executeQuery();
            rs.next();

            int[] tab = {rs.getInt(2), rs.getInt(3)};
            return tab;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }

    private void resetPhase4(int idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("DELETE FROM MouvementPhase4 WHERE MouvementPhase4.idGroupe = ?");
            pst.setInt(1, idGroupe);
            pst.executeUpdate();
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

    public void arreter() {
        continuer = false;
    }
}
