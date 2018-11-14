package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.provider.Settings;
import android.widget.Toast;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AudioCallController {

    public static void CheckAudioCallStatus(final IAction action) {
        Vars.appFirebase.checkAudioCallStatus(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void SetAudioCallDoctor(String status) {
        Vars.appFirebase.setAudioCallDoctor(status, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.StatusChanged, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void GetAudioCallPatientData(String patientId, final IAction action) {
        Vars.appFirebase.loadSpecificPatient(patientId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void CheckAudioCallDeviceId(String userId, final IAction action) {
        Vars.appFirebase.checkAudioCallDeviiceId(userId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

}
