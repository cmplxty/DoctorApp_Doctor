package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.app.AlertDialog;
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

import com.google.firebase.database.DataSnapshot;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Adapter.HomeServiceRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.HomeServiceController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICallPatient;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IHomeServiceEvent;
import app.doctor.dmcx.app.da.project.doctorapp.Model.HomeService;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.AppDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class HomeServiceFragment extends Fragment implements IHomeServiceEvent {

    // Variables
    private TextView homeServiceRegsiterHSTV;
    private TextView informationHSTV;
    private Button registerHSBTN;
    private RecyclerView homeServiceRequestsHSRV;
    private RotateLoading mLoadingRL;

    private HomeServiceRecyclerViewAdapter homeServiceRecyclerViewAdapter;
    private boolean isDoctorRegistered = false;
    private IHomeServiceEvent iHomeServiceEvent;

    private ICallPatient iCallPatient;
    // Variables

    // Methods
    private void init(View view) {
        homeServiceRegsiterHSTV = view.findViewById(R.id.homeServiceRegsiterHSTV);
        informationHSTV = view.findViewById(R.id.informationHSTV);
        registerHSBTN = view.findViewById(R.id.registerHSBTN);
        homeServiceRegsiterHSTV = view.findViewById(R.id.homeServiceRegsiterHSTV);
        homeServiceRequestsHSRV = view.findViewById(R.id.homeServiceRequestsHSRV);
        mLoadingRL = view.findViewById(R.id.mLoadingRL);

        iHomeServiceEvent = this;

        homeServiceRecyclerViewAdapter = new HomeServiceRecyclerViewAdapter();
        homeServiceRequestsHSRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        homeServiceRequestsHSRV.setHasFixedSize(true);
        homeServiceRequestsHSRV.setAdapter(homeServiceRecyclerViewAdapter);

        iCallPatient = homeServiceRecyclerViewAdapter.getICallPatient();

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

    private void updateHomeServiceRV(List<HomeService> homeServices) {
        if (homeServices.size() > 0) {
            homeServiceRequestsHSRV.setVisibility(View.VISIBLE);
            informationHSTV.setVisibility(View.GONE);
        } else {
            informationHSTV.setVisibility(View.VISIBLE);
            homeServiceRequestsHSRV.setVisibility(View.GONE);
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

    private void loadHomeServices() {
        mLoadingRL.start();

        HomeServiceController.LoadHomeServiceRequests(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                List<HomeService> homeServices = new ArrayList<>();
                if (object != null) {
                    for (Object homeService : (List<?>) object) {
                        homeServices.add((HomeService) homeService);
                    }
                }

                updateHomeServiceRV(homeServices);
                homeServiceRecyclerViewAdapter.setHomeServices(homeServices);
                homeServiceRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
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
        loadHomeServices();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_CALL_CODE_HS) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(RefActivity.refACActivity.get(), ValidationText.PermissionNeededForDirectCall, Toast.LENGTH_SHORT).show();
            } else {
                iCallPatient.call();
            }
        }
    }
}
