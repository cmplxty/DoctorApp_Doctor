package app.doctor.dmcx.app.da.project.doctorapp.Service;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.AudioCall.AudioCallActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LDBModel;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LocalDB;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Notification;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class MessagingServiceFirebase extends FirebaseMessagingService {

    @Override
    @SuppressLint("HardwareIds")
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String icon = remoteMessage.getNotification().getIcon();
        String activity = remoteMessage.getNotification().getClickAction();

        String doctorId = remoteMessage.getData().get("doctorId");
        String patientId = remoteMessage.getData().get("patientId");
        String type = remoteMessage.getData().get("type");
        String timestamp = remoteMessage.getData().get("timestamp");
        String deviceId = remoteMessage.getData().get("deviceId");
        String currentDeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if (!deviceId.equals(currentDeviceId)) {
            if (type.equals(AFModel.audio_call)) {
                if (mAuth.getCurrentUser() != null) {
                    if (doctorId.equals(mAuth.getCurrentUser().getUid())) {
                        audioCall(doctorId, patientId, timestamp, activity, title, body);
                    }
                }
            }
        }
    }

    private void audioCall(String doctorId, String patientId, String timestamp, String activity, String title, String body) {
        Notification notification = new Notification();
        notification.setDoctor_id(doctorId);
        notification.setPatient_id(patientId);
        notification.setTimestamp(timestamp);



//        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setOngoing(true);
//
//        int notificationId = (int) System.currentTimeMillis();
//        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        try {
//            nManager.cancel(0);
//        } catch (Exception e) {
//
//        }
//        if (nManager != null)
//            nManager.notify(notificationId, nBuilder.build());
//
//        if (Vars.localDB == null)
//            Vars.localDB = new LocalDB();
//        Vars.localDB.saveInteger(LDBModel.SAVE_CALL_NOTIFICATION_ID, notificationId);
    }
}
