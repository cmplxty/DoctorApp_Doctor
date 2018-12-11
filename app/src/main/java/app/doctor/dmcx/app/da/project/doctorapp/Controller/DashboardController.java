package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class DashboardController {

    public static void CountNewAppointments(final IAction action) {
        Vars.appFirebase.countNewAppointments(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else
                    action.onCompleteAction(null);
            }
        });
    }

    public static void CountNewHomeService(final IAction action) {
        Vars.appFirebase.countNewHomeServices(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else
                    action.onCompleteAction(null);
            }
        });
    }

    public static void CountNewMessages(final IAction action) {
        Vars.appFirebase.countNewMessage(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else
                    action.onCompleteAction(null);
            }
        });
    }

    public static void CountActiveAssistants(final IAction action) {
        Vars.appFirebase.countActiveAssistants(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else
                    action.onCompleteAction(null);
            }
        });
    }

}
