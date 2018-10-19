package app.doctor.dmcx.app.da.project.doctorapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AppFragmentManager {

    public static final int homeFragmentContainer = R.id.home_fragment_container;

    public static Fragment addArgumentFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        return fragment;
    }

    public static void replace (AppCompatActivity appCompatActivity, int container, Fragment fragment, String tag) {
        appCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(container, fragment, tag)
                .commit();

        Vars.currentFragment = fragment;
    }

}
