package app.doctor.dmcx.app.da.project.doctorapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable {

    private String id;
    private String about;
    private String chamber;
    private String city;
    private String country;
    private String degree;
    private String email;
    private String hospital;
    private String image_link;
    private String name;
    private String phone;
    private String rating;
    private String registration;
    private String specialist;

    public Doctor() {
    }

    protected Doctor(Parcel in) {
        id = in.readString();
        about = in.readString();
        chamber = in.readString();
        city = in.readString();
        country = in.readString();
        degree = in.readString();
        email = in.readString();
        hospital = in.readString();
        image_link = in.readString();
        name = in.readString();
        phone = in.readString();
        rating = in.readString();
        registration = in.readString();
        specialist = in.readString();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public void setAbout(String about) {
        this.about = about;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChamber() {
        return chamber;
    }

    public String getDegree() {
        return degree;
    }

    public String getHospital() {
        return hospital;
    }

    public String getRating() {
        return rating;
    }

    public String getRegistration() {
        return registration;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getCity() {
        return city;
    }

    public String getAbout() {
        return about;
    }

    public String getSpecialist() {
        return specialist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(about);
        parcel.writeString(chamber);
        parcel.writeString(city);
        parcel.writeString(country);
        parcel.writeString(degree);
        parcel.writeString(email);
        parcel.writeString(hospital);
        parcel.writeString(image_link);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(rating);
        parcel.writeString(registration);
        parcel.writeString(specialist);
    }
}