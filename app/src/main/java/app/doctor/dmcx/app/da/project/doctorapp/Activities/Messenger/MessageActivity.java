package app.doctor.dmcx.app.da.project.doctorapp.Activities.Messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Vars.ActivityTrigger;
import app.doctor.dmcx.app.da.project.doctorapp.Activities.Home.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Adapter.MessageRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.MessageController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
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
    private EditText messageAreaAMET;
    private ImageButton prescriptionAMIB;
    private ImageButton sendAMIB;

    private Patient patient;
    private MessageRecyclerViewAdapter messageRecyclerViewAdapter;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        messageAreaAMET = findViewById(R.id.messageAreaAMET);
        prescriptionAMIB = findViewById(R.id.prescriptionAMIB);
        messagesAMRV = findViewById(R.id.messagesAMRV);
        sendAMIB = findViewById(R.id.sendAMIB);

        messageRecyclerViewAdapter = new MessageRecyclerViewAdapter();
        messagesAMRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        messagesAMRV.setHasFixedSize(true);
        messagesAMRV.setAdapter(messageRecyclerViewAdapter);

        patient = getIntent().getParcelableExtra(Vars.Connector.MESSAGE_ACTIVITY_DATA);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(patient.getName());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void event() {
        sendAMIB.setOnClickListener(new View.OnClickListener() {
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


        prescriptionAMIB.setOnClickListener(new View.OnClickListener() {
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

                if (object != null) {
                    List<Message> messages = new ArrayList<>();
                    for (Object message : (List<?>) object) {
                        messages.add((Message) message);
                    }

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
        return messageAreaAMET.getText().toString();
    }

    /*
     * Reset MessageUserList
     * */
    private void resetMessage() {
        messageAreaAMET.setText("");
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        RefActivity.updateACActivity(this);
        init();
        setupToolbar();
        event();
        loadMessages();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }
}
