package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Adapter.AppointmentRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AppointmentController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAppointment;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAppointmentEvent;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class AppointmentFragment extends Fragment implements IAppointmentEvent {

    // Variables
    private Button setUpApptHSBTN;
    private RecyclerView appointmentAPRV;

    private AppointmentRecyclerViewAdapter appointmentRecyclerViewAdapter;
    private boolean isAppointmentDoctor;
    private IAppointmentEvent iAppointmentEvent;
    // Variables

    // Methods
    private void init(View view) {
        setUpApptHSBTN = view.findViewById(R.id.setUpApptHSBTN);
        appointmentAPRV = view.findViewById(R.id.appointmentAPRV);

        appointmentRecyclerViewAdapter = new AppointmentRecyclerViewAdapter();

        appointmentAPRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        appointmentAPRV.setHasFixedSize(true);
        appointmentAPRV.setAdapter(appointmentRecyclerViewAdapter);

        isAppointmentDoctor = false;
        iAppointmentEvent = this;

        AppointmentController.CheckAppointmentDoctor(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                isAppointmentDoctor = (boolean) object;
                iAppointmentEvent.onFinalize();
            }
        });
    }

    private void updateUiElements() {
        if (isAppointmentDoctor) {
            setUpApptHSBTN.setText("Remove");
        } else {
            setUpApptHSBTN.setText("Setup");
        }
    }

    private void event() {
        setUpApptHSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAppointmentDoctor)
                    ActivityTrigger.SetupAppointmentActivity();
                else
                    AppointmentController.RemoveAppointmentDoctor();
            }
        });
    }

    private void loadAppointments() {

    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_appointment, container, false);
        init(view);
        return view;
    }


    @Override
    public void onFinalize() {
        updateUiElements();
        event();
        loadAppointments();
    }
}
