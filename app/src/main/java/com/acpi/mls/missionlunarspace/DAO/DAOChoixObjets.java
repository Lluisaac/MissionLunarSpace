package com.acpi.mls.missionlunarspace.DAO;

import com.acpi.mls.missionlunarspace.ChoixPersoActivity;

public class DAOChoixObjets extends DAO {

    private ChoixPersoActivity monChoixPerso;

    public DAOChoixObjets(ChoixPersoActivity monChoixPerso) {
        this.monChoixPerso = monChoixPerso;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"",strings[1]};
        switch (strings[0]) {

        }
        return tab;
    }
}
