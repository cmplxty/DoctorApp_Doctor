package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class APDoctor {

    private String id;
    private String name;
    private String specialist;
    private String phone;
    private String clinic;
    private String email;
    private String appointments;

    public APDoctor() {}

    public APDoctor(String name, String specialist, String phone, String clinic, String appointments, String email) {
        this.name = name;
        this.specialist = specialist;
        this.phone = phone;
        this.clinic = clinic;
        this.appointments = appointments;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSpecialist() {
        return specialist;
    }

    public String getPhone() {
        return phone;
    }

    public String getClinic() {
        return clinic;
    }

    public String getAppointments() {
        return appointments;
    }
}
