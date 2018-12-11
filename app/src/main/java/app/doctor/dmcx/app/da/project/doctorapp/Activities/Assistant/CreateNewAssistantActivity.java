package app.doctor.dmcx.app.da.project.doctorapp.Activities.Assistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Home.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AssistantController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Assistant;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.Validator;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class CreateNewAssistantActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText assistantNameNAET;
    private EditText assistantPhoneNAET;
    private EditText assistantAddressNAET;
    private Spinner assistantTypeNASNR;
    private EditText assistantEmailNAET;
    private EditText assistantPasswordNAET;
    private EditText doctorEmailNAET;
    private EditText doctorPasswordET;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        assistantNameNAET = findViewById(R.id.assistantNameNAET);
        assistantPhoneNAET = findViewById(R.id.assistantPhoneNAET);
        assistantAddressNAET = findViewById(R.id.assistantAddressNAET);
        assistantTypeNASNR = findViewById(R.id.assistantTypeNASNR);
        assistantEmailNAET = findViewById(R.id.assistantEmailNAET);
        assistantPasswordNAET = findViewById(R.id.assistantPasswordNAET);
        doctorEmailNAET = findViewById(R.id.doctorEmailNAET);
        doctorPasswordET = findViewById(R.id.doctorPasswordET);

        doctorEmailNAET.setText(Vars.appFirebase.getCurrentUser().getEmail());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void reset() {
        assistantNameNAET.setText("");
        assistantPhoneNAET.setText("");
        assistantAddressNAET.setText("");
        assistantEmailNAET.setText("");
        assistantPasswordNAET.setText("");
        doctorEmailNAET.setText("");
        doctorPasswordET.setText("");

        assistantTypeNASNR.setSelection(0);
    }

    private Map<String, String> getAssistantData() {
        String name = assistantNameNAET.getText().toString().trim();
        String phone = assistantPhoneNAET.getText().toString().trim();
        String address = assistantAddressNAET.getText().toString().trim();
        String email = assistantEmailNAET.getText().toString().trim();
        String password = assistantPasswordNAET.getText().toString().trim();
        String dEmail = doctorEmailNAET.getText().toString().trim();
        String dPassword = doctorPasswordET.getText().toString().trim();
        String type = String.valueOf(assistantTypeNASNR.getSelectedItem());

        if (
                Validator.empty(name) ||
                Validator.empty(email) ||
                Validator.empty(password) ||
                Validator.empty(dEmail) ||
                Validator.empty(dPassword)
            ) {

            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.RequiredFieldsAreNeedToFill, Toast.LENGTH_SHORT).show();
            return null;
        } else if (Validator.validEmail(email)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.ValidEmail, Toast.LENGTH_SHORT).show();
            return null;
        } else if (Validator.validPassword(password)) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.ValidPassword, Toast.LENGTH_SHORT).show();
            return null;
        }

        Map<String, String> map = new HashMap<>();
        map.put(AFModel.name, name);
        map.put(AFModel.email, email);
        map.put(AFModel.password, password);
        map.put(AFModel.doctor_email, dEmail);
        map.put(AFModel.doctor_password, dPassword);
        map.put(AFModel.phone, phone);
        map.put(AFModel.address, address);
        map.put(AFModel.type, type);

        return map;
    }

    private void saveAssistant() {
        if (getAssistantData() != null) {
            Map<String, String> map = getAssistantData();

            Assistant assistant = new Assistant();
            assistant.setName(map.get(AFModel.name));
            assistant.setAddress(map.get(AFModel.address));
            assistant.setType(map.get(AFModel.type));
            assistant.setEmail(map.get(AFModel.email));
            assistant.setPhone(map.get(AFModel.phone));

            AssistantController.CreateNewAssistant(
                    map.get(AFModel.email),
                    map.get(AFModel.password),
                    map.get(AFModel.doctor_email),
                    map.get(AFModel.doctor_password),
                    assistant,
                    new IAction() {
                        @Override
                        public void onCompleteAction(Object object) {
                            if ((Boolean) object) {
                                reset();
                            }
                        }
                    }
            );
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_assistant);
        RefActivity.updateACActivity(this);

        init();
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveMI: {
                saveAssistant();
                break;
            }
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
