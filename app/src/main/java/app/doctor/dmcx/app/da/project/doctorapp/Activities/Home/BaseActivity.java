package app.doctor.dmcx.app.da.project.doctorapp.Activities.Home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Service.SinchService;

public abstract class BaseActivity extends AppCompatActivity implements ServiceConnection {

    private SinchService.SinchServiceBinder sinchServiceBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().bindService(new Intent(this, SinchService.class), this, BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            sinchServiceBinder = (SinchService.SinchServiceBinder) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            sinchServiceBinder = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {

    }

    protected void onServiceDisconnected() {

    }

    protected SinchService.SinchServiceBinder getSinchServiceBinder() {
        return sinchServiceBinder;
    }
}
