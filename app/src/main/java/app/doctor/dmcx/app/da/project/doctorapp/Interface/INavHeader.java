package app.doctor.dmcx.app.da.project.doctorapp.Interface;

import android.widget.ImageView;
import android.widget.TextView;

import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import de.hdodenhof.circleimageview.CircleImageView;

public interface INavHeader {
    void onNavHeaderUpdate(Doctor doctor);
}
