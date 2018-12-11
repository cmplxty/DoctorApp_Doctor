package app.doctor.dmcx.app.da.project.doctorapp.Seed;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;

public class Seeder {
    private static Seeder instance;

    private char[] seed = {'1', '2', '3', '4', '5'};

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    public static Seeder Instance() {
        if (instance == null)
            instance = new Seeder();

        return instance;
    }

    private FirebaseUser getCurrentUser() {
        if (mAuth == null)
            mAuth = FirebaseAuth.getInstance();

        return mAuth.getCurrentUser();
    }

    public Seeder create() {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        return instance;
    }

    public Seeder showToast() {
        Toast.makeText(RefActivity.refACActivity.get(), "Seeding...", Toast.LENGTH_SHORT).show();
        return instance;
    }

    public void seedAppointment() {
        if (mReference != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(AFModel.ap_variables.patient_name, "Mr. Kamal Raj");
            map.put(AFModel.ap_variables.patient_phone, "0192039122");
            map.put(AFModel.ap_variables.date, "24 NOV 2018");
            map.put(AFModel.ap_variables.time, "3 PM - 6 PM");
            map.put(AFModel.ap_variables.status, AFModel.accept);
            map.put(AFModel.ap_variables.doctor_name, "Doctor One");
            map.put(AFModel.ap_variables.doctor_clinic, "Some Chember");
            map.put(AFModel.ap_variables.timestamp, String.valueOf(System.currentTimeMillis()));
            map.put(AFModel.notification_status, AFModel.not_viewed);

            for (char value : seed) {
                mReference.child(AFModel.database)
                        .child(AFModel.appointment)
                        .child(getCurrentUser().getUid())
                        .child(String.valueOf(value))
                        .updateChildren(map);
            }
        }
    }

}
