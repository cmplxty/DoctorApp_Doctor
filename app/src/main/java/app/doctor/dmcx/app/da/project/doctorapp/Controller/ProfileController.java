package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import com.google.gson.Gson;

import app.doctor.dmcx.app.da.project.doctorapp.Firebase.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LDBModel;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class ProfileController {

    public static void LoadProfile(final IAction action) {
        // Loading
        LoadingDialog.start(LoadingText.RetrivingProfile);

        Vars.appFirebase.getUserProfileData(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                // Loading Stop
                LoadingDialog.stop();

                if (!isSuccessful) {
                    if (object instanceof String) {
                        action.onCompleteAction(object);
                    }
                } else {
                    if (object instanceof Doctor) {
                        Doctor doctor = (Doctor) object;
                        Gson gson = new Gson();
                        String doctorJson = gson.toJson(doctor);

                        Vars.localDB.saveString(LDBModel.SAVE_DOCTOR_PROFILE, doctorJson);
                        action.onCompleteAction(doctor);
                    }
                }
            }
        });
    }

    public static void LoadLocalProfile(IAction action) {
        String doctorJson = Vars.localDB.retriveString(LDBModel.SAVE_DOCTOR_PROFILE);
        Gson gson = new Gson();
        Doctor doctor = gson.fromJson(doctorJson, Doctor.class);

        action.onCompleteAction(doctor);
    }

    public static Doctor GetLocalProfile() {
        String doctorJson = Vars.localDB.retriveString(LDBModel.SAVE_DOCTOR_PROFILE);
        Gson gson = new Gson();
        return gson.fromJson(doctorJson, Doctor.class);
    }

    public static void CheckForProfileData(IAction action) {
        if (!Vars.localDB.retriveString(LDBModel.SAVE_DOCTOR_PROFILE).equals("")) {
            LoadLocalProfile(action);
            return;
        }

        LoadProfile(action);
    }

}
