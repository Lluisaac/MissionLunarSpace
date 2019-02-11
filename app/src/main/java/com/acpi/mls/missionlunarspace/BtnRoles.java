package com.acpi.mls.missionlunarspace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class BtnRoles extends AppCompatActivity {

    private Button btnRole;
    private TextView role;
    private boolean cache;


    public Button getBtnRole() {
        return btnRole;
    }

    public void setBtnRole(Button btnRole) {
        this.btnRole = btnRole;
    }

    public TextView getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role.setText(role);
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }


    public BtnRoles() {
        setCache(true);
    }

    /**
     * @param role il faut récuperer le role de la personne à partir de la BD
     * @param btn
     */
    public BtnRoles(TextView role, Button btn) {
        this.setBtnRole(btn);
        this.role = role;
        setCache(true);
    }

    public void clickBtn() {
        if (isCache()) {
            setCache(false);
            role.setVisibility(View.INVISIBLE);
            btnRole.setText("Montrer");
        } else {
            setCache(true);
            role.setVisibility(View.VISIBLE);
            btnRole.setText("Cacher");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bouton_role);
        final BtnRoles btnRole = new BtnRoles();
        Button btn = findViewById(R.id.btnRole);
        TextView txt = findViewById(R.id.txtRole);

        btnRole.setBtnRole(btn);
        //btnRole.setRole(txt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRole.clickBtn();
            }
        });
    }
}