package app.doctor.dmcx.app.da.project.doctorapp.Fragments;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import app.doctor.dmcx.app.da.project.doctorapp.R;

public class AppPreferenceFragmentManager {

    public static final String PREFERENCE_TAG = "Preference Fragment Tag";

    public static void repace(AppCompatActivity appCompatActivity, Fragment fragment) {
        AppSupportFragmentManager.remove(appCompatActivity);

        appCompatActivity.getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.home_fragment_container, fragment, PREFERENCE_TAG)
                .commit();
    }

    public static void remove(AppCompatActivity appCompatActivity) {
        if(currentFragment(appCompatActivity) == null)
            return;

        appCompatActivity.getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .remove(currentFragment(appCompatActivity))
                .commit();
    }

    public static Fragment currentFragment(AppCompatActivity appCompatActivity) {
        return appCompatActivity.getFragmentManager().findFragmentByTag(PREFERENCE_TAG);
    }

}
