package app.doctor.dmcx.app.da.project.doctorapp.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.AudioCall.AudioCallActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AudioCallController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AppFirebase;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class SinchService extends Service {

    public class SinchServiceBinder extends Binder {
        public Call callUser(String userId) {
            return mSinchClient.getCallClient().callUser(userId);
        }
        public String getUserName() {
            return mUserId;
        }
        public boolean isStarted() {
            return SinchService.this.isStarted();
        }
        public void startClient() {
            start();
        }
        public void stopClient() {
            stop();
        }
        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }
        public Call getCall(String callId) {
            return mSinchClient.getCallClient().getCall(callId);
        }
        public Call getIncomingCall() {
            return incomingCall;
        }
    }

    public interface StartFailedListener {
        void onStartFailed(SinchError error);
        void onStarted();
    }

    private SinchServiceBinder mSinchServiceBinder = new SinchServiceBinder();
    private SinchClient mSinchClient;
    private String mUserId;
    private StartFailedListener mListener;
    private Call incomingCall;

    private void start() {
        if (mSinchClient == null) {
            if (Vars.appFirebase == null)
                Vars.appFirebase = new AppFirebase();

            if (Vars.appFirebase.getCurrentUser() == null) return;

            mUserId = Vars.appFirebase.getCurrentUser().getUid();
            mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext())
                    .userId(Vars.appFirebase.getCurrentUser().getUid())
                    .applicationKey(Vars.Sinch.APP_KEY)
                    .applicationSecret(Vars.Sinch.APP_SECRET)
                    .environmentHost(Vars.Sinch.ENVIRONMENT)
                    .build();

            mSinchClient.setSupportCalling(true);
            mSinchClient.startListeningOnActiveConnection();

            mSinchClient.addSinchClientListener(new AppSinchClientListener());
            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            mSinchClient.start();
        }
    }

    private void stop() {
        if (mSinchClient != null) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (mSinchClient != null && mSinchClient.isStarted());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.terminate();
        }
        super.onDestroy();
    }

    private class AppSinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            mSinchClient.terminate();
            mSinchClient = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {

        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration clientRegistration) {

        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {
            final String userId = call.getRemoteUserId();
            incomingCall = call;

            if (userId != null) {
                AudioCallController.CheckAudioCallDeviceId(userId, new IAction() {
                    @Override
                    public void onCompleteAction(Object object) {
                        if (object != null) {
                            @SuppressLint("HardwareIds")
                            String currentDeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                            String deviceId = String.valueOf(object);

                            if (currentDeviceId.equals(deviceId)) {
                                Toast.makeText(getApplicationContext(), ValidationText.SameDeviceConfict, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Intent audioCallIntent = new Intent(RefActivity.refACActivity.get(), AudioCallActivity.class);
                            audioCallIntent.putExtra(Vars.Connector.AUDIO_CALL_ACTIVITY_DATA, userId);
                            audioCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SinchService.this.startActivity(audioCallIntent);
                        }
                    }
                });
            }
        }
    }
}
