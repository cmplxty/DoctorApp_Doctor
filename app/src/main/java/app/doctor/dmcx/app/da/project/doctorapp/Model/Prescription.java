package app.doctor.dmcx.app.da.project.doctorapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Prescription implements Parcelable {

    private String doctor_id;
    private String patient_id;
    private String doctor_name;
    private String patient_name;
    private String patient_phone;
    private String patient_address;
    private String patient_age;
    private String medicines;
    private String date;
    private String timestamp;

    public Prescription() {
    }

    public Prescription(String doctor_id, String patient_id, String doctor_name, String patient_name, String patient_phone, String patient_address, String patient_age, String medicines, String date, String timestamp) {
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.doctor_name = doctor_name;
        this.patient_name = patient_name;
        this.patient_phone = patient_phone;
        this.patient_address = patient_address;
        this.patient_age = patient_age;
        this.medicines = medicines;
        this.date = date;
        this.timestamp = timestamp;
    }

    private Prescription(Parcel in) {
        doctor_id = in.readString();
        patient_id = in.readString();
        doctor_name = in.readString();
        patient_name = in.readString();
        patient_phone = in.readString();
        patient_address = in.readString();
        patient_age = in.readString();
        medicines = in.readString();
        date = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<Prescription> CREATOR = new Creator<Prescription>() {
        @Override
        public Prescription createFromParcel(Parcel in) {
            return new Prescription(in);
        }

        @Override
        public Prescription[] newArray(int size) {
            return new Prescription[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(doctor_id);
        parcel.writeString(patient_id);
        parcel.writeString(doctor_name);
        parcel.writeString(patient_name);
        parcel.writeString(patient_phone);
        parcel.writeString(patient_address);
        parcel.writeString(patient_age);
        parcel.writeString(medicines);
        parcel.writeString(date);
        parcel.writeString(timestamp);
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getPatient_phone() {
        return patient_phone;
    }

    public String getPatient_address() {
        return patient_address;
    }

    public String getPatient_age() {
        return patient_age;
    }

    public String getMedicines() {
        return medicines;
    }

    public String getDate() {
        return date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public static Creator<Prescription> getCREATOR() {
        return CREATOR;
    }
}
