package com.acpi.mls.missionlunarspace.DAO.refresh.check;

import com.acpi.mls.missionlunarspace.ChoixClasseActivity;
import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.ChoixPersoActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.ScoreFinalActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCheckEtape extends DAO {

    private ChoixPersoActivity activityPerso;
    private ChoixGroupeActivity activityGroupe;
    private ChoixClasseActivity activityClasse;
    private ScoreFinalActivity activityScoreFinal;

    public DAOCheckEtape(ChoixPersoActivity activity) {
        this.activityPerso = activity;
    }
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
        while (!isEtapeDeGroupe(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]))) {
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
                //Sortie de la page perso
                activityPerso.continuerChoixGroupe(null);
                break;
            case "2":
                //Sortie du regroupement, passage au CG1
                activityGroupe.sortirPageRegroupement();
                break;
            case "3":
                //Sortie du CG1
                activityGroupe.faireAttente(4);
                break;
            case "4":
                //Sortie de l'attente, passage au CG2
                activityGroupe.changementDePhase(null);
                break;
            case "5":
                //Sortie du CG2
                activityGroupe.faireAttente(6);
                break;
            case "6":
                //Sortie de l'attente, passage au CG3
                activityGroupe.changementDePhase(null);
                break;
            case "7":
                //Sortie du CG3
                activityGroupe.faireAttente(8);
                break;
            case "8":
                //Sortie de l'attente, passage au CG4
                activityGroupe.changementDePhase(null);
                break;
            case "9":
                //Sortie du CG4
                activityGroupe.passageAttenteClasse();
                break;
            case "10":
                //Sortie de l'attente, passage au classement de classe
                activityGroupe.passageChoixClasse();
                break;
            case "11":
                //Sortie du classement de classe
                activityClasse.passageAttenteDenonciation(null);
                break;
            case "12":
                //Sortie de l'attente, passage a l'enquete
                activityClasse.passageDenonciation();
                break;
            case "13":
                //Sortie de l'enquete, passage au formulaire de satisfaction
                activityScoreFinal.passageFormSatisfaction(null);
                break;
        }
    }

    private boolean isEtapeDeGroupe(int idGroupe, int etape) {
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
