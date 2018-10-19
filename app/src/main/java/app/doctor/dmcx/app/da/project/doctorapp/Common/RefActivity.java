package app.doctor.dmcx.app.da.project.doctorapp.Common;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class RefActivity {

    public static WeakReference<AppCompatActivity> refACActivity;

    public static void updateACActivity(AppCompatActivity instance) {
        refACActivity = new WeakReference<>(instance);
    }

}
