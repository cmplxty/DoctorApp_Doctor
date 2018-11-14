package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class APRequest {

    private String doctor_name;
    private String doctor_clinic;
    private String doctor_phone;
    private String patient_phone;
    private String patient_name;
    private String patient_id;
    private String time;
    private String date;
    private String status;
    private String notification_status;
    private String timestamp;

    public APRequest() {
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getDoctor_clinic() {
        return doctor_clinic;
    }

    public String getDoctor_phone() {
        return doctor_phone;
    }

    public String getPatient_phone() {
        return patient_phone;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }
}