package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Prescription;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class PrescriptionListRecyclerViewAdapter extends RecyclerView.Adapter<PrescriptionListRecyclerViewAdapter.PrescriptionListRecyclerViewHolder> {

    private Context context;
    private List<Prescription> prescriptions = new ArrayList<>();

    public PrescriptionListRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setPrescription(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    @NonNull
    @Override
    public PrescriptionListRecyclerViewAdapter.PrescriptionListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_single_prescription, parent, false);
        return new PrescriptionListRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionListRecyclerViewAdapter.PrescriptionListRecyclerViewHolder holder, int position) {

        final int itemPosition = position;

        holder.pPatientsNamePTV.setText(prescriptions.get(position).getPatient_name());
        holder.pDatePTV.setText(new StringBuilder("Date: ").append(prescriptions.get(position).getDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTrigger.PrescriptionActivity(prescriptions.get(itemPosition));
            }
        });

        if (position == prescriptions.size() - 1 && prescriptions.size() != 1) {
            holder.relativeLayoutLineRL.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    class PrescriptionListRecyclerViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout relativeLayoutLineRL;
        private TextView pPatientsNamePTV;
        private TextView pDatePTV;

        PrescriptionListRecyclerViewHolder(View itemView) {
            super(itemView);

            relativeLayoutLineRL = itemView.findViewById(R.id.relativeLayoutLineRL);
            pPatientsNamePTV = itemView.findViewById(R.id.pPatientsNamePTV);
            pDatePTV = itemView.findViewById(R.id.pDatePTV);
        }
    }
}
