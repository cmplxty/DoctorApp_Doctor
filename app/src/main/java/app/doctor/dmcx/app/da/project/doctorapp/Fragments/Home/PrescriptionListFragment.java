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
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Adapter.PrescriptionListRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Adapter.PrescriptionPatientListRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.PrescriptionController;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Prescription;
import app.doctor.dmcx.app.da.project.doctorapp.Model.PrescriptionPatient;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class PrescriptionListFragment extends Fragment {

    // Variables
    private RecyclerView prescriptionListPLRV;
    private RotateLoading mLoadingRL;

    private String patientId;
    private PrescriptionListRecyclerViewAdapter prescriptionListRecyclerViewAdapter;

    // Variables

    // Methods
    private void init(View view) {
        mLoadingRL = view.findViewById(R.id.mLoadingRL);

        prescriptionListRecyclerViewAdapter = new PrescriptionListRecyclerViewAdapter(RefActivity.refACActivity.get());
        prescriptionListPLRV = view.findViewById(R.id.prescriptionListPLRV);
        prescriptionListPLRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        prescriptionListPLRV.setHasFixedSize(true);
        prescriptionListPLRV.setAdapter(prescriptionListRecyclerViewAdapter);

        patientId = getArguments() == null ? "" : getArguments().getString(Vars.FragmentConnector.PRESCRIPTION_LIST_FRAGMENT_DATA);
    }

    private void loadData() {
        if (patientId.equals("")) {
            Toast.makeText(RefActivity.refACActivity.get(), ErrorText.NoIdSpecified, Toast.LENGTH_SHORT).show();
            return;
        }

        mLoadingRL.start();

        PrescriptionController.LoadSinglePatientPrescriptions(patientId, new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                if (object == null) return;

                List<Prescription> prescriptions = new ArrayList<>();
                for (Object prescription : (List<?>) object) {
                    prescriptions .add((Prescription) prescription);
                }

                prescriptionListRecyclerViewAdapter.setPrescription(prescriptions);
                prescriptionListRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_prescription_list, container, false);

        init(view);
        loadData();

        return view;
    }
}