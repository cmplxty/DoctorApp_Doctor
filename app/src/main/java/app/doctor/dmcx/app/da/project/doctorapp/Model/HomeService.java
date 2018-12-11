package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class HomeService {

    private String doctor_name = "";
    private String doctor_location = "";
    private String doctor_phone = "";
    private String doctor_specialist = "";
    private String doctor_time = "";
    private String patient_id = "";
    private String patient_address = "";
    private String patient_phone = "";
    private String patient_name = "";
    private String timestamp = "";
    private String notification_status = "";

    public HomeService() {
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public void setDoctor_location(String doctor_location) {
        this.doctor_location = doctor_location;
    }

    public void setDoctor_phone(String doctor_phone) {
        this.doctor_phone = doctor_phone;
    }

    public void setDoctor_specialist(String doctor_specialist) {
        this.doctor_specialist = doctor_specialist;
    }

    public void setDoctor_time(String doctor_time) {
        this.doctor_time = doctor_time;
    }

    public void setPatient_address(String patient_address) {
        this.patient_address = patient_address;
    }

    public void setPatient_phone(String patient_phone) {
        this.patient_phone = patient_phone;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setNotification_status(String notification_status) {
        this.notification_status = notification_status;
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
