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
    private String patinet_name;

    public HomeService() {
    }

    public HomeService(String doctor_name, String doctor_location, String doctor_phone, String doctor_specialist, String doctor_time, String patient_address, String patient_phone, String patinet_name) {
        this.doctor_name = doctor_name;
        this.doctor_location = doctor_location;
        this.doctor_phone = doctor_phone;
        this.doctor_specialist = doctor_specialist;
        this.doctor_time = doctor_time;
        this.patient_address = patient_address;
        this.patient_phone = patient_phone;
        this.patinet_name = patinet_name;
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

    public String getPatinet_name() {
        return patinet_name;
    }
}
