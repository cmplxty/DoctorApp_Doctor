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

import app.doctor.dmcx.app.da.project.doctorapp.Adapter.PrescriptionPatientListRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.PrescriptionController;
import app.doctor.dmcx.app.da.project.doctorapp.Model.PrescriptionPatient;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class PrescriptionPatientListFragment extends Fragment {

    // Variables
    private RecyclerView prescriptionPatinetListRV;
    private RotateLoading mLoadingRL;

    private PrescriptionPatientListRecyclerViewAdapter prescriptionPatientListRecyclerViewAdapter;
    // Variables

    // Methods
    private void init(View view) {
        mLoadingRL = view.findViewById(R.id.mLoadingRL);

        prescriptionPatinetListRV = view.findViewById(R.id.prescriptionPatinetListRV);
        prescriptionPatinetListRV.setHasFixedSize(true);
        prescriptionPatinetListRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));

        prescriptionPatientListRecyclerViewAdapter = new PrescriptionPatientListRecyclerViewAdapter(RefActivity.refACActivity.get());
        prescriptionPatinetListRV.setAdapter(prescriptionPatientListRecyclerViewAdapter);
    }

    private void loadPatinets() {
        mLoadingRL.start();

        PrescriptionController.LoadAllPrescriptionPatients(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                mLoadingRL.stop();

                if (object == null) return;

                List<PrescriptionPatient> prescriptionPatients = new ArrayList<>();
                for (Object prescriptionPatient : (List<?>) object) {
                    prescriptionPatients.add((PrescriptionPatient) prescriptionPatient);
                }

                prescriptionPatientListRecyclerViewAdapter.setPrescriptionPatients(prescriptionPatients);
                prescriptionPatientListRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_prescription_patient_list, container, false);

        init(view);
        loadPatinets();

        return view;
    }
}
