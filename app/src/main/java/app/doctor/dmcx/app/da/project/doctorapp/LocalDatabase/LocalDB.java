package app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;

public class LocalDB {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public LocalDB() {
        sharedPreferences = RefActivity.refACActivity.get().getSharedPreferences(LDBModel.DB_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String retriveString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void clearLocalDB() {
        editor.clear();
        editor.apply();
    }

}
