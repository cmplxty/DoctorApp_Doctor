package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class Message {

    private String userProfileName;
    private String userProfileImage;

    private String timestamp;
    private String content;
    private String from;
    private String to;
    private String type;

    public String getUserProfileName() {
        return userProfileName;
    }

    public void setUserProfileName(String userProfileName) {
        this.userProfileName = userProfileName;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
