package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class Doctor {

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
}