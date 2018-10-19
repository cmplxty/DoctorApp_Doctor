package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import android.widget.Toast;
import android.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.ICallback;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Prescription;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.LoadingText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class PrescriptionController {

    public static void SendPrescription(final Prescription prescription) {
        Map<String, Object> prescriptionMap = new HashMap<>();
        prescriptionMap.put(AFModel.prescription_variables.doctor_id, prescription.getDoctor_id());
        prescriptionMap.put(AFModel.prescription_variables.patient_id, prescription.getPatient_id());
        prescriptionMap.put(AFModel.prescription_variables.doctor_name, prescription.getDoctor_name());
        prescriptionMap.put(AFModel.prescription_variables.patient_name, prescription.getPatient_name());
        prescriptionMap.put(AFModel.prescription_variables.patient_phone, prescription.getPatient_phone());
        prescriptionMap.put(AFModel.prescription_variables.patient_age, prescription.getPatient_age());
        prescriptionMap.put(AFModel.prescription_variables.patient_address, prescription.getPatient_address());
        prescriptionMap.put(AFModel.prescription_variables.date, prescription.getDate());
        prescriptionMap.put(AFModel.prescription_variables.medicines, prescription.getMedicines());
        prescriptionMap.put(AFModel.prescription_variables.timestamp, prescription.getTimestamp());

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(prescription.getDoctor_id() + "/" + prescription.getPatient_id() + "/" + Vars.appFirebase.getPushId(), prescriptionMap);
        updateMap.put(prescription.getPatient_id() + "/" + Vars.appFirebase.getPushId(), prescriptionMap);

        Vars.appFirebase.savePrescription(updateMap, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (object instanceof String)
                    Toast.makeText(RefActivity.refACActivity.get(), String.valueOf(object), Toast.LENGTH_SHORT).show();
                if (isSuccessful) {
                    MessageController.SendMessagePrescription(prescription.getPatient_id(), prescription.getTimestamp());
                }
            }
        });
    }

    public static void LoadSinglePrescription(String patientId, String timestamp, final IAction action) {
        LoadingDialog.start(LoadingText.PleaseWait);

        Vars.appFirebase.loadSpecificPrescription(patientId, timestamp, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                LoadingDialog.stop();

                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else {
                    if (object instanceof String)
                        Toast.makeText(RefActivity.refACActivity.get(), String.valueOf(object), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void LoadAllPrescriptionPatients(final IAction action) {
        Vars.appFirebase.loadPrescriptionPatients(new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (object instanceof String) {
                    action.onCompleteAction(null);
                    Toast.makeText(RefActivity.refACActivity.get(), String.valueOf(object), Toast.LENGTH_SHORT).show();
                } else {
                    action.onCompleteAction(object);
                }
            }
        });
    }

    public static void LoadSinglePatientPrescriptions(String patientId, final IAction action) {
        Vars.appFirebase.loadSpecificPatientPrescriptions(patientId, new ICallback() {
            @Override
            public void onCallback(boolean isSuccessful, Object object) {
                if (isSuccessful) {
                    action.onCompleteAction(object);
                } else {
                    if (object instanceof String)
                        Toast.makeText(RefActivity.refACActivity.get(), String.valueOf(object), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
