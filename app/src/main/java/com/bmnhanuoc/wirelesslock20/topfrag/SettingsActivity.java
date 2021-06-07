package com.bmnhanuoc.wirelesslock20.topfrag;

import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragment;

import com.bmnhanuoc.wirelesslock20.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (findViewById(R.id.settingFrame) != null ){
            if (savedInstanceState != null){
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.settingFrame,new SettingsFragment()).commit();
        }
    }
}