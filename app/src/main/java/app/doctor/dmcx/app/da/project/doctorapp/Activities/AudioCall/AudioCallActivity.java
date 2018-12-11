package app.doctor.dmcx.app.da.project.doctorapp.Activities.AudioCall;

import android.app.NotificationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Home.BaseActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Home.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AudioCallController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LDBModel;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Notification;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;

public class AudioCallActivity extends BaseActivity {

    // Variables
    private CircleImageView userProfilePicACCIV;
    private TextView userNameACTV;
    private TextView callStateACTV;
    private FloatingActionButton declineCallFab;
    private FloatingActionButton rejectCallFab;
    private FloatingActionButton acceptCallFab;

    private Call call;
    private SinchClient sinchClient;
    private Notification notification;
    private Patient patient;
    private String patientId;
    private CallHandler callHandler;
    private boolean isCallConnected = false;
    // Variables

    // Class
    private static class CallHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }

    private class CallRunnable implements Runnable {
        @Override
        public void run() {
            activeCall();
        }
    }
    // Class

    // Methods
    private void init() {
        userProfilePicACCIV = findViewById(R.id.userProfilePicACCIV);
        userNameACTV = findViewById(R.id.userNameACTV);
        callStateACTV = findViewById(R.id.callStateACTV);
        declineCallFab = findViewById(R.id.declineCallFab);
        rejectCallFab = findViewById(R.id.rejectCallFab);
        acceptCallFab = findViewById(R.id.acceptCallFab);

        callHandler = new CallHandler();
        patientId = getIntent().getStringExtra(Vars.Connector.AUDIO_CALL_ACTIVITY_DATA);
        AudioCallController.GetAudioCallPatientData(patientId, new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                patient = (Patient) object;

                if (!patient.getLink().equals(""))
                    Picasso.with(RefActivity.refACActivity.get()).load(patient.getLink()).placeholder(R.drawable.noperson).into(userProfilePicACCIV);

                userNameACTV.setText(patient.getName());
                callStateACTV.setText("Phone Connecting...");
            }
        });
    }

    private void event() {
        rejectCallFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCallConnected = false;

                rejectCall();
                finish();
            }
        });

        acceptCallFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCallConnected = true;
                AudioCallController.SaveCallHistory(patientId, AFModel.received);

                updateUi();
                receiveCall();
            }
        });

        declineCallFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCallConnected = true;

                rejectCall();
                finish();
            }
        });
    }

    private void cancelNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(Vars.localDB.retriveInteger(LDBModel.SAVE_CALL_NOTIFICATION_ID));
            manager.cancelAll();
        } else {
            Toast.makeText(RefActivity.refACActivity.get(), "Cancel notification failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void activeCall() {
        if (sinchClient.isStarted()) {
            if (call == null) {
                call = sinchClient.getCallClient().callUser(notification.getPatient_id());
                call.addCallListener(new SinchCallListener());
            }
        }
    }

    public void receiveCall() {
        if (call != null) {
            call.answer();
            call.addCallListener(new SinchCallListener());
        } else {
            Toast.makeText(RefActivity.refACActivity.get(), "Receive Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void rejectCall() {
        RefActivity.updateACActivity(HomeActivity.instance.get());

        if (call != null) {
            call.hangup();
        } else {
            Toast.makeText(RefActivity.refACActivity.get(), "Reject Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUi() {
        declineCallFab.setVisibility(View.VISIBLE);
        acceptCallFab.setVisibility(View.GONE);
        rejectCallFab.setVisibility(View.GONE);
    }
    // Methods

    @Override
    protected void onServiceConnected() {
        call = getSinchServiceBinder().getIncomingCall();
        if (call != null) {
            call.addCallListener(new SinchCallListener());
        } else {
            Log.e(Vars.TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_call);
        RefActivity.updateACActivity(this);
        init();
        event();
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallProgressing(Call call) {
            callStateACTV.setText("Ringing...");
        }

        @Override
        public void onCallEstablished(Call call) {
            callStateACTV.setText("Connected...");
        }

        @Override
        public void onCallEnded(Call endCall) {
            if (!isCallConnected) {
                AudioCallController.SaveCallHistory(patientId, AFModel.missed);
            }

            cancelNotification();
            rejectCall();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            finish();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            call.addCallListener(new SinchCallListener());
            callStateACTV.setText("Incoming...");
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(RefActivity.refACActivity.get(), "Can't go back at this stage", Toast.LENGTH_SHORT).show();
    }
}
