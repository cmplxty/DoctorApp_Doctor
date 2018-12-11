package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AuthController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.AppSupportFragmentManager;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.FragmentNames;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    // Variables
    private CircleImageView doctorProfilePicCIV;
    private TextView doctorNameTV;
    private TextView doctorEmailTV;
    private TextView doctorPhoneTV;
    private TextView doctorSpecialistTV;
    private TextView doctorChamberTV;
    private TextView doctorCountryTV;
    private TextView doctorAboutTV;
    private TextView doctorHospitalTV;

    private Doctor doctor;
    // Variables

    // Methods
    private void init(View view) {
        doctorProfilePicCIV = view.findViewById(R.id.doctorProfilePicCIV);
        doctorNameTV = view.findViewById(R.id.doctorNameTV);
        doctorEmailTV = view.findViewById(R.id.doctorEmailTV);
        doctorPhoneTV = view.findViewById(R.id.doctorPhoneTV);
        doctorChamberTV = view.findViewById(R.id.doctorChamberTV);
        doctorSpecialistTV = view.findViewById(R.id.doctorSpecialistTV);
        doctorCountryTV = view.findViewById(R.id.doctorCountryTV);
        doctorAboutTV = view.findViewById(R.id.doctorAboutTV);
        doctorHospitalTV = view.findViewById(R.id.doctorHospitalTV);
    }

    private void load() {
        ProfileController.CheckForProfileData(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                doctor = (Doctor) object;

                if (doctor != null) {
                    Picasso.with(RefActivity.refACActivity.get()).load(doctor.getImage_link()).placeholder(R.drawable.noprofilepic).into(doctorProfilePicCIV);

                    doctorNameTV.setText(doctor.getName());
                    doctorEmailTV.setText(doctor.getEmail());
                    doctorPhoneTV.setText(doctor.getPhone());
                    doctorSpecialistTV.setText(doctor.getSpecialist());
                    doctorChamberTV.setText(doctor.getChamber());
                    doctorCountryTV.setText(doctor.getCountry());
                    doctorHospitalTV.setText(doctor.getHospital());
                    doctorAboutTV.setText(doctor.getAbout());
                }
            }
        });
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_profile, container, false);
        init(view);
        load();
        return view;
    }
}
