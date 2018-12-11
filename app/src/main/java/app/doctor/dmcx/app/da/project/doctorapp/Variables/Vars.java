package app.doctor.dmcx.app.da.project.doctorapp.Variables;

import android.support.v4.app.Fragment;

import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AppFirebase;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LocalDB;

public class Vars {

    public static final String TAG = "APPTAGLLL";
    public static Fragment currentFragment;
    public static AppFirebase appFirebase;
    public static LocalDB localDB;

    public static final String app_version = "1.0.0";

    public static class BNavBarProps {
        public static final int bottomNavBarTextSize = 10;
    }

    public static class Connector {
        public static final String MESSAGE_ACTIVITY_DATA                = "M A D";
        public static final String VIEW_IMAGE_DATA                      = "V I D";
        public static final String PERSCRIPTION_ACTIVITY_DATA           = "P A D";
        public static final String PROFILE_EDIT_FRAGMENT_DATA           = "P E F D";
        public static final String AUDIO_CALL_ACTIVITY_DATA             = "A C A D";
        public static final String BLOG_EDITOR_ACTIVITY_DATA            = "B E A D";
        public static final String BLOG_VIEWER_ACTIVITY_DATA            = "B V A D";
        public static final String PRESCRIPTION_LIST_FRAGMENT_DATA      = "P L F D";
        public static final String HOME_SERVICE_REGISTER_ACTIVITY_DATA  = "H S R D";
    }

    public static class ParentActivity {
        public static final String TRIG_BLOG_EDITOR_ACTIVITY = "TRIG BLOG EDITOR ACTIVITY";
        public static final String TRIG_BLOG_VIEWER_ACTIVITY = "TRIG BLOG VIEWER ACTIVITY";

        public static final String HOME_ACTIVITY = "HOME ACTIVITY";
        public static final String MY_BLOG_ACTIVITY = "MY BLOG ACTIVITY";
    }

    public static class RequestCode {
        public static final int REQUEST_CALL_CODE_HS = 1111;
        public static final int REQUEST_CALL_CODE_AP = 1112;
        public static final int REQUEST_ACCESS_IMAGE_CODE_PEF = 1113;
        public static final int REQUEST_ACCESS_IMAGE_CODE_BEA = 1114;
        public static final int REQUEST_FOR_ONLINE_AUDIO_CALL = 1115;
    }

    public static class Notification {
        public static final int AUDIO_CALL = 8881;
    }

    public static class Sinch {
        public static final String APP_KEY = "76a8749a-b68e-4196-b382-84de8eb133fc";
        public static final String APP_SECRET = "eWBstQ1CoUCYdGQbjHaYwA==";
        public static final String ENVIRONMENT = "clientapi.sinch.com";
    }
}
