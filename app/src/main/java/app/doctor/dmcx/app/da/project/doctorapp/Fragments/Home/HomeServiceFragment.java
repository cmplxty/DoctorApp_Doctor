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

import app.doctor.dmcx.app.da.project.doctorapp.Adapter.HomeServiceRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.HomeServiceController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICall;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IHomeServiceEvent;
import app.doctor.dmcx.app.da.project.doctorapp.Model.HomeService;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.AppDialog;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class HomeServiceFragment extends Fragment implements IHomeServiceEvent {

    // Variables
    private TextView informationHSTV;
    private RecyclerView homeServiceRequestsHSRV;
    private RotateLoading mLoadingRL;

    private HomeServiceRecyclerViewAdapter homeServiceRecyclerViewAdapter;
    private boolean isDoctorRegistered = false;
    private IHomeServiceEvent iHomeServiceEvent;

    private ICall iCall;
    // Variables

    // Methods
    private void init(View view) {
        informationHSTV = view.findViewById(R.id.informationHSTV);
        homeServiceRequestsHSRV = view.findViewById(R.id.homeServiceRequestsHSRV);
        mLoadingRL = view.findViewById(R.id.mLoadingRL);

        iHomeServiceEvent = this;

        homeServiceRecyclerViewAdapter = new HomeServiceRecyclerViewAdapter();
        homeServiceRequestsHSRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        homeServiceRequestsHSRV.setHasFixedSize(true);
        homeServiceRequestsHSRV.setAdapter(homeServiceRecyclerViewAdapter);

        iCall = homeServiceRecyclerViewAdapter.getICallPatient();

        HomeServiceController.CheckForRegisteredHomeServiceDoctor(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                isDoctorRegistered = object instanceof Boolean && (boolean) object;
                iHomeServiceEvent.onFinializeRegister();
            }
        });

        HomeServiceController.UpdateNotViewedToViewedHomeService();
    }

    private void updateUIElements() {
        if (!isDoctorRegistered) {
            informationHSTV.setText("Register, to get home service requests. Goto preference.");
        } else {
            informationHSTV.setText("No requests as of now.");
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

                homeServiceRecyclerViewAdapter.setHomeServices(homeServices);
                homeServiceRecyclerViewAdapter.notifyDataSetChanged();
                updateHomeServiceRV(homeServices);
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
        loadHomeServices();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_CALL_CODE_HS) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(RefActivity.refACActivity.get(), ValidationText.PermissionNeededForDirectCall, Toast.LENGTH_SHORT).show();
            } else {
                iCall.call();
            }
        }
    }
}
