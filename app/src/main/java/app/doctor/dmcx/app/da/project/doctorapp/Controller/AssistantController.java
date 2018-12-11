package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.support.design.widget.Snackbar;
import android.widget.Toast;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Assistant;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AssistantController {

    public static void LoadAllAssistants(final IAction action) {
        Vars.appFirebase.loadAllAssistants(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                action.onCompleteAction(object);
            }
        });
    }

    public static void CreateNewAssistant(String aEmail, String aPass, final String dEmail, final String dPass, Assistant assistant, final IAction action) {
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.createNewAssistant(aEmail, aPass, dEmail, dPass, assistant, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                if (!isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.CreateAssistantSignInFailed, Toast.LENGTH_SHORT).show();
                }
            }
        }, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                action.onCompleteAction(isSuccessful);

                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AccountCreated, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FailedToCreateNewAccout, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void SendNoteToAssistant(String assistantId, String note) {
        Vars.appFirebase.sendNoteToAssistant(assistantId, note, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.NoteSent, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FailedToSent, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void DeleteAssistant(String assistantId) {
        Vars.appFirebase.deleteAssistant(assistantId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful)
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AssistantDeleted, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
