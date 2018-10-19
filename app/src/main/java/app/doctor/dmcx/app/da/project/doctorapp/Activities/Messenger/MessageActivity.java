package app.doctor.dmcx.app.da.project.doctorapp.Activities.Messenger;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Adapter.MessageRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.MessageController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Message;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Patient;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Prescription;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class MessageActivity extends AppCompatActivity {

    // Variables
    private Toolbar toolbar;
    private RecyclerView messagesAMRV;
    private EditText messageAreaET;
    private ImageButton prescriptionMsgIB;
    private ImageButton sendMsgIB;

    private Patient patient;
    private MessageRecyclerViewAdapter messageRecyclerViewAdapter;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        messageAreaET = findViewById(R.id.messageAreaET);
        prescriptionMsgIB = findViewById(R.id.prescriptionMsgIB);
        sendMsgIB = findViewById(R.id.sendMsgIB);

        messageRecyclerViewAdapter = new MessageRecyclerViewAdapter();
        messagesAMRV = findViewById(R.id.messagesAMRV);
        messagesAMRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        messagesAMRV.setHasFixedSize(true);
        messagesAMRV.setAdapter(messageRecyclerViewAdapter);

        patient = getIntent().getParcelableExtra(Vars.Connector.MESSAGE_ACTIVITY_DATA);
    }

    private void initToolbar() {
        toolbar.setTitle(patient.getName());
    }

    private void event() {
        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getMessage().equals("")) {
                    MessageController.SendMessageText(getMessage(), patient.getId());
                    resetMessage();
                } else {
                    Toast.makeText(RefActivity.refACActivity.get(), ErrorText.EnterSomething, Toast.LENGTH_SHORT).show();
                }
            }
        });


        prescriptionMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String did = ProfileController.GetLocalProfile().getId();
                final String pid = patient.getId();
                final String dname = ProfileController.GetLocalProfile().getName();
                final String pname = patient.getName();
                final String phone = patient.getPhone();
                final String age = patient.getAge();
                final String address = patient.getAddress();

                @SuppressWarnings("simpledateformat")
                final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                final String timestamp = String.valueOf(System.currentTimeMillis());

                Prescription prescription = new Prescription(did, pid, dname, pname, phone, address, age, null, date, timestamp);
                ActivityTrigger.PrescriptionActivity(prescription);
            }
        });
    }

    private void loadMessages() {
        MessageController.LoadUserMessages(patient.getId(), new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object instanceof String) {
                    String errorCode = String.valueOf(object);
                    Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Message> messages = (List<Message>) object;
                if (messages != null) {
                    messageRecyclerViewAdapter.setPatientAndDoctorId(patient.getId(), Vars.appFirebase.getCurrentUser().getUid());
                    messageRecyclerViewAdapter.setMessages(messages);
                    messageRecyclerViewAdapter.notifyDataSetChanged();
                    messagesAMRV.scrollToPosition(messages.size() - 1);
                }
            }
        });
    }

    /*
     * Get MessageUserList
     * */
    private String getMessage() {
        return messageAreaET.getText().toString();
    }

    /*
     * Reset MessageUserList
     * */
    private void resetMessage() {
        messageAreaET.setText("");
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        RefActivity.updateACActivity(this);

        init();
        initToolbar();
        event();

        loadMessages();
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance);
        super.onBackPressed();
    }
}
