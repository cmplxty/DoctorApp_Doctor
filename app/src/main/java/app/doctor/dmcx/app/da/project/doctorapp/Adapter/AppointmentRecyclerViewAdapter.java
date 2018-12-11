package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AppointmentController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AudioCallController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICall;
import app.doctor.dmcx.app.da.project.doctorapp.Model.APRequest;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Appointment;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;


public class AppointmentRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.AppointmentRecyclerViewHolder> implements ICall {

    private List<APRequest> apRequests = new ArrayList<>();
    private int lastPosition;

    public ICall getiCallPatient() {
        return this;
    }

    public void setApRequests(List<APRequest> apRequests) {
        this.apRequests = apRequests;
    }

    public class AppointmentDialog {
        private String[] options = new String[] {"Call Patient", "Accept Request", "Cancel Request", "Remove Appointment"};
        private Context context;
        private int position;

        private AppointmentDialog(Context context, int position) {
            this.context = context;
            this.position = position;
        }

        public void create() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select an options");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int selectedPosition) {
                    dialogInterface.dismiss();

                    if (selectedPosition == 0) {
                        lastPosition = position;

                        if (checkPermission()) {
                            callPatient(position);
                        }
                    } else if (selectedPosition == 1) {
                        if (!apRequests.get(position).getStatus().equals(AFModel.accept)) {
                            AppointmentController.AcceptAppointmentRequest(apRequests.get(position).getPatient_id(), null);
                        } else {
                            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AlreadyRequestAccepted, Toast.LENGTH_SHORT).show();
                        }
                    } else if (selectedPosition == 2) {
                        AppointmentController.CancelAppointmentRequest(apRequests.get(position).getPatient_id(), null);
                    } else if (selectedPosition == 3) {
                        AppointmentController.DeleteAppointmentRequest(apRequests.get(position).getPatient_id());
                    } else {
                        Log.d(Vars.TAG, "Dialog: Unknown Call");
                    }
                }
            });

            builder.show();
        }
    }

    private void callPatient(int position) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(apRequests.get(position).getPatient_phone())) {
            Intent callIntent = new Intent();
            callIntent.setAction(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + apRequests.get(position).getPatient_phone()));
            RefActivity.refACActivity.get().startActivity(callIntent);
        } else {
            Toast.makeText(RefActivity.refACActivity.get(), ErrorText.InvalidPhoneNumberGiven, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Vars.currentFragment.requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, Vars.RequestCode.REQUEST_CALL_CODE_AP);
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public AppointmentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_appointment, parent, false);
        return new AppointmentRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        holder.patientNameAPTV.setText(apRequests.get(position).getPatient_name());
        holder.patientDateAPTV.setText(new StringBuilder("Date: ").append(apRequests.get(position).getDate()));
        holder.patientStatusAPTV.setText(new StringBuilder("Status: ").append(apRequests.get(position).getStatus()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentDialog dialog = new AppointmentDialog(RefActivity.refACActivity.get(), itemPosition);
                dialog.create();
            }
        });

        if (apRequests.size() > 1 && apRequests.size() - 1 == position) {
            holder.bottomLineAPV.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return apRequests.size();
    }

    @Override
    public void call() {
        callPatient(lastPosition);
    }

    class AppointmentRecyclerViewHolder extends RecyclerView.ViewHolder {

        private View bottomLineAPV;
        private TextView patientNameAPTV;
        private TextView patientDateAPTV;
        private TextView patientStatusAPTV;

        AppointmentRecyclerViewHolder(View itemView) {
            super(itemView);

            bottomLineAPV = itemView.findViewById(R.id.bottomLineAPV);
            patientNameAPTV = itemView.findViewById(R.id.patientNameAPTV);
            patientDateAPTV = itemView.findViewById(R.id.patientDateAPTV);
            patientStatusAPTV = itemView.findViewById(R.id.patientStatusAPTV);
        }
    }
}
