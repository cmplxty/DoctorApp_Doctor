package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AppointmentController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICallPatient;
import app.doctor.dmcx.app.da.project.doctorapp.Model.APRequest;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;


public class AppointmentRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.AppointmentRecyclerViewHolder> implements ICallPatient {

    private List<APRequest> apRequests = new ArrayList<>();
    private int lastPosition;

    public ICallPatient getiCallPatient() {
        return this;
    }

    public void setApRequests(List<APRequest> apRequests) {
        this.apRequests = apRequests;
    }

    private void updateUi(int position, AppointmentRecyclerViewHolder holder) {
        if (!apRequests.get(position).getStatus().equals(AFModel.cancel)) {
            holder.cancelApptIV.setImageResource(R.drawable.cross_cyan);
        } else {
            holder.cancelApptIV.setImageResource(R.drawable.circle_cross_cyan);
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
        final AppointmentRecyclerViewHolder tmpHolder = holder;

        holder.patientNameAPTV.setText(apRequests.get(position).getPatient_name());
        holder.patientDateAPTV.setText(new StringBuilder("Date: ").append(apRequests.get(position).getDate()));
        holder.patientStatusAPTV.setText(new StringBuilder("Status: ").append(apRequests.get(position).getStatus()));
        updateUi(itemPosition, tmpHolder);

        holder.phoneApptIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPosition = itemPosition;

                if (checkPermission()) {
                    callPatient(itemPosition);
                }
            }
        });

        holder.cancelApptIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!apRequests.get(itemPosition).getStatus().equals(AFModel.cancel))
                    AppointmentController.CancelAppointmentRequest(apRequests.get(itemPosition).getPatient_id(), new IAction() {
                        @Override
                        public void onCompleteAction(Object object) {
                            updateUi(itemPosition, tmpHolder);
                        }
                    });
                else {
                    AppointmentController.DeleteAppointmentRequest(apRequests.get(itemPosition).getPatient_id(), new IAction() {
                        @Override
                        public void onCompleteAction(Object object) {
                            updateUi(itemPosition, tmpHolder);
                        }
                    });
                }
            }
        });

        holder.acceptApptIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!apRequests.get(itemPosition).getStatus().equals(AFModel.accept)) {
                    AppointmentController.AcceptAppointmentRequest(apRequests.get(itemPosition).getPatient_id(), new IAction() {
                        @Override
                        public void onCompleteAction(Object object) {
                            if ((boolean) object) {
                                updateUi(itemPosition, tmpHolder);
                            }
                        }
                    });
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.AlreadyRequestAccepted, Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        private TextView patientNameAPTV;
        private TextView patientDateAPTV;
        private TextView patientStatusAPTV;
        private ImageView phoneApptIV;
        private ImageView cancelApptIV;
        private ImageView acceptApptIV;

        AppointmentRecyclerViewHolder(View itemView) {
            super(itemView);

            patientNameAPTV = itemView.findViewById(R.id.patientNameAPTV);
            patientDateAPTV = itemView.findViewById(R.id.patientDateAPTV);
            patientStatusAPTV = itemView.findViewById(R.id.patientStatusAPTV);
            phoneApptIV = itemView.findViewById(R.id.phoneApptIV);
            cancelApptIV = itemView.findViewById(R.id.cancelApptIV);
            acceptApptIV = itemView.findViewById(R.id.acceptApptIV);
        }
    }
}
