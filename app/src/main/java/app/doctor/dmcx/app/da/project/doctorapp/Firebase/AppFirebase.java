package app.doctor.dmcx.app.da.project.doctorapp.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Message;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.Model.MessageUser;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Prescription;
import app.doctor.dmcx.app.da.project.doctorapp.Model.PrescriptionPatient;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;

public class AppFirebase {

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    public AppFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mReference = FirebaseDatabase.getInstance().getReference();
    }

    // Developer Private
    private void callOnErrorOccured(String error) {
        Toast.makeText(RefActivity.refACActivity.get(), error, Toast.LENGTH_SHORT).show();
    }
    // Developer Private

    /*
    * Get Current User
    * */
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    /*
     * Get Push Id
     * */
    public String getPushId() {
        return mReference.push().getKey();
    }

    /*
    * Sign In
    * */
    public void signIn(String email, String password, final ICallback callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    callback.onCallback(task.isSuccessful(),
                            task.getException() == null ? null : Objects.requireNonNull(task.getException()).getMessage());
                } catch (Exception ex) {
                    // Exception Callback
                    callOnErrorOccured(ErrorText.ErrorServiceBlocked);
                }
            }
        });
    }

    /*
    * Sign Out
    * */
    public void signOut() {
        mAuth.signOut();
    }

    /*
    * Get User Info
    * */
    public void getCurrentUserInfo(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.doctor)
                .child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        callback.onCallback(dataSnapshot.exists(), dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onCallback(false, databaseError.getMessage());
                    }
                });
    }

    /*
    * Get User Info by Email of (Doctor)
    * */
    public void getUserByEmail(final String email, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.doctor)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("SDASDD", "onDataChange: " + dataSnapshot.toString());

                        boolean isEmailExists = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Doctor doctor = snapshot.getValue(Doctor.class);
                            if (doctor != null) {
                                isEmailExists = doctor.getEmail().equals(email);
                                if (isEmailExists) {
                                    break;
                                }
                            }
                        }

                        callback.onCallback(isEmailExists, dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Get Doctor profile data
    * */
    public void getUserProfileData(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.doctor)
                .child(getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Doctor doctor = dataSnapshot.getValue(Doctor.class);
                            if (doctor != null) {
                                doctor.setId(dataSnapshot.getKey());
                                callback.onCallback(true, doctor);
                            } else {
                                callback.onCallback(false, ErrorText.ErrorUserNotExists);
                            }
                        } else {
                            callback.onCallback(false, ErrorText.ErrorUserNotExists);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Users who messeages doctor
    * */
    public void loadMessageUsers(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.message_user)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<MessageUser> messageUsers = new ArrayList<>();
                        boolean isDataFound = false;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MessageUser messageUser = snapshot.getValue(MessageUser.class);
                                if (messageUser != null) {
                                    if (!isDataFound) isDataFound = true;
                                    messageUsers.add(messageUser);
                                }
                            }
                        }

                        callback.onCallback(isDataFound, messageUsers);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * User detail who messaeg doctor
    * */
    public void loadMessageUserDetail(final String userId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.patient)
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Patient patient = null;
                        boolean isDataFound = true;
                        if (dataSnapshot.exists()){
                            patient = dataSnapshot.getValue(Patient.class);

                            assert patient != null;
                            patient.setId(userId);
                        } else {
                            isDataFound = false;
                        }

                        callback.onCallback(isDataFound, patient);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Messages of a user (Patient)
    * */
    public void loadUserMessages(final String userId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.message)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Message> messages = new ArrayList<>();
                        boolean isDataFound = false;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Message message = snapshot.getValue(Message.class);
                                if (message != null) {
                                    if (message.getFrom().equals(userId) && message.getTo().equals(getCurrentUser().getUid())
                                        || message.getTo().equals(userId) && message.getFrom().equals(getCurrentUser().getUid())) {

                                        if (!isDataFound) isDataFound = true;
                                        messages.add(message);
                                    }
                                }
                            }
                        }

                        callback.onCallback(isDataFound, messages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Save MessageUserList to Firebase
    * */
    public void saveMessage(Map<String, Object> value, final Map<String, Object> userMessage, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.message)
                .updateChildren(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mReference.child(AFModel.database)
                                .child(AFModel.message_user)
                                .updateChildren(userMessage);

                        callback.onCallback(task.isSuccessful(), task.isSuccessful() ? ValidationText.MessageSent : ValidationText.MessageNotSent);
                    }
                });
    }

    /*
    * Save Prescription
    * */
    public void savePrescription(Map<String, Object> value, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.prescription)
                .updateChildren(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onCallback(true, ValidationText.PrescriptionSent);
                        } else {
                            callback.onCallback(false, ErrorText.ErrorPrescriptionNotSent);
                        }
                    }
                });
    }

    /*
    * Load Specific Prescription
    * */
    public void loadSpecificPrescription(final String patientId, final String timestamp, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.prescription)
                .child(getCurrentUser().getUid())
                .child(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isMatchFound = false;
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Prescription prescription = snapshot.getValue(Prescription.class);
                                if (prescription != null) {
                                    if (prescription.getTimestamp().equals(timestamp)) {
                                        isMatchFound = true;
                                        callback.onCallback(true, prescription);
                                    }
                                }
                            }

                            if (!isMatchFound)
                                callback.onCallback(false, ErrorText.ErrorNoPrescriptionFound);
                        } else {
                            callback.onCallback(false, ErrorText.ErrorNoDataFound);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    /*
     * Load All Prescriptions
     * */
    public void loadPrescriptions(String patinetId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.prescription)
                .child(getCurrentUser().getUid())
                .child(patinetId)
                .orderByChild(AFModel.timestamp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ArrayList<Prescription> prescriptions = new ArrayList<>();
                            boolean isPrescriptionsFound = false;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Prescription prescription = snapshot.getValue(Prescription.class);
                                if (prescription != null) {
                                    if (!isPrescriptionsFound) isPrescriptionsFound = true;
                                    prescriptions.add(prescription);
                                }
                            }

                            Collections.sort(prescriptions, new Comparator<Prescription>() {
                                @Override
                                public int compare(Prescription p1, Prescription p2) {
                                    return Long.valueOf(p2.getTimestamp()).compareTo(Long.valueOf(p1.getTimestamp()));
                                }
                            });
                            callback.onCallback(isPrescriptionsFound, prescriptions);
                        } else {
                            callback.onCallback(false, ErrorText.ErrorNoPrescriptionFound);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Load Prescription Patient
    * -- Actually load all patient whom the doctor prescribe
    * */
    public void loadPrescriptionPatients(final ICallback callback) {

        mReference.child(AFModel.database)
                .child(AFModel.prescription)
                .child(getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<PrescriptionPatient> patientNames = new ArrayList<>();
                            boolean isDataFound = false;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        Prescription prescription = childSnapshot.getValue(Prescription.class);
                                        if (prescription != null) {
                                            if (!isDataFound) isDataFound = true;
                                            patientNames.add(new PrescriptionPatient(prescription.getPatient_id(), prescription.getPatient_name()));
                                            break;
                                        }
                                    }
                                }
                            }

                            if (isDataFound)
                                callback.onCallback(true, patientNames);
                            else
                                callback.onCallback(false, ErrorText.ErrorNoDataFound);
                        } else {
                            callback.onCallback(false, ErrorText.ErrorNoDataFound);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    /*
     * Load Prescription
     * */
    public void loadSpecificPatientPrescriptions(final String patientId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.prescription)
                .child(getCurrentUser().getUid())
                .child(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Prescription> prescriptions = new ArrayList<>();
                        boolean isDataFound = false;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Prescription prescription = snapshot.getValue(Prescription.class);
                                if (prescription != null) {
                                    if (!isDataFound) isDataFound = true;
                                    prescriptions.add(prescription);
                                }
                            }

                            if (!isDataFound)
                                callback.onCallback(false, ErrorText.ErrorNoPrescriptionFound);
                            else
                                callback.onCallback(true, prescriptions);
                        } else {
                            callback.onCallback(false, ErrorText.ErrorNoDataFound);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Check Registered Home Service Doctor
    * */

    public void checkHomeServiceUser(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service_doctor)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        callback.onCallback(dataSnapshot.hasChild(getCurrentUser().getUid()), null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Register Home Service
    * */
    public void registerHomeService(Map<String, Object> value, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service_doctor)
                .child(getCurrentUser().getUid())
                .setValue(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Unregister Home Service
    * */
    public void unregisterHomeService(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service_doctor)
                .child(getCurrentUser().getUid())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                         callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }
}
