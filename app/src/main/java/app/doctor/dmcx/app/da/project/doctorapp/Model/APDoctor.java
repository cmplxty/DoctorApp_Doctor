package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class APDoctor {

    private String name;
    private String specialist;
    private String phone;
    private String clinic;
    private String appointments;
    private String email;
    private String passcode;

    public APDoctor() {
    }

    public APDoctor(String name, String specialist, String phone, String clinic, String appointments, String email, String passcode) {
        this.name = name;
        this.specialist = specialist;
        this.phone = phone;
        this.clinic = clinic;
        this.appointments = appointments;
        this.email = email;
        this.passcode = passcode;
    }

    public String getPasscode() {
        return passcode;
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
