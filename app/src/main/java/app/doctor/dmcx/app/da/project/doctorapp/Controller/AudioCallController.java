package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
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

    public static void SaveCallHistory(String patientId, String status) {
        Vars.appFirebase.saveAudioCallHistory(patientId, status);
    }

    public static void RetriveAudioCallHistory(final IAction action) {
        Vars.appFirebase.loadAllPatients(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    List<Patient> patients = new ArrayList<>();
                    if (object != null) {
                        for (Object patient : (List<?>) object) {
                            if (patient != null) {
                                patients.add((Patient) patient);
                            }
                        }
                    }

                    Vars.appFirebase.retriveAudioCallHistory(patients, new ICallback() {
                        @Override
                        public void onCallback(boolean isSuccessful, Object object) {
                            action.onCompleteAction(object);
                        }
                    });
                }
            }
        });
    }

    public static void DeleteCurrentHistory(String historyId) {
        Vars.appFirebase.deleteCurrentHistory(historyId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful)
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.HistoryDeleted, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FailedToDelete, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
