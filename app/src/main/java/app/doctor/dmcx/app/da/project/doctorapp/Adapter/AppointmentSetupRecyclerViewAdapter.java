package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAppointment;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Appointment;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class AppointmentSetupRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentSetupRecyclerViewAdapter.AppointmentSetupRecyclerViewHolder> {

    private List<Appointment> appointments = new ArrayList<>();
    private IAppointment iAppointment;

    public AppointmentSetupRecyclerViewAdapter(IAppointment iAppointment) {
        this.iAppointment = iAppointment;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentSetupRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_appointment_doctor, parent, false);
        return new AppointmentSetupRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentSetupRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        holder.daysAPDTV.setText(new StringBuilder("Days: ").append(appointments.get(position).getDays()));
        holder.timeAPDTV.setText(new StringBuilder("Time: ").append(appointments.get(position).getTime()));
        holder.deleteAPDIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iAppointment.delete(itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class AppointmentSetupRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView daysAPDTV;
        private TextView timeAPDTV;
        private ImageButton deleteAPDIB;

        AppointmentSetupRecyclerViewHolder(View itemView) {
            super(itemView);

            daysAPDTV = itemView.findViewById(R.id.daysAPDTV);
            timeAPDTV = itemView.findViewById(R.id.timeAPDTV);
            deleteAPDIB = itemView.findViewById(R.id.deleteAPDIB);
        }
    }
}
