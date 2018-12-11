package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.PrescriptionController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Message;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Prescription;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.FormetterDateTime;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageRecyclerViewHolder> {

    private List<Message> messages = new ArrayList<>();
    private String patinetId, doctorId;

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setPatientAndDoctorId(String patinetId, String doctorId) {
        this.patinetId = patinetId;
        this.doctorId = doctorId;
    }

    @NonNull
    @Override
    public MessageRecyclerViewAdapter.MessageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_message, parent, false);
        return new MessageRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecyclerViewHolder holder, int position) {

        final int itemPosition = position;

        switch (messages.get(position).getType()) {
            case AFModel.image:
                holder.messageContent.setVisibility(View.GONE);
                holder.viewPrescriptionBTN.setVisibility(View.GONE);
                holder.messageImage.setVisibility(View.VISIBLE);

                Picasso.with(RefActivity.refACActivity.get()).load(messages.get(position).getContent()).placeholder(R.drawable.nophoto).into(holder.messageImage);
                break;
            case AFModel.text:
                holder.messageImage.setVisibility(View.GONE);
                holder.viewPrescriptionBTN.setVisibility(View.GONE);
                holder.messageContent.setVisibility(View.VISIBLE);

                holder.messageContent.setText(messages.get(position).getContent());
                break;
            case AFModel.prescription:
                holder.messageContent.setVisibility(View.GONE);
                holder.messageImage.setVisibility(View.GONE);
                holder.viewPrescriptionBTN.setVisibility(View.VISIBLE);

                holder.viewPrescriptionBTN.setText(new StringBuilder("View Prescription\n").append(FormetterDateTime.getDate(messages.get(position).getTimestamp())));

                holder.viewPrescriptionBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PrescriptionController.LoadSinglePrescription(patinetId, messages.get(itemPosition).getContent(), new IAction() {
                            @Override
                            public void onCompleteAction(Object object) {
                                if (object instanceof Prescription) {
                                    ActivityTrigger.PrescriptionActivity((Prescription) object);
                                }
                            }
                        });
                    }
                });
                break;
        }

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageParams.width = Math.round(RefActivity.refACActivity.get().getResources().getDisplayMetrics().density * 250);
        imageParams.height = Math.round(RefActivity.refACActivity.get().getResources().getDisplayMetrics().density * 250);

        LinearLayout.LayoutParams generalParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        generalParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;

        if (messages.get(position).getFrom().equals(Vars.appFirebase.getCurrentUser().getUid())) {
            imageParams.gravity = Gravity.END;
            generalParams.gravity = Gravity.END;

            holder.messageImage.setBackground(RefActivity.refACActivity.get().getDrawable(R.drawable.message_from));
            holder.messageImage.setLayoutParams(imageParams);

            holder.messageContent.setBackground(RefActivity.refACActivity.get().getDrawable(R.drawable.message_from));
            holder.messageContent.setTextColor(Color.BLACK);
            holder.messageContent.setLayoutParams(generalParams);
        } else {
            imageParams.gravity = Gravity.START;
            generalParams.gravity = Gravity.START;

            holder.messageImage.setBackground(RefActivity.refACActivity.get().getDrawable(R.drawable.message_to));
            holder.messageImage.setLayoutParams(imageParams);

            holder.messageContent.setBackground(RefActivity.refACActivity.get().getDrawable(R.drawable.message_to));
            holder.messageContent.setTextColor(Color.WHITE);
            holder.messageContent.setLayoutParams(generalParams);
        }

        holder.messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!messages.get(itemPosition).getContent().equals(AFModel.deflt))
                    ActivityTrigger.ViewImageActivity(messages.get(itemPosition).getContent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView messageContent;
        private ImageView messageImage;
        private Button viewPrescriptionBTN;

        MessageRecyclerViewHolder(View itemView) {
            super(itemView);

            messageContent = itemView.findViewById(R.id.messageContent);
            messageImage = itemView.findViewById(R.id.messageImage);
            viewPrescriptionBTN = itemView.findViewById(R.id.viewPrescriptionBTN);
        }
    }
}
