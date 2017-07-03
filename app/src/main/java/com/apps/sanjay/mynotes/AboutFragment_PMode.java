package com.apps.sanjay.mynotes;

/**
 * Created by sanjay on 14/6/17.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class AboutFragment_PMode extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.about_fragment_pmode, container, false);
    }
}
