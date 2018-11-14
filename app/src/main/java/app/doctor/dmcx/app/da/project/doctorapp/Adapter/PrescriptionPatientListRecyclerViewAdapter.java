package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.AppFragmentManager;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.FragmentNames;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.PrescriptionListFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Prescription;
import app.doctor.dmcx.app.da.project.doctorapp.Model.PrescriptionPatient;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class PrescriptionPatientListRecyclerViewAdapter extends RecyclerView.Adapter<PrescriptionPatientListRecyclerViewAdapter.PrescriptionPatientListRecyclerViewHolder> {

    private Context context;
    private List<PrescriptionPatient> prescriptionPatients = new ArrayList<>();

    public PrescriptionPatientListRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setPrescriptionPatients(List<PrescriptionPatient> prescriptionPatients) {
        this.prescriptionPatients = prescriptionPatients;
    }

    @NonNull
    @Override
    public PrescriptionPatientListRecyclerViewAdapter.PrescriptionPatientListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_single_prescription_patient, parent, false);
        return new PrescriptionPatientListRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionPatientListRecyclerViewAdapter.PrescriptionPatientListRecyclerViewHolder holder, int position) {

        final int itemPosition = position;

        holder.patientsNamePPTV.setText(prescriptionPatients.get(position).getPatient_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Vars.Connector.PRESCRIPTION_LIST_FRAGMENT_DATA, prescriptionPatients.get(itemPosition).getPatient_id());

                AppFragmentManager.replace(RefActivity.refACActivity.get(), AppFragmentManager.homeFragmentContainer, AppFragmentManager.addArgumentFragment(new PrescriptionListFragment(), bundle), FragmentNames.Prescription);
            }
        });

        if (position == prescriptionPatients.size() - 1 && prescriptionPatients.size() != 1) {
            holder.relativeLayoutLineRL.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return prescriptionPatients.size();
    }

    class PrescriptionPatientListRecyclerViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout relativeLayoutLineRL;
        private TextView patientsNamePPTV;

        PrescriptionPatientListRecyclerViewHolder(View itemView) {
            super(itemView);

            relativeLayoutLineRL = itemView.findViewById(R.id.relativeLayoutLineRL);
            patientsNamePPTV = itemView.findViewById(R.id.patientsNamePPTV);
        }
    }
}
