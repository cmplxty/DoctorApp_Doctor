package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Adapter.AppointmentRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AppointmentController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAppointmentEvent;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICallPatient;
import app.doctor.dmcx.app.da.project.doctorapp.Model.APRequest;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class AppointmentFragment extends Fragment implements IAppointmentEvent {

    // Variables
    private RotateLoading mLoadingRL;
    private Button setUpApptHSBTN;
    private RecyclerView appointmentAPRV;
    private TextView noDataFoundTV;

    private AppointmentRecyclerViewAdapter appointmentRecyclerViewAdapter;
    private boolean isAppointmentDoctor;
    private IAppointmentEvent iAppointmentEvent;
    private ICallPatient iCallPatient;
    // Variables

    // Methods
    private void init(View view) {
        mLoadingRL = view.findViewById(R.id.mLoadingRL);
        setUpApptHSBTN = view.findViewById(R.id.setUpApptHSBTN);
        appointmentAPRV = view.findViewById(R.id.appointmentAPRV);
        noDataFoundTV = view.findViewById(R.id.noDataFoundTV);

        appointmentRecyclerViewAdapter = new AppointmentRecyclerViewAdapter();
        appointmentAPRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        appointmentAPRV.setHasFixedSize(true);
        appointmentAPRV.setAdapter(appointmentRecyclerViewAdapter);

        isAppointmentDoctor = false;
        iAppointmentEvent = this;
        iCallPatient = appointmentRecyclerViewAdapter.getiCallPatient();

        AppointmentController.CheckAppointmentDoctor(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                isAppointmentDoctor = (boolean) object;
                iAppointmentEvent.onFinalize();
            }
        });

        AppointmentController.UpdateNotViewedToViewedAppointment();
    }

    private void updateUiElements() {
        if (isAppointmentDoctor) {
            setUpApptHSBTN.setText("Remove");
        } else {
            setUpApptHSBTN.setText("Setup");
        }
    }

    private void updateUi(List<APRequest> apRequests) {
        if (apRequests.size() > 0) {
            appointmentAPRV.setVisibility(View.VISIBLE);
            noDataFoundTV.setVisibility(View.GONE);
        } else {
            noDataFoundTV.setVisibility(View.VISIBLE);
            appointmentAPRV.setVisibility(View.GONE);
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
        mLoadingRL.start();

        AppointmentController.LoadAllAppointmentRequests(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                List<APRequest> apRequests = new ArrayList<>();

                if (object != null) {
                    for (Object request : (List<?>) object) {
                        apRequests.add((APRequest) request);
                    }
                }

                appointmentRecyclerViewAdapter.setApRequests(apRequests);
                appointmentRecyclerViewAdapter.notifyDataSetChanged();
                updateUi(apRequests);
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_CALL_CODE_AP) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iCallPatient.call();
            } else {
                Toast.makeText(RefActivity.refACActivity.get(), ValidationText.PermissionNeededForDirectCall, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
