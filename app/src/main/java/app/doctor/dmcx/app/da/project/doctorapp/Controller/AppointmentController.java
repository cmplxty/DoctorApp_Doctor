package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Model.APDoctor;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AppointmentController {

    public static void CheckAppointmentDoctor(final IAction action) {
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.checkAppointmentDoctor(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();
                action.onCompleteAction(isSuccessful);
            }
        });
    }

    public static void SetupAppointmentDoctor(String passcode, APDoctor apDoctor, final IAction action) {
        Map<String, Object> map = new HashMap<>();
        map.put(AFModel.name, apDoctor.getName());
        map.put(AFModel.phone, apDoctor.getPhone());
        map.put(AFModel.clinic, apDoctor.getClinic());
        map.put(AFModel.specialist, apDoctor.getSpecialist());
        map.put(AFModel.appointments, apDoctor.getAppointments());
        map.put(AFModel.passcode, passcode);

        Vars.appFirebase.setupAppointmentDoctor(map, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.ApptSetupSuccess, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.SetupFailed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void RemoveAppointmentDoctor() {
        Vars.appFirebase.removeAppointmentDoctor(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.Removed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RemoveFailed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void LoadAllAppointmentRequests(final IAction action) {
        Vars.appFirebase.loadAllAppointmentRequests(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);

                if (!isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorNoDataFound, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void CancelAppointmentRequest(String patientId, final IAction action) {
        Vars.appFirebase.changeRequestStatusAppointmentRequest(AFModel.cancel, patientId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);

                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RequestCanceled, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void AcceptAppointmentRequest(String patientId, final IAction action) {
        Vars.appFirebase.changeRequestStatusAppointmentRequest(AFModel.accept, patientId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(isSuccessful);

                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RequestAccepted, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void DeleteAppointmentRequest(String patientId, final IAction action) {
        Vars.appFirebase.deleteAppointmentFromDoctor(patientId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AppointmentDeleted, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
