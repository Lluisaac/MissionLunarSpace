package com.acpi.mls.missionlunarspace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChoixPerso_activity extends AppCompatActivity {

    private ListView mListView;
    private String[] prenoms = new String[]{
            "Antoine", "Benoit", "Cyril", "David", "Eloise", "Florent",
            "Gerard", "Hugo", "Ingrid", "Jonathan", "Kevin", "Logan",
            "Mathieu", "Noemie", "Olivia", "Philippe", "Quentin", "Romain",
            "Sophie", "Tristan", "Ulric", "Vincent", "Willy", "Xavier",
            "Yann", "Zoé"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_perso_activity);

        mListView = (ListView) findViewById(R.id.listViewTest);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChoixPerso_activity.this,
                android.R.layout.simple_list_item_1, prenoms);
        mListView.setAdapter(adapter);

    }

}
