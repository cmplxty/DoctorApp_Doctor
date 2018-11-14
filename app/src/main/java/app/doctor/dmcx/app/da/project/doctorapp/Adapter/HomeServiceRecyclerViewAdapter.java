package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.HomeServiceController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICallPatient;
import app.doctor.dmcx.app.da.project.doctorapp.Model.HomeService;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class HomeServiceRecyclerViewAdapter extends RecyclerView.Adapter<HomeServiceRecyclerViewAdapter.HomeServiceRecyclerViewHolder> implements ICallPatient {

    private List<HomeService> homeServices = new ArrayList<>();
    private int lastPosition = 0;

    public ICallPatient getICallPatient() {
        return this;
    }

    public void setHomeServices(List<HomeService> homeServices) {
        this.homeServices = homeServices;
    }

    @NonNull
    @Override
    public HomeServiceRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_home_service_request, parent, false);
        return new HomeServiceRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeServiceRecyclerViewHolder holder, int position) {
        final int itemPosition = position;
        final String address = homeServices.get(position).getPatient_address() == null || homeServices.get(position).getPatient_address().equals(AFModel.deflt) ? "" : homeServices.get(position).getPatient_address();

        holder.patientNameHSTV.setText(homeServices.get(position).getPatient_name());
        holder.patientAddressHSTV.setText(new StringBuilder("Address: ").append(address));

        holder.phoneHSIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPosition = itemPosition;

                if (checkPermission()) {
                    callPatient(lastPosition);
                }
            }
        });

        holder.cancelHSIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeServiceController.CancelHomeServiceRequest(homeServices.get(itemPosition).getPatient_id());
            }
        });

        if (position == homeServices.size() - 1 && homeServices.size() != 1)
            holder.bottomLineHSRL.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return homeServices.size();
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Vars.currentFragment.requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, Vars.RequestCode.REQUEST_CALL_CODE_HS);
            return false;
        }
        return true;
    }

    private void callPatient(int itemPosition) {
        Intent callPatient = new Intent();
        callPatient.setAction(Intent.ACTION_CALL);
        callPatient.setData(Uri.parse("tel:" + homeServices.get(itemPosition).getPatient_phone()));
        RefActivity.refACActivity.get().startActivity(callPatient);
    }

    @Override
    public void call() {
        callPatient(lastPosition);
    }

    class HomeServiceRecyclerViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout bottomLineHSRL;
        private TextView patientNameHSTV;
        private TextView patientAddressHSTV;
        private ImageButton phoneHSIB;
        private ImageButton cancelHSIB;

        HomeServiceRecyclerViewHolder(View itemView) {
            super(itemView);

            bottomLineHSRL = itemView.findViewById(R.id.bottomLineHSRL);
            patientNameHSTV = itemView.findViewById(R.id.patientNameHSTV);
            patientAddressHSTV = itemView.findViewById(R.id.patientAddressHSTV);
            phoneHSIB = itemView.findViewById(R.id.phoneHSIB);
            cancelHSIB = itemView.findViewById(R.id.cancelHSIB);
        }
    }


}
