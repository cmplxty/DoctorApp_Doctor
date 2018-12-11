package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AudioCallController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Model.AudioCallHistory;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;

public class AudioCallHistoryRecyclerViewAdapter extends RecyclerView.Adapter<AudioCallHistoryRecyclerViewAdapter.AudioCallHistoryRecyclerViewHolder> {

    private List<AudioCallHistory> histories = new ArrayList<>();

    public void setHistories(List<AudioCallHistory> histories) {
        this.histories = histories;
    }

    private class HistoryDialog {
        private String[] options = new String[] {"Message", "Remove"};
        private Context context;
        private int position;

        private HistoryDialog(Context context, int position) {
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
                        ActivityTrigger.MessageActivity((Patient) histories.get(position).getUser());
                    } else if (selectedPosition == 1) {
                        AudioCallController.DeleteCurrentHistory(histories.get(position).getId());
                    } else {
                        Log.d(Vars.TAG, "Dialog: Unknown Call");
                    }
                }
            });

            builder.show();
        }
    }

    @NonNull
    @Override
    public AudioCallHistoryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_audio_call_history, parent, false);
        return new AudioCallHistoryRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioCallHistoryRecyclerViewHolder holder, int position) {
        final int itemPosition = position;

        if (histories.size() > 1 && position == 0) {
            holder.relativeLayoutLineRL.setVisibility(View.INVISIBLE);
        }

        if (histories.get(position).getCall_status().equals(AFModel.missed)) {
            holder.historyUserCallStatusHIV.setImageResource(R.drawable.missed_call_red_10);
        } else if (histories.get(position).getCall_status().equals(AFModel.received)) {
            holder.historyUserCallStatusHIV.setImageResource(R.drawable.received_call_green_10);
        }

        if (histories.get(position).getUser() instanceof Patient) {
            Patient patient = (Patient) histories.get(position).getUser();
            if (patient != null) {
                if (!patient.getLink().equals("")) {
                    holder.historyUserNameHTV.setText(patient.getName());
                    Picasso.with(RefActivity.refACActivity.get()).load(patient.getLink())
                            .placeholder(R.drawable.noperson)
                            .into(holder.historyCallerImageHCIV);
                }
            }
        }

        holder.historyCallDateHTV.setText(histories.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryDialog dialog = new HistoryDialog(RefActivity.refACActivity.get(), itemPosition);
                dialog.create();
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class AudioCallHistoryRecyclerViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayoutLineRL;
        private CircleImageView historyCallerImageHCIV;
        private TextView historyUserNameHTV;
        private ImageView historyUserCallStatusHIV;
        private TextView historyCallDateHTV;

        private AudioCallHistoryRecyclerViewHolder(View itemView) {
            super(itemView);

            relativeLayoutLineRL = itemView.findViewById(R.id.relativeLayoutLineRL);
            historyCallerImageHCIV = itemView.findViewById(R.id.historyCallerImageHCIV);
            historyUserNameHTV = itemView.findViewById(R.id.historyUserNameHTV);
            historyUserCallStatusHIV = itemView.findViewById(R.id.historyUserCallStatusHIV);
            historyCallDateHTV = itemView.findViewById(R.id.historyCallDateHTV);
        }
    }
}
