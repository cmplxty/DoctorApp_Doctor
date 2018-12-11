package app.doctor.dmcx.app.da.project.doctorapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AppSupportFragmentManager {

    public static final int homeFragmentContainer = R.id.home_fragment_container;

    public static Fragment addArgumentFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        return fragment;
    }

    public static void replace (AppCompatActivity appCompatActivity, int container, Fragment fragment, String tag) {
        AppPreferenceFragmentManager.remove(appCompatActivity);

        appCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(container, fragment, tag)
                .commit();

        Vars.currentFragment = fragment;
        updateOptionsMenu();
    }

    public static void replace (AppCompatActivity appCompatActivity, int container, Fragment fragment, String tag, Bundle bundle) {
        fragment.setArguments(bundle);

        replace(appCompatActivity, container, fragment, tag);
        updateOptionsMenu();
    }

    public static void remove(AppCompatActivity appCompatActivity) {
        if (Vars.currentFragment != null) {
            appCompatActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .remove(Vars.currentFragment)
                    .commit();
        }

        Vars.currentFragment = null;
    }

    private static void updateOptionsMenu() {
        if (RefActivity.refACActivity.get() != null)
            RefActivity.refACActivity.get().invalidateOptionsMenu();
    }
}
