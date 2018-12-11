package app.doctor.dmcx.app.da.project.doctorapp.Utility;

import android.util.Patterns;

import java.util.List;

public class Validator {

    public static boolean empty(String value) {
        return value.equals("");
    }

    public static boolean validEmail(String value) {
        return !Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    public static boolean validPassword(String value) {
        return value.length() < 6;
    }

}
