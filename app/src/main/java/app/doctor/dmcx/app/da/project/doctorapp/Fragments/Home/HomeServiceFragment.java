package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import app.doctor.dmcx.app.da.project.doctorapp.Controller.HomeServiceController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IHomeServiceEvent;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.AppDialog;

public class HomeServiceFragment extends Fragment implements IHomeServiceEvent {

    // Variables
    private TextView homeServiceRegsiterHSTV;
    private TextView informationHSTV;
    private Button registerHSBTN;

    private boolean isDoctorRegistered = false;
    private IHomeServiceEvent iHomeServiceEvent;
    // Variables

    // Methods
    private void init(View view) {
        homeServiceRegsiterHSTV = view.findViewById(R.id.homeServiceRegsiterHSTV);
        informationHSTV = view.findViewById(R.id.informationHSTV);
        registerHSBTN = view.findViewById(R.id.registerHSBTN);
        homeServiceRegsiterHSTV = view.findViewById(R.id.homeServiceRegsiterHSTV);

        iHomeServiceEvent = this;

        HomeServiceController.CheckForRegisteredHomeServiceDoctor(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                isDoctorRegistered = object instanceof Boolean && (boolean) object;
                iHomeServiceEvent.onFinializeRegister();
            }
        });
    }

    private void updateUIElements() {
        if (!isDoctorRegistered) {
            homeServiceRegsiterHSTV.setText("You have to register for this Service.");
            informationHSTV.setText("Register, to get HS requests.");
            registerHSBTN.setText("Register");
        } else {
            homeServiceRegsiterHSTV.setText("You are registered for this service. You can get requests here.");
            informationHSTV.setText("No requests as of now.");
            registerHSBTN.setText("Unregister");
        }
    }

    private void event() {
        registerHSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isDoctorRegistered) {
                    HomeServiceController.UnregisterHomeService();
                } else {
                    showRegisterDialog();
                }

            }
        });
    }

    private void showRegisterDialog() {
        AppDialog.HomeServiceDialog.register();
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_home_service, container, false);

        init(view);

        return view;
    }

    @Override
    public void onFinializeRegister() {
        updateUIElements();
        event();
    }
}
