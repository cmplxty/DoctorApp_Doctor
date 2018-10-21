package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Model.APRequest;
import app.doctor.dmcx.app.da.project.doctorapp.R;


public class AppointmentRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.AppointmentRecyclerViewHolder> {

    private List<APRequest> apRequests = new ArrayList<>();

    public void setApRequests(List<APRequest> apRequests) {
        this.apRequests = apRequests;
    }

    @NonNull
    @Override
    public AppointmentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_appointment, parent, false);
        return new AppointmentRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerViewHolder holder, int position) {
        holder.patientNameAPTV.setText(apRequests.get(position).getPatient_name());
        holder.patientApptAPTV.setText(new StringBuilder("Appointments: ").append(apRequests.get(position).getPatient_date()).append(" - ").append(apRequests.get(position).getPatient_time()));

        holder.phoneApptIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.cancelApptIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return apRequests.size();
    }

    class AppointmentRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView patientNameAPTV;
        private TextView patientApptAPTV;
        private ImageView phoneApptIV;
        private ImageView cancelApptIV;

        AppointmentRecyclerViewHolder(View itemView) {
            super(itemView);

            patientNameAPTV = itemView.findViewById(R.id.patientNameAPTV);
            patientApptAPTV = itemView.findViewById(R.id.patientApptAPTV);
            phoneApptIV = itemView.findViewById(R.id.phoneApptIV);
            cancelApptIV = itemView.findViewById(R.id.cancelApptIV);
        }
    }
}
