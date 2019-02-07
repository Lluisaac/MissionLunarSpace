package com.acpi.mls.missionlunarspace.DAO;

import com.acpi.mls.missionlunarspace.ChoixPersoActivity;

import java.util.ArrayList;

public class DAOChoixPersoActivity extends DAO {

    private ChoixPersoActivity monChoixPerso;
    private String[] strings;

    public DAOChoixPersoActivity(ChoixPersoActivity monChoixPerso) {
        this.monChoixPerso = monChoixPerso;
    }


    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"",strings[1]};
        switch (strings[0]) {
            case "saveObjet":
                saveClassementObjet(strings[2],strings[3]);
                break;
        }
        return tab;
    }

    private void saveClassementObjet(String nomObjet, String position){

    }
}
