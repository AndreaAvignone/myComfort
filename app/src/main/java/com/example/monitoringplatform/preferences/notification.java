package com.example.monitoringplatform.preferences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.monitoringplatform.R;
import com.example.monitoringplatform.myfragments.GeneralSettingsFragment;

public class notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitle("My Settings");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new GeneralSettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}

