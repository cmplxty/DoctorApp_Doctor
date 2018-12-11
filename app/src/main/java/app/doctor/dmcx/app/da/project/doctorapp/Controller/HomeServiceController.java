package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.util.ArrayMap;
import android.widget.Toast;

import java.util.Map;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Model.HomeService;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class HomeServiceController {

    public static void CheckForRegisteredHomeServiceDoctor(final IAction action) {
        LoadingDialog.start(LoadingText.PleaseWait);
        Vars.appFirebase.checkHomeServiceUser(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();
                action.onCompleteAction(isSuccessful);
            }
        });
    }

    public static void RegisterHomeService(HomeService homeService) {
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.registerHomeService(homeService, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RegistrationSuccess, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RegistrationFailed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void UnregisterHomeService() {
        Vars.appFirebase.unregisterHomeService(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (object instanceof Boolean)
                    if ((Boolean) object) {
                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.Unregistered, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.UnregisterFailed, Toast.LENGTH_SHORT).show();
                    }
                else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorUnknownReturnType, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void LoadHomeServiceRequests(final IAction action) {
        Vars.appFirebase.loadHomeServiceRequests(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else {
                    action.onCompleteAction(null);
                }
            }
        });
    }

    public static void CancelHomeServiceRequest(String patientId) {
        Vars.appFirebase.cancelHomeServiceRequest(patientId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RequestCanceled, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.RequestCancelFailed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void UpdateNotViewedToViewedHomeService() {
        Vars.appFirebase.updateNotViewedToViewed(AFModel.home_service);
    }
}
