package app.doctor.dmcx.app.da.project.doctorapp.Firebase;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Model.ACDevice;
import app.doctor.dmcx.app.da.project.doctorapp.Model.APRequest;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Blog;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.Model.HomeService;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Message;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.Model.MessageUser;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Prescription;
import app.doctor.dmcx.app.da.project.doctorapp.Model.PrescriptionPatient;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AppFirebase {

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private StorageReference mStorage;

    public AppFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mReference = FirebaseDatabase.getInstance().getReference();
        this.mStorage = FirebaseStorage.getInstance().getReference();
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
                    callback.onCallback(task.isSuccessful(), task.getException() == null ? null : Objects.requireNonNull(task.getException()).getMessage());
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
     * Set Token Id
     * */
    public void setTokenId() {
        if (getCurrentUser() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(AFModel.token_id, FirebaseInstanceId.getInstance().getToken());

            mReference.child(AFModel.database)
                    .child(AFModel.notification)
                    .child(AFModel.token)
                    .child(getCurrentUser().getUid())
                    .updateChildren(map);
        }
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
                        mReference.child(AFModel.database)
                                .child(AFModel.home_service)
                                .child(getCurrentUser().getUid())
                                .removeValue();

                        mReference.child(AFModel.database)
                                .child(AFModel.home_service)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                snapshot.child(getCurrentUser().getUid()).getRef().removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                         callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Load Home Service Requests
    * */
    public void loadHomeServiceRequests(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<HomeService> homeServices = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HomeService homeService = snapshot.getValue(HomeService.class);
                                if (homeService != null) {
                                    homeService.setPatient_id(snapshot.getKey());
                                    homeServices.add(homeService);
                                }
                            }

                            if (homeServices.size() > 0) {
                                callback.onCallback(true, homeServices);
                            } else {
                                callback.onCallback(false, null);
                            }
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Cancel Home Service Request
    * */
    public void cancelHomeServiceRequest(final String patientId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service)
                .child(getCurrentUser().getUid())
                .child(patientId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mReference.child(AFModel.database)
                                    .child(AFModel.home_service)
                                    .child(patientId)
                                    .child(getCurrentUser().getUid()).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            callback.onCallback(task.isSuccessful(), null);
                                        }
                                    });
                        } else {
                            callback.onCallback(false, null);
                        }
                    }
                });
    }

    /*
    * Setup Appointment Doctor
    * */
    public void setupAppointmentDoctor(Map<String, Object> map, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment_doctor)
                .child(getCurrentUser().getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Check Appointment Doctor
    * */
    public void checkAppointmentDoctor(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment_doctor)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild(getCurrentUser().getUid())) {
                                callback.onCallback(true, null);
                            } else {
                                callback.onCallback(false, null);
                            }
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onCallback(false, null);
                    }
                });
    }

    /*
    * Remove Appointment Doctor
    * */
    public void removeAppointmentDoctor(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment_doctor)
                .child(getCurrentUser().getUid())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mReference.child(AFModel.database)
                                .child(AFModel.appointment)
                                .child(getCurrentUser().getUid())
                                .removeValue();

                        mReference.child(AFModel.database)
                                .child(AFModel.appointment)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            if (snapshot.hasChild(getCurrentUser().getUid())) {
                                                snapshot.getRef().removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Load All Appointment Requests
    * */
    public void loadAllAppointmentRequests(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<APRequest> apRequests = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                APRequest apRequest = snapshot.getValue(APRequest.class);
                                if (apRequest != null) {
                                    apRequest.setPatient_id(snapshot.getKey());
                                    apRequests.add(apRequest);
                                }
                            }

                            if (apRequests.size() > 0)
                                callback.onCallback(true, apRequests);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Change Request Status Appointment
    * */
    public void changeRequestStatusAppointmentRequest(final String value, final String patientId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment)
                .child(getCurrentUser().getUid())
                .child(patientId)
                .child(AFModel.ap_variables.status)
                .setValue(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mReference.child(AFModel.database)
                                .child(AFModel.appointment)
                                .child(patientId)
                                .child(getCurrentUser().getUid())
                                .child(AFModel.ap_variables.status)
                                .setValue(value)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        callback.onCallback(task.isSuccessful(), null);
                                    }
                                });
                    }
                });
    }

    /*
    * Delete Appointment From Doctor
    * */
    public void deleteAppointmentFromDoctor(final String patientId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment)
                .child(getCurrentUser().getUid())
                .child(patientId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mReference.child(AFModel.database)
                                .child(AFModel.appointment)
                                .child(patientId)
                                .child(getCurrentUser().getUid())
                                .child(AFModel.ap_variables.status)
                                .setValue(AFModel.cancel);

                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Upload Profile Image
    * */
    public void uploadProfileImage(final Uri image, final ICallback callback) {
        final StorageReference uploadImage = mStorage.child(AFModel.profile_image).child(getCurrentUser().getUid());

        uploadImage.putFile(image)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            uploadImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    callback.onCallback(true, url);
                                }
                            });
                        } else {
                            callback.onCallback(false, null);
                        }
                    }
                });
    }

    /*
    * Update Patient Profile
    * */
    public void updatePatientProfile(Map<String, Object> map, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.doctor)
                .child(getCurrentUser().getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Create New Appointment
    * */
    public void countNewAppointments(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.appointment)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int count = 0;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                APRequest apRequest = snapshot.getValue(APRequest.class);
                                if (apRequest != null) {
                                    if (apRequest.getNotification_status() != null && apRequest.getNotification_status().equals(AFModel.not_viewed)) {
                                        count++;
                                    }
                                }
                            }

                            if (count > 0)
                                callback.onCallback(true, count);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Count New Home Services
    * */
    public void countNewHomeServices(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.home_service)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int count = 0;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HomeService homeService = snapshot.getValue(HomeService.class);
                                if (homeService != null) {
                                    if (homeService.getNotification_status() != null && homeService.getNotification_status().equals(AFModel.not_viewed)) {
                                        count++;
                                    }
                                }
                            }

                            if (count > 0)
                                callback.onCallback(true, count);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Count New Message
    * */
    public void countNewMessage(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.message_user)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int count = 0;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                MessageUser messageUser = snapshot.getValue(MessageUser.class);
                                if (messageUser != null) {
                                    if (messageUser.getNotification_status() != null && messageUser.getNotification_status().equals(AFModel.not_viewed)) {
                                        count++;
                                    }
                                }
                            }

                            if (count > 0)
                                callback.onCallback(true, count);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Update Not Viewed To Viewed
    * */
    public void updateNotViewedToViewed(String property) {
        mReference.child(AFModel.database)
                .child(property)
                .child(getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.hasChild(AFModel.notification_status)) {
                                snapshot.child(AFModel.notification_status).getRef().setValue(AFModel.viewed);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Set Audio Call Doctor
    * */
    public void setAudioCallDoctor(String status, final ICallback callback) {
        Map<String, Object> map = new HashMap<>();
        map.put(AFModel.audio_call_status, status);

        mReference.child(AFModel.database)
                .child(AFModel.audio_call_doctor)
                .child(getCurrentUser().getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onCallback(task.isSuccessful(), null);
                    }
                });
    }

    /*
    * Check Audio Call Status
    * */
    public void checkAudioCallStatus(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.audio_call_doctor)
                .child(getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String status = dataSnapshot.child(AFModel.audio_call_status).getValue(String.class);
                            callback.onCallback(true, status);
                        } else
                            callback.onCallback(false, AFModel.offline);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Load Specific Patient
    * */
    public void loadSpecificPatient(String patientId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.patient)
                .child(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            callback.onCallback(true, dataSnapshot.getValue(Patient.class));
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Load Last Blog Content
    * */
    public void loadLastBlog(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blog)
                .orderByChild(AFModel.timestamp)
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Blog blog = dataSnapshot.getValue(Blog.class);
                            if (blog != null) {
                                blog.setId(dataSnapshot.getKey());
                                callback.onCallback(true, blog);
                            } else {
                                callback.onCallback(false, null);
                            }
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    /*
     * Load All Blog Content
     * */
    public void loadAllBlogs(final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.blog)
                .orderByChild(AFModel.timestamp)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Blog> blogs = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Blog blog = snapshot.getValue(Blog.class);
                                if (blog != null) {
                                    blog.setId(snapshot.getKey());
                                    blogs.add(blog);
                                }
                            }

                            if (blogs.size() > 0)
                                callback.onCallback(true, blogs);
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    * Check Audio Call Device Id
    * */
    public void checkAudioCallDeviiceId(String userId, final ICallback callback) {
        mReference.child(AFModel.database)
                .child(AFModel.audio_call_device)
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ACDevice acDevice = dataSnapshot.getValue(ACDevice.class);
                            if (acDevice != null)
                                callback.onCallback(true, acDevice.getDevice_id());
                            else
                                callback.onCallback(false, null);
                        } else {
                            callback.onCallback(false, null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
