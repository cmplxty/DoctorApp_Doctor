package app.doctor.dmcx.app.da.project.doctorapp.Activities.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Auth.AuthActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class SplashActivity extends AppCompatActivity {

    private SplashHandler splashHandlar = new SplashHandler();

    private static class SplashHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }

    private class SplashRunnable implements Runnable {

        @Override
        public void run() {
            RefActivity.refACActivity.get().startActivity(new Intent(RefActivity.refACActivity.get(), AuthActivity.class));
            RefActivity.refACActivity.get().finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RefActivity.updateACActivity(this);

        splashHandlar.postDelayed(new SplashRunnable(), 1000);
    }

}
