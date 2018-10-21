package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class APRequest {

    private String patient_name;
    private String patient_time;
    private String patient_date;
    private String patient_phone;

    public APRequest() {}

    public APRequest(String patient_name, String patient_time, String patient_date, String patient_phone) {
        this.patient_name = patient_name;
        this.patient_time = patient_time;
        this.patient_date = patient_date;
        this.patient_phone = patient_phone;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getPatient_time() {
        return patient_time;
    }

    public String getPatient_date() {
        return patient_date;
    }

    public String getPatient_phone() {
        return patient_phone;
    }
}