package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Model.Appointment;
import app.doctor.dmcx.app.da.project.doctorapp.R;


public class AppointmentRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.AppointmentRecyclerViewHolder> {

    private Context context;
    private List<Appointment> appointments;

    public AppointmentRecyclerViewAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_single_appt, parent, false);
        return new AppointmentRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class AppointmentRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView apptDoctorNameTV;
        private TextView apptDateTimeTV;
        private TextView apptLocationTV;
        private ImageView cancelApptIV;

        AppointmentRecyclerViewHolder(View itemView) {
            super(itemView);

            apptDoctorNameTV = itemView.findViewById(R.id.apptDoctorNameTV);
            apptDateTimeTV = itemView.findViewById(R.id.apptDateTimeTV);
            apptLocationTV = itemView.findViewById(R.id.apptLocationTV);
            cancelApptIV = itemView.findViewById(R.id.cancelApptIV);
        }
    }
}
