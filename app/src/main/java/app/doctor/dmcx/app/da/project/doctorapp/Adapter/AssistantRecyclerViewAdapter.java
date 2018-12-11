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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AssistantController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.ICall;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Assistant;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;

public class AssistantRecyclerViewAdapter extends RecyclerView.Adapter<AssistantRecyclerViewAdapter.AssistantRecyclerViewHolder> implements ICall {

    private List<Assistant> assistants = new ArrayList<>();
    private int lastPosition = 0;

    public ICall getICallPatient() {
        return this;
    }

    public void setAssistants(List<Assistant> assistants) {
        this.assistants = assistants;
    }

    @NonNull
    @Override
    public AssistantRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.layout_rv_single_assistent, parent, false);
        return new AssistantRecyclerViewHolder(view);
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
        callPatient.setData(Uri.parse("tel:" + assistants.get(itemPosition).getPhone()));
        RefActivity.refACActivity.get().startActivity(callPatient);
    }

    @Override
    public void call() {
        callPatient(lastPosition);
    }

    @Override
    public void onBindViewHolder(@NonNull AssistantRecyclerViewHolder holder, int position) {
        final AssistantRecyclerViewHolder viewHolder = holder;
        final int itemPosition = position;

        if (!assistants.get(itemPosition).getImage_link().equals("")) {
            Picasso.with(RefActivity.refACActivity.get()).load(assistants.get(itemPosition).getImage_link()).placeholder(R.drawable.noperson).into(holder.profileImageSACIV);
        }

        holder.profileNameSATV.setText(assistants.get(itemPosition).getName());
        holder.profileTypeSATV.setText(assistants.get(itemPosition).getType());
        holder.profileEmailSATV.setText(assistants.get(itemPosition).getEmail());
        holder.profilePhoneSATV.setText(assistants.get(itemPosition).getPhone());
        holder.profileAddressSATV.setText(assistants.get(itemPosition).getAddress());
        holder.profileStatusSATV.setText(assistants.get(itemPosition).getStatus());

        holder.sendMailSAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] TO = {assistants.get(itemPosition).getEmail()};

                Intent emailIntent = new Intent();
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "A mail from your doctor");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                try {
                    RefActivity.refACActivity.get().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        holder.phoneCallSAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPosition = itemPosition;

                if (checkPermission()) {
                    callPatient(lastPosition);
                }
            }
        });

        holder.noteTogglerSAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.noteSAET.isEnabled())
                    viewHolder.noteSAET.setEnabled(false);
                else {
                    viewHolder.noteSAET.setEnabled(true);
                    viewHolder.noteSAET.requestFocus();
                }
            }
        });

        holder.deleteAccoutSAET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (assistants.get(itemPosition).getAuth_status() != null) {
                    if (assistants.get(itemPosition).getAuth_status().equals(AFModel.sign_out))
                        AssistantController.DeleteAssistant(assistants.get(itemPosition).getId());
                    else
                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.UserIsSignedInSignOutRequired, Toast.LENGTH_SHORT).show();
                } else {
                    AssistantController.DeleteAssistant(assistants.get(itemPosition).getId());
                }
            }
        });

        holder.sendNoteSAET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String note = viewHolder.noteSAET.getText().toString();

                if (note.equals("")) {
                    Toast.makeText(RefActivity.refACActivity.get(), ValidationText.PleaseWriteSomeNote, Toast.LENGTH_SHORT).show();
                    return;
                }

                AssistantController.SendNoteToAssistant(assistants.get(itemPosition).getId(), note);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assistants.size();
    }

    class AssistantRecyclerViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImageSACIV;
        private TextView profileNameSATV;
        private TextView profileTypeSATV;
        private TextView profileStatusSATV;
        private TextView profileEmailSATV;
        private TextView profilePhoneSATV;
        private TextView profileAddressSATV;
        private ImageView sendMailSAIV;
        private ImageView sendNoteSAET;
        private ImageView phoneCallSAIV;
        private EditText noteSAET;
        private ImageView noteTogglerSAIV;
        private Button deleteAccoutSAET;

        AssistantRecyclerViewHolder(View itemView) {
            super(itemView);

            profileImageSACIV = itemView.findViewById(R.id.profileImageSACIV);
            profileNameSATV = itemView.findViewById(R.id.profileNameSATV);
            profileTypeSATV = itemView.findViewById(R.id.profileTypeSATV);
            sendNoteSAET = itemView.findViewById(R.id.sendNoteSAET);
            profileStatusSATV = itemView.findViewById(R.id.profileStatusSATV);
            profileEmailSATV = itemView.findViewById(R.id.profileEmailSATV);
            profilePhoneSATV = itemView.findViewById(R.id.profilePhoneSATV);
            profileAddressSATV = itemView.findViewById(R.id.profileAddressSATV);
            sendMailSAIV = itemView.findViewById(R.id.sendMailSAIV);
            phoneCallSAIV = itemView.findViewById(R.id.phoneCallSAIV);
            noteSAET = itemView.findViewById(R.id.noteSAET);
            noteTogglerSAIV = itemView.findViewById(R.id.noteTogglerSAIV);
            deleteAccoutSAET = itemView.findViewById(R.id.deleteAccoutSAET);
        }
    }
}
