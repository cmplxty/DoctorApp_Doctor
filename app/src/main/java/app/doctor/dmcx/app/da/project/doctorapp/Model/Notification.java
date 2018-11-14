package app.doctor.dmcx.app.da.project.doctorapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {

    private String doctor_id;
    private String patient_id;
    private String timestamp;

    protected Notification(Parcel in) {
        doctor_id = in.readString();
        patient_id = in.readString();
        timestamp = in.readString();
    }

    public Notification() {
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(doctor_id);
        parcel.writeString(patient_id);
        parcel.writeString(timestamp);
    }
}
