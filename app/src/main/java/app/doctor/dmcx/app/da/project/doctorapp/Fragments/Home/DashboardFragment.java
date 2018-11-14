package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.BlogController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.CounterController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Blog;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class DashboardFragment extends Fragment {

    // Variables
    private CircleImageView doctorImageDBCIV;
    private TextView doctorNameDBTV;
    private TextView doctorDegreeDBTV;
    private TextView doctorSpecialistDBTV;
    private TextView doctorHospitalDBTV;
    private TextView doctorEmailDBTV;
    private TextView doctorPhoneDBTV;
    private TextView doctorChamberDBTV;

    private TextView newMessageCounterDBTV;
    private TextView newHomeServiceCounterDBTV;
    private TextView newApptCounterDBTV;

    private ImageView blogImageDBIV;
    private TextView blogTitleDBTV;
    private TextView blogContentDBTV;
    private Button writeBlogNowDBBTN;

    private TextView recpNameTV;
    private TextView recpStatusTV;

    private ConstraintLayout serviceDBCL;
    private ConstraintLayout blogDBCL;
    private ConstraintLayout receptionistDBCL;
    private ConstraintLayout noBlogDBCL;
    private ConstraintLayout blogMainDBCL;

    private ModelHandler modelHandler = new ModelHandler();
    // Variables

    // Class
    private static class ModelHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class ServiceLayoutRunnable implements Runnable {

        @Override
        public void run() {
            visibleServiceLayout();
        }
    }

    private class BlogLayoutRunnable implements Runnable {

        @Override
        public void run() {
            visibleBlogLayout();
        }
    }

    private class ReceptionistLayoutRunnable implements Runnable {

        @Override
        public void run() {
            visibleReceptionistLayout();
        }
    }
    // Class

    // Methods
    private void init(View view) {
        doctorImageDBCIV = view.findViewById( R.id.doctorImageDBCIV);
        doctorNameDBTV = view.findViewById( R.id.doctorNameDBTV);
        doctorDegreeDBTV = view.findViewById( R.id.doctorDegreeDBTV);
        doctorSpecialistDBTV = view.findViewById( R.id.doctorSpecialistDBTV);
        doctorHospitalDBTV = view.findViewById( R.id.doctorHospitalDBTV);
        doctorEmailDBTV = view.findViewById( R.id.doctorEmailDBTV);
        doctorPhoneDBTV = view.findViewById( R.id.doctorPhoneDBTV);
        doctorChamberDBTV = view.findViewById( R.id.doctorChamberDBTV);
        newMessageCounterDBTV = view.findViewById(R.id.newMessageCounterDBTV);
        newHomeServiceCounterDBTV = view.findViewById(R.id.newHomeServiceCounterDBTV);
        newApptCounterDBTV = view.findViewById(R.id.newApptCounterDBTV);
        blogImageDBIV = view.findViewById(R.id.blogImageDBIV);
        blogTitleDBTV = view.findViewById(R.id.blogTitleDBTV);
        blogContentDBTV = view.findViewById(R.id.blogContentDBTV);
        recpNameTV = view.findViewById(R.id.recpNameTV);
        recpStatusTV = view.findViewById(R.id.recpStatusTV);
        serviceDBCL = view.findViewById(R.id.serviceDBCL);
        blogDBCL = view.findViewById(R.id.blogDBCL);
        receptionistDBCL = view.findViewById(R.id.receptionistDBCL);
        blogMainDBCL = view.findViewById(R.id.blogMainDBCL);
        noBlogDBCL = view.findViewById(R.id.noBlogDBCL);
        writeBlogNowDBBTN = view.findViewById(R.id.writeBlogNowDBBTN);
    }

    private void task() {
        modelHandler.postDelayed(new ServiceLayoutRunnable(), 550);
        modelHandler.postDelayed(new ReceptionistLayoutRunnable(), 700);
        modelHandler.postDelayed(new BlogLayoutRunnable(), 800);
    }

    private void event() {
        writeBlogNowDBBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.BlogEditorActivity(null);
            }
        });
    }

    private void loadProfileData() {
        ProfileController.CheckForProfileData(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                Doctor doctor = (Doctor) object;
                if (doctor != null) {
                    Picasso.with(RefActivity.refACActivity.get()).load(doctor.getImage_link()).placeholder(R.drawable.noperson).into(doctorImageDBCIV);
                    doctorNameDBTV.setText(doctor.getName());
                    doctorDegreeDBTV.setText(doctor.getDegree());
                    doctorSpecialistDBTV.setText(doctor.getSpecialist());
                    doctorHospitalDBTV.setText(new StringBuilder("Hospital: ").append(doctor.getHospital()));
                    doctorEmailDBTV.setText(new StringBuilder("Email: ").append(doctor.getEmail()));
                    doctorPhoneDBTV.setText(new StringBuilder("Phone: ").append(doctor.getPhone()));
                    doctorChamberDBTV.setText(new StringBuilder("Chamber: ").append(doctor.getChamber()));
                }
            }
        });
    }

    private void loadCounter() {
        CounterController.CountNewAppointments(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object instanceof Integer) {
                    if ((Integer) object < 9) {
                        newApptCounterDBTV.setText(String.valueOf(object));
                    } else {
                        newApptCounterDBTV.setText("*");
                    }
                } else {
                    newApptCounterDBTV.setText("0");
                }
            }
        });

        CounterController.CountNewHomeService(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object instanceof Integer) {
                    if ((Integer) object < 9) {
                        newHomeServiceCounterDBTV.setText(String.valueOf(object));
                    } else {
                        newHomeServiceCounterDBTV.setText("*");
                    }
                } else {
                    newHomeServiceCounterDBTV.setText("0");
                }
            }
        });

        CounterController.CountNewMessages(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object instanceof Integer) {
                    if ((Integer) object < 9) {
                        newMessageCounterDBTV.setText(String.valueOf(object));
                    } else {
                        newMessageCounterDBTV.setText("*");
                    }
                } else {
                    newMessageCounterDBTV.setText("0");
                }
            }
        });
    }

    private void loadLastBlog() {
        BlogController.LoadLastBlog(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object != null) {
                    switchBlogLayout(View.VISIBLE, View.GONE);
                    Blog blog = (Blog) object;

                    if (!blog.getPoster().equals(""))
                        Picasso.with(RefActivity.refACActivity.get()).load(blog.getPoster()).placeholder(R.drawable.noimage).into(blogImageDBIV);

                    blogTitleDBTV.setText(blog.getTitle());
                    blogContentDBTV.setText(blog.getTitle());

                } else {
                    switchBlogLayout(View.GONE, View.VISIBLE);
                }
            }
        });
    }

    private void goneLayouts() {
        serviceDBCL.setVisibility(View.GONE);
        blogDBCL.setVisibility(View.GONE);
        receptionistDBCL.setVisibility(View.GONE);
    }

    private void visibleServiceLayout() {
        serviceDBCL.setVisibility(View.VISIBLE);
    }

    private void visibleBlogLayout() {
        blogDBCL.setVisibility(View.VISIBLE);
    }

    private void switchBlogLayout(int i1, int i2) {
        blogMainDBCL.setVisibility(i1);
        noBlogDBCL.setVisibility(i2);
    }

    private void visibleReceptionistLayout() {
        receptionistDBCL.setVisibility(View.VISIBLE);
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_dashboard, container, false);
        init(view);
        event();
        goneLayouts();
        task();
        loadProfileData();
        loadCounter();
        loadLastBlog();
        return view;
    }
}
