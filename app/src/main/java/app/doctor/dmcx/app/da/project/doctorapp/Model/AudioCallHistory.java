package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class AudioCallHistory {

    private String id = "";
    private String caller_id = "";
    private String call_status = "";
    private String date = "";
    private String timestamp = "";
    private Object user;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaller_id() {
        return caller_id;
    }

    public void setCaller_id(String caller_id) {
        this.caller_id = caller_id;
    }

    public String getCall_status() {
        return call_status;
    }

    public void setCall_status(String call_status) {
        this.call_status = call_status;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }
}
