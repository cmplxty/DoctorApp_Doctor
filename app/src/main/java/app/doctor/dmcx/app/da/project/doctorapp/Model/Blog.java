package app.doctor.dmcx.app.da.project.doctorapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Blog implements Parcelable {

    private String id = "";
    private String blogger_id = "";
    private String name = "";
    private String detail = "";
    private String image_link = "";
    private String poster = "";
    private String title = "";
    private String content = "";
    private String date = "";
    private String timestamp = "";

    public Blog() {
    }

    protected Blog(Parcel in) {
        id = in.readString();
        blogger_id = in.readString();
        name = in.readString();
        detail = in.readString();
        image_link = in.readString();
        poster = in.readString();
        title = in.readString();
        content = in.readString();
        date = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(blogger_id);
        parcel.writeString(name);
        parcel.writeString(detail);
        parcel.writeString(image_link);
        parcel.writeString(poster);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(date);
        parcel.writeString(timestamp);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlogger_id() {
        return blogger_id;
    }

    public void setBlogger_id(String blogger_id) {
        this.blogger_id = blogger_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public static Creator<Blog> getCREATOR() {
        return CREATOR;
    }
}