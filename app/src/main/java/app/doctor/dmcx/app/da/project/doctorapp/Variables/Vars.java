package app.doctor.dmcx.app.da.project.doctorapp.Variables;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;

import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AppFirebase;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LocalDB;

public class Vars {

    public static Fragment currentFragment;
    public static AppFirebase appFirebase;
    public static LocalDB localDB;

    public static final String app_version = "1.0.0";

    public static class BNavBarProps {
        public static final int bottomNavBarTextSize = 10;
    }

    public static class Connector {
        public static final String MESSAGE_ACTIVITY_DATA = "M A D";
        public static final String VIEW_IMAGE_DATA = "V I D";
        public static final String PERSCRIPTION_ACTIVITY_DATA = "P A D";
    }

    public class FragmentConnector {
        public static final String PRESCRIPTION_LIST_FRAGMENT_DATA = "P L F D";
    }

    public class RequestCode {
        public static final int REQUEST_CALL_CODE_HS = 1111;
    }
}
