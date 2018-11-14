package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class HomeService {

    private String doctor_name;
    private String doctor_location;
    private String doctor_phone;
    private String doctor_specialist;
    private String doctor_time;
    private String patient_id;
    private String patient_address;
    private String patient_phone;
    private String patient_name;
    private String timestamp;
    private String notification_status;

    public HomeService() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getDoctor_location() {
        return doctor_location;
    }

    public String getDoctor_phone() {
        return doctor_phone;
    }

    public String getDoctor_specialist() {
        return doctor_specialist;
    }

    public String getDoctor_time() {
        return doctor_time;
    }

    public String getPatient_address() {
        return patient_address;
    }

    public String getPatient_phone() {
        return patient_phone;
    }

    public String getPatient_name() {
        return patient_name;
    }
}
