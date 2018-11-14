package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.app.AlertDialog;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.INavHeader;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LDBModel;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class ProfileController {

    private static void LoadProfile(final IAction action) {
        INavHeader iNavHeader = null;
        if (RefActivity.refACActivity.get() instanceof HomeActivity) {
            iNavHeader = (HomeActivity) RefActivity.refACActivity.get();
        }

        final INavHeader finalINavHeader = iNavHeader;

        final AlertDialog alertDialog = LoadingDialog.on(LoadingText.RetrivingProfile);
        Vars.appFirebase.getUserProfileData(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.off(alertDialog);

                if (!isSuccessful) {
                    if (object instanceof String) {
                        action.onCompleteAction(object);
                    }
                } else {
                    if (object instanceof Doctor) {
                        Doctor doctor = (Doctor) object;
                        Gson gson = new Gson();
                        String doctorJson = gson.toJson(doctor);

                        if (finalINavHeader != null) {
                            finalINavHeader.onNavHeaderUpdate(doctor);
                        }

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

    public static void UpdateProfileDetail(Doctor doctor, Uri profileImageUri) {
        final Map<String, Object> map = new HashMap<>();
        map.put(AFModel.about, doctor.getAbout().equals("") ? AFModel.deflt : doctor.getAbout());
        map.put(AFModel.chamber, doctor.getChamber().equals("") ? AFModel.deflt : doctor.getChamber());
        map.put(AFModel.city, doctor.getCity().equals("") ? AFModel.deflt : doctor.getCity());
        map.put(AFModel.country, doctor.getCountry().equals("") ? AFModel.deflt : doctor.getCountry());
        map.put(AFModel.degree, doctor.getDegree().equals("") ? AFModel.deflt : doctor.getDegree());
        map.put(AFModel.email, doctor.getEmail().equals("") ? AFModel.deflt : doctor.getEmail());
        map.put(AFModel.hospital, doctor.getHospital().equals("") ? AFModel.deflt : doctor.getHospital());
        map.put(AFModel.image_link, doctor.getImage_link().equals("") ? AFModel.deflt : doctor.getImage_link());
        map.put(AFModel.name, doctor.getName().equals("") ? AFModel.deflt : doctor.getName());
        map.put(AFModel.phone, doctor.getPhone().equals("") ? AFModel.deflt : doctor.getPhone());
        map.put(AFModel.registration, doctor.getRegistration().equals("") ? AFModel.deflt : doctor.getRegistration());
        map.put(AFModel.specialist, doctor.getSpecialist().equals("") ? AFModel.deflt : doctor.getSpecialist());

        if (profileImageUri != null) {
            final AlertDialog alertDialog = LoadingDialog.on(LoadingText.UploadingImage);
            Vars.appFirebase.uploadProfileImage(profileImageUri, new ICallback() {
                @Override
                public void onCallback(boolean isSuccessful, Object object) {
                    LoadingDialog.off(alertDialog);

                    if (isSuccessful) {
                        String url = (String) object;
                        map.put(AFModel.image_link, url);
                    }

                    UpdateUserDetail(map);
                }
            });
        } else {
            UpdateUserDetail(map);
        }
    }

    private static void UpdateUserDetail(Map<String, Object> map) {
        final AlertDialog alertDialog = LoadingDialog.on(LoadingText.PleaseWait);
        Vars.appFirebase.updatePatientProfile(map, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.off(alertDialog);

                if (isSuccessful) {
                    LoadProfile(new IAction() {
                        @Override
                        public void onCompleteAction(Object object) {
                            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.UpdateSuccessful, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
