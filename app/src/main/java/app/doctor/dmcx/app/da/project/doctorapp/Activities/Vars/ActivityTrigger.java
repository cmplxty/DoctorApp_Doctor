package app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Appointment.SetupAppointmentActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Assistant.CreateNewAssistantActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Auth.AuthActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Blog.BlogEditorActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Blog.BlogViewerActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Blog.MyBlogActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Home.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.HomeService.HomeServiceRegisterActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Messenger.PrescriptionActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Messenger.ViewImageActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Messenger.MessageActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Blog;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
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
    public static void MessageActivity(Patient patient) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, MessageActivity.class);
        intent.putExtra(Vars.Connector.MESSAGE_ACTIVITY_DATA, patient);
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

    /*
     * Blog Editor Activity
     * */
    public static void BlogEditorActivity(String parentActivity, Blog blog) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, BlogEditorActivity.class);
        intent.putExtra(Vars.ParentActivity.TRIG_BLOG_EDITOR_ACTIVITY, parentActivity);
        intent.putExtra(Vars.Connector.BLOG_EDITOR_ACTIVITY_DATA, blog);
        activity.startActivity(intent);
    }

    /*
     * My Blog Activity
     * */
    public static void MyBlogActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, MyBlogActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Blog Viewer Activity
     * */
    public static void BlogViewerActivity(String parentActivity, Blog blog) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, BlogViewerActivity.class);
        intent.putExtra(Vars.ParentActivity.TRIG_BLOG_VIEWER_ACTIVITY, parentActivity);
        intent.putExtra(Vars.Connector.BLOG_VIEWER_ACTIVITY_DATA, blog);
        activity.startActivity(intent);
    }

    /*
     * Create New Assistant Activity
     * */
    public static void CreateNewAssistantActivity() {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, CreateNewAssistantActivity.class);
        activity.startActivity(intent);
    }

    /*
     * Create New Assistant Activity
     * */
    public static void HomeServiceRegisterActivity(Doctor doctor) {
        Activity activity = RefActivity.refACActivity.get();
        Intent intent = new Intent(activity, HomeServiceRegisterActivity.class);
        intent.putExtra(Vars.Connector.HOME_SERVICE_REGISTER_ACTIVITY_DATA, doctor);
        activity.startActivity(intent);
    }
}
