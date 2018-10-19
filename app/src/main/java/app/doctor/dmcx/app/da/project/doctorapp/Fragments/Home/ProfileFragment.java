package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.content.Intent;
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

import app.doctor.dmcx.app.da.project.doctorapp.Activities.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AuthController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.R;


public class ProfileFragment extends Fragment {

    // Variables
    private ImageView doctorProfilePicIV;
    private ImageView doctorPaymentIV;
    private ImageView doctorEditProfileIV;
    private TextView doctorNameTV;
    private TextView doctorEmailTV;
    private TextView doctorPhoneTV;
    private TextView doctorSpecialistTV; //
    private TextView doctorChamberTV;
    private TextView doctorCountryTV;
    private Button signOutPBTN;
    // Variables

    // Methods
    private void init(View view) {
        doctorProfilePicIV = view.findViewById(R.id.doctorProfilePicIV);
        doctorPaymentIV = view.findViewById(R.id.doctorPaymentIV);
        doctorEditProfileIV = view.findViewById(R.id.doctorEditProfileIV);
        doctorNameTV = view.findViewById(R.id.doctorNameTV);
        doctorEmailTV = view.findViewById(R.id.doctorEmailTV);
        doctorPhoneTV = view.findViewById(R.id.doctorPhoneTV);
        doctorChamberTV = view.findViewById(R.id.doctorChamberTV);
        doctorSpecialistTV = view.findViewById(R.id.doctorSpecialistTV);
        doctorCountryTV = view.findViewById(R.id.doctorCountryTV);
        signOutPBTN = view.findViewById(R.id.signOutPBTN);
    }

    private void event() {
        doctorEditProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        doctorPaymentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        signOutPBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthController.SignOut();
                ActivityTrigger.AuthActivity();
            }
        });
    }

    private void load() {
        ProfileController.LoadLocalProfile(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                Doctor doctor = (Doctor) object;

                if (doctor != null) {
                    Picasso.with(RefActivity.refACActivity.get()).load(doctor.getImage_link()).placeholder(R.drawable.noprofilepic).into(doctorProfilePicIV);

                    doctorNameTV.setText(doctor.getName());
                    doctorEmailTV.setText(doctor.getEmail());
                    doctorPhoneTV.setText(doctor.getPhone());
                    doctorSpecialistTV.setText(doctor.getSpecialist());
                    doctorChamberTV.setText(doctor.getChamber());
                    doctorCountryTV.setText(doctor.getCountry());
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
        event();
        load();

        return view;
    }
}
