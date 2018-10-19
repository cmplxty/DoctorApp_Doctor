package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.widget.Toast;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.Validator;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AuthController {

    public static void SignIn(final String email, final String password, final IAction action) {
        if (Validator.empty(email)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.EmptyEmail, Toast.LENGTH_SHORT).show();
            return;
        } else if (Validator.validEmail(email)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.ValidEmail, Toast.LENGTH_SHORT).show();
            return;
        } else if (Validator.empty(password)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.EmptyPassword, Toast.LENGTH_SHORT).show();
            return;
        }

        // Loading
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.getUserByEmail(email, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Vars.appFirebase.signIn(email, password, new ICallback() {
                        @Override
                        public void onCallback(boolean isSuccessful, Object object) {
                            // Loading Stop
                            LoadingDialog.stop();

                            if (!isSuccessful) {
                                String errorCode = (String) object;
                                action.onCompleteAction(errorCode);
                            } else {
                                action.onCompleteAction(true);
                            }
                        }
                    });
                } else {
                    // Loading Stop
                    LoadingDialog.stop();

                    action.onCompleteAction(ValidationText.UserNotExists);
                }
            }
        });
    }

    public static void SignOut() {
        Vars.appFirebase.signOut();
    }

    public static boolean isUserLoggedIn() {
        return Vars.appFirebase.getCurrentUser() != null;
    }
}
