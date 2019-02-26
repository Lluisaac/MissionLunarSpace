package com.acpi.mls.missionlunarspace.DAO.refresh.check;

import com.acpi.mls.missionlunarspace.ChoixClasseActivity;
import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.ScoreFinalActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCheckEtape extends DAO {

    private ChoixGroupeActivity activityGroupe;
    private ChoixClasseActivity activityClasse;
    private ScoreFinalActivity activityScoreFinal;

    public DAOCheckEtape(ChoixGroupeActivity activity) {
        this.activityGroupe = activity;
    }
    public DAOCheckEtape(ChoixClasseActivity activity) {
        this.activityClasse = activity;
    }
    public DAOCheckEtape(ScoreFinalActivity activity) {
        this.activityScoreFinal = activity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        while (!isClassementPersoFait(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]))) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
        }
        return strings;
    }

    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "1":
                activityGroupe.sortirPageRegroupement();
                break;
            case "2":
                activityGroupe.passageChoixClasse();
                break;
            case "3":
                activityClasse.passageDenonciation();
                break;
            case "4":
                activityScoreFinal.passageFormSatisfaction(null);
                break;
        }
    }

    private boolean isClassementPersoFait(int idGroupe, int etape) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT etape, idClasse FROM Classes cl JOIN Groupes gp ON cl.idClasse = gp.classe WHERE idGroupe = ?");
            pst.setInt(1, idGroupe);
            ResultSet rs = pst.executeQuery();

            rs.next();
            if (rs.getInt(1) < etape) {
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
}
