package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class Appointment {

    private String name;
    private String doctorName;
    private String location;
    private String dateTime;
    private String remainderTime;

    public Appointment() {
    }

    public Appointment(String name, String doctorName, String location, String dateTime, String remainderTime) {
        this.name = name;
        this.doctorName = doctorName;
        this.location = location;
        this.dateTime = dateTime;
        this.remainderTime = remainderTime;
    }

    public String getName() {
        return name;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getRemainderTime() {
        return remainderTime;
    }
}