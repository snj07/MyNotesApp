package com.apps.sanjay.mynotes;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration config = getResources().getConfiguration();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            /**
             * Landscape mode of the device
             */
            AboutFragment_LMode ls_fragment = new AboutFragment_LMode();
            fragmentTransaction.replace(android.R.id.content, ls_fragment);
        } else {
            /**
             * Portrait mode of the device
             */
            AboutFragment_PMode pm_fragment = new AboutFragment_PMode();
            fragmentTransaction.replace(android.R.id.content, pm_fragment);
        }
        fragmentTransaction.commit();

    }
}
