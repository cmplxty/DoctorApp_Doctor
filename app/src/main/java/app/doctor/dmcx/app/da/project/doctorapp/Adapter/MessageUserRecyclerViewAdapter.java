package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.MessageController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Model.MessageUser;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class MessageUserRecyclerViewAdapter extends RecyclerView.Adapter<MessageUserRecyclerViewAdapter.MessageUserRecyclerViewHolder> {

    private List<MessageUser> messageUsers = new ArrayList<>();

    public void setMessageUsers(List<MessageUser> messageUsers) {
        this.messageUsers = messageUsers;
    }

    @NonNull
    @Override
    public MessageUserRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_message_list_item, parent, false);
        return new MessageUserRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageUserRecyclerViewHolder holder, int position) {

        final int itemPosition = position;
        final MessageUserRecyclerViewHolder viewHolder = holder;

        MessageController.LoadMessageUserContent(messageUsers.get(itemPosition).getPatient(), new IAction() {
            @Override
            public void onCompleteAction(Object object) {

                if (object instanceof Patient) {
                    final Patient patient = (Patient) object;
                    Picasso.with(RefActivity.refACActivity.get())
                            .load(patient.getLink() == null ? AFModel.deflt : patient.getLink())
                            .placeholder(R.drawable.noperson)
                            .into(viewHolder.userImageIV);

                    viewHolder.fromTV.setText(patient.getName());
                    if (messageUsers.get(itemPosition).getType().equals(AFModel.text)) {
                        viewHolder.messageContentTV.setText(messageUsers.get(itemPosition).getContent());
                    } else if (messageUsers.get(itemPosition).getType().equals(AFModel.image)) {
                        viewHolder.messageContentTV.setText(new StringBuilder("Photo"));
                    } else {
                        viewHolder.messageContentTV.setText(new StringBuilder("Prescription"));
                    }
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityTrigger.MessageActivity(patient);
                        }
                    });

                } else {
                    String errorCode = String.valueOf(object);
                    Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return messageUsers.size();
    }

    class MessageUserRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImageIV;
        private TextView fromTV;
        private TextView messageContentTV;

        MessageUserRecyclerViewHolder(View itemView) {
            super(itemView);

            userImageIV = itemView.findViewById(R.id.userImageIV);
            fromTV = itemView.findViewById(R.id.fromTV);
            messageContentTV = itemView.findViewById(R.id.messageContentTV);
        }
    }
}
