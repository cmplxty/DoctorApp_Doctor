package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Message;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class MessageController {

    public static void LoadMessgeUsersList(final IAction action) {
        Vars.appFirebase.loadMessageUsers(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    if (object != null) {
                        action.onCompleteAction(object);
                    } else {
                        action.onCompleteAction(ErrorText.NoUserFound);
                    }
                } else {
                    action.onCompleteAction(ErrorText.NoUserFound);
                }
            }
        });
    }

    public static void LoadMessageUserContent(String fromId, final IAction action) {
        Vars.appFirebase.loadMessageUserDetail(fromId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    Patient patient = (Patient) object;
                    action.onCompleteAction(patient);
                } else {
                    action.onCompleteAction(ErrorText.ErrorUserNotExists);
                }
            }
        });
    }

    public static void LoadUserMessages(String fromId, final IAction action) {
        Vars.appFirebase.loadUserMessages(fromId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    List<Message> messages = new ArrayList<>();
                    for (Object obj : (List<?>) object) {
                        messages.add((Message) obj);
                    }

                    if (messages.size() > 0) {
                        action.onCompleteAction(object);
                    } else {
                        action.onCompleteAction(ErrorText.ErrorNoMessagesFound);
                    }
                } else {
                    action.onCompleteAction(ErrorText.ErrorNoMessagesFound);
                }
            }
        });
    }

    public static void SendMessageText(String message, String pToUserId) {

        String dFromUserId = Vars.appFirebase.getCurrentUser().getUid();

        Map<String, String> map = new HashMap<>();
        map.put(AFModel.content, message);
        map.put(AFModel.type, AFModel.text);
        map.put(AFModel.from, dFromUserId);
        map.put(AFModel.to, pToUserId);
        map.put(AFModel.timestamp, String.valueOf(System.currentTimeMillis()));

        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put(dFromUserId + "/" + Vars.appFirebase.getPushId(), map);
        mainMap.put(pToUserId + "/" + Vars.appFirebase.getPushId(), map);

        // Hold the last message - Update for one user
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put(AFModel.content, message);
        userMessage.put(AFModel.type, AFModel.text);
        userMessage.put(AFModel.doctor, dFromUserId);
        userMessage.put(AFModel.patient, pToUserId);
        userMessage.put(AFModel.timestamp, String.valueOf(System.currentTimeMillis()));

        Map<String, Object> userMessageMap = new HashMap<>();
        userMessage.put(AFModel.notification_status, AFModel.viewed);
        userMessageMap.put(dFromUserId + "/" + pToUserId, userMessage);
        userMessage.put(AFModel.notification_status, AFModel.not_viewed);
        userMessageMap.put(pToUserId + "/" + dFromUserId, userMessage);

        Vars.appFirebase.saveMessage(mainMap, userMessageMap, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (object instanceof String) {
                    Toast.makeText(RefActivity.refACActivity.get(), String.valueOf(object), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorUnknownReturnType, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void SendMessagePrescription(String pToUserId, String prescribeTimestamp) {

        String dFromUserId = Vars.appFirebase.getCurrentUser().getUid();

        Map<String, String> map = new HashMap<>();
        map.put(AFModel.content, prescribeTimestamp);
        map.put(AFModel.type, AFModel.prescription);
        map.put(AFModel.from, dFromUserId);
        map.put(AFModel.to, pToUserId);
        map.put(AFModel.timestamp, String.valueOf(System.currentTimeMillis()));

        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put(dFromUserId + "/" + Vars.appFirebase.getPushId(), map);
        mainMap.put(pToUserId + "/" + Vars.appFirebase.getPushId(), map);

        // Hold the last message - Update for one user
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put(AFModel.content, prescribeTimestamp);
        userMessage.put(AFModel.type, AFModel.prescription);
        userMessage.put(AFModel.doctor, dFromUserId);
        userMessage.put(AFModel.patient, pToUserId);
        userMessage.put(AFModel.timestamp, String.valueOf(System.currentTimeMillis()));

        Map<String, Object> userMessageMap = new HashMap<>();
        userMessage.put(AFModel.notification_status, AFModel.viewed);
        userMessageMap.put(dFromUserId + "/" + pToUserId, userMessage);
        userMessage.put(AFModel.notification_status, AFModel.not_viewed);
        userMessageMap.put(pToUserId + "/" + dFromUserId, userMessage);

        Vars.appFirebase.saveMessage(mainMap, userMessageMap, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (object instanceof String) {
                    Toast.makeText(RefActivity.refACActivity.get(), String.valueOf(object), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.ErrorUnknownReturnType, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void UpdateNotViewedToViewedMessage() {
        Vars.appFirebase.updateNotViewedToViewed(AFModel.message_user);
    }
}
