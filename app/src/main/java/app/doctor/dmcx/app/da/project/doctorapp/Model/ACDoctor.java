package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class ACDoctor {

    private Doctor doctor;
    private String audio_call_status;

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getAudio_call_status() {
        return audio_call_status;
    }

    public void setAudio_call_status(String audio_call_status) {
        this.audio_call_status = audio_call_status;
    }
}
