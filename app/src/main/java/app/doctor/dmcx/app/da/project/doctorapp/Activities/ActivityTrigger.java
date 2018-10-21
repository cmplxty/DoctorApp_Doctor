package app.doctor.dmcx.app.da.project.doctorapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Appointment.SetupAppointmentActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Messenger.PrescriptionActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Messenger.ViewImageActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Messenger.MessageActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class ActivityTrigger {

    /*
    * Auth Activity
    * */
    public static void AuthActivity() {
        Activity activity = RefActivity.refACActivity.get();
        activity.startActivity(new Intent(RefActivity.refACActivity.get(), AuthActivity.class));
        activity.finish();
    }

    /*
     * MessageUserList Activity
     * */
    public static void MessageActivity(Parcelable parcelable) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, MessageActivity.class);
        intent.putExtra(Vars.Connector.MESSAGE_ACTIVITY_DATA, parcelable);
        activity.startActivity(intent);

        if (activity instanceof HomeActivity) {
            return;
        }

        activity.finish();
    }

    /*
     * View Image Activity
     * */
    public static void ViewImageActivity(String url) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, ViewImageActivity.class);
        intent.putExtra(Vars.Connector.VIEW_IMAGE_DATA, url);
        activity.startActivity(intent);
    }

    /*
     * Prescription Activity
     * */
    public static void PrescriptionActivity(Parcelable parcelable) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, PrescriptionActivity.class);
        intent.putExtra(Vars.Connector.PERSCRIPTION_ACTIVITY_DATA, parcelable);
        activity.startActivity(intent);
    }

    /*
     * Setup APRequest Activity
     * */
    public static void SetupAppointmentActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, SetupAppointmentActivity.class);
        activity.startActivity(intent);
    }

}
