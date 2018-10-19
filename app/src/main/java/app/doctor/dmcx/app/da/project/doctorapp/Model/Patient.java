package app.doctor.dmcx.app.da.project.doctorapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Patient implements Parcelable {

    private String id;
    private String name;
    private String email;
    private String link;
    private String phone;
    private String address;
    private String country;
    private String gender;
    private String dob;
    private String age;
    private String type;

    public Patient() {
    }

    protected Patient(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        link = in.readString();
        phone = in.readString();
        address = in.readString();
        country = in.readString();
        gender = in.readString();
        dob = in.readString();
        age = in.readString();
        type = in.readString();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public String getDob() {
        return dob;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLink() {
        return link;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getType() {
        return type;
    }

    public static Creator<Patient> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(link);
        parcel.writeString(phone);
        parcel.writeString(address);
        parcel.writeString(country);
        parcel.writeString(gender);
        parcel.writeString(dob);
        parcel.writeString(age);
        parcel.writeString(type);
    }
}
