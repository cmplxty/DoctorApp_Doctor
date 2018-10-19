package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class PrescriptionPatient {

    private String patient_id;
    private String patient_name;

    public PrescriptionPatient(String patient_id, String patient_name) {
        this.patient_id = patient_id;
        this.patient_name = patient_name;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }
}
