package app.doctor.dmcx.app.da.project.doctorapp.Activities.Appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.Home.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Adapter.AppointmentSetupRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AppointmentController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAppointment;
import app.doctor.dmcx.app.da.project.doctorapp.Model.APDoctor;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Appointment;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ErrorText;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class SetupAppointmentActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, IAppointment {

    // Variables
    private Toolbar toolbar;
    private EditText timeToAPET;
    private EditText timeFromAPET;
    private Spinner toAmPmAPSP;
    private Spinner fromAmPmAPSP;
    private EditText daysAPET;
    private TextView satTV;
    private TextView sunTV;
    private TextView monTV;
    private TextView tueTV;
    private TextView wedTV;
    private TextView thuTV;
    private TextView friTV;
    private ImageButton addAppointmentIB;
    private RecyclerView appointmentDayTimeListAPRV;

    private AppointmentSetupRecyclerViewAdapter appointmentSetupRecyclerViewAdapter;
    private List<String> days;
    private List<Appointment> appointments;
    private Doctor doctor;
    // Variables

    // Methods
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        timeFromAPET = findViewById(R.id.timeFromAPET);
        timeToAPET = findViewById(R.id.timeToAPET);
        toAmPmAPSP = findViewById(R.id.toAmPmAPSP);
        fromAmPmAPSP = findViewById(R.id.fromAmPmAPSP);
        satTV = findViewById(R.id.satTV);
        sunTV = findViewById(R.id.sunTV);
        monTV = findViewById(R.id.monTV);
        tueTV = findViewById(R.id.tueTV);
        wedTV = findViewById(R.id.wedTV);
        thuTV = findViewById(R.id.thuTV);
        friTV = findViewById(R.id.friTV);
        daysAPET = findViewById(R.id.daysAPET);
        addAppointmentIB = findViewById(R.id.addAppointmentIB);
        appointmentDayTimeListAPRV = findViewById(R.id.appointmentDayTimeListAPRV);

        appointmentDayTimeListAPRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        appointmentDayTimeListAPRV.setHasFixedSize(true);

        days = new ArrayList<>();
        appointments = new ArrayList<>();
        doctor = new Doctor();

        appointmentSetupRecyclerViewAdapter = new AppointmentSetupRecyclerViewAdapter(this);
        appointmentDayTimeListAPRV.setAdapter(appointmentSetupRecyclerViewAdapter);

        ProfileController.CheckForProfileData(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object != null)
                    doctor = (Doctor) object;
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void event() {
        satTV.setOnClickListener(this);
        sunTV.setOnClickListener(this);
        monTV.setOnClickListener(this);
        tueTV.setOnClickListener(this);
        wedTV.setOnClickListener(this);
        thuTV.setOnClickListener(this);
        friTV.setOnClickListener(this);

        addAppointmentIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAppointmentSet();
            }
        });

        timeFromAPET.addTextChangedListener(this);
        timeToAPET.addTextChangedListener(this);
    }

    private void fillDays(String day) {
        StringBuilder daysBuilder = new StringBuilder();
        String sDay = day + " ";

        if (!days.contains(sDay)) {
            days.add(sDay);
        } else {
            days.remove(sDay);
        }

        for (String get : days) {
            daysBuilder.append(get);
        }

        daysBuilder.trimToSize();
        daysAPET.setText(daysBuilder);
    }

    private void addAppointmentSet() {
        final String timeTo = timeToAPET.getText().toString();
        final String timeFrom = timeFromAPET.getText().toString();
        final String toAmPm = toAmPmAPSP.getSelectedItem().toString();
        final String fromAmPm = fromAmPmAPSP.getSelectedItem().toString();

        final String days = daysAPET.getText().toString().trim();
        final String time = timeFrom + " " + fromAmPm + " - " + timeTo + " " + toAmPm;

        if (days.equals("") || timeFrom.equals("") || timeTo.equals("")) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.PleaseFillDaysAndTime, Toast.LENGTH_SHORT).show();
            return;
        }

        appointments.add(new Appointment(days, time));
        appointmentSetupRecyclerViewAdapter.setAppointments(appointments);
        appointmentSetupRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void onSave() {
        if (doctor != null) {
            Gson gson = new Gson();
            final String name = doctor.getName();
            final String phone = doctor.getPhone();
            final String specialist = doctor.getSpecialist();
            final String clinic = doctor.getChamber();
            final String appoinments = gson.toJson(appointments);
            final String email = Vars.appFirebase.getCurrentUser().getEmail();

            if (name.equals("") || phone.equals("") || specialist.equals("") || clinic.equals("") || appoinments.equals("[]")) {
                Toast.makeText(RefActivity.refACActivity.get(), ValidationText.PleaseFillAllTheFields, Toast.LENGTH_SHORT).show();
                return;
            }

            AppointmentController.SetupAppointmentDoctor(new APDoctor(name, specialist, phone, clinic, appoinments, email), new IAction() {
                @Override
                public void onCompleteAction(Object object) {
                    if ((boolean) object) {
//                        onBackPressed();
                    }
                }
            });
        } else {
            Toast.makeText(RefActivity.refACActivity.get(), ErrorText.FaileToLoadProfileDataTryAgain, Toast.LENGTH_SHORT).show();
        }
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_appointment);
        RefActivity.updateACActivity(this);

        init();
        setupToolbar();
        event();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appointment_setup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveASMI:
                onSave();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.satTV:
                fillDays("SAT");
                break;
            case R.id.sunTV:
                fillDays("SUN");
                break;
            case R.id.monTV:
                fillDays("MON");
                break;
            case R.id.tueTV:
                fillDays("TUE");
                break;
            case R.id.wedTV:
                fillDays("WED");
                break;
            case R.id.thuTV:
                fillDays("THU");
                break;
            case R.id.friTV:
                fillDays("FRI");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        appointmentDayTimeListAPRV = null;

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        RefActivity.updateACActivity(HomeActivity.instance.get());
        super.onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Pass
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!charSequence.toString().equals("")) {
            int value = Integer.valueOf(charSequence.toString());
            if (value < 1) {
                if (timeFromAPET.getText().toString().equals(charSequence.toString())) {
                    timeFromAPET.setText("1");
                } else if (timeToAPET.getText().toString().equals(charSequence.toString())) {
                    timeToAPET.setText("1");
                }
            } else if (value > 12) {
                if (timeFromAPET.getText().toString().equals(charSequence.toString())) {
                    timeToAPET.setText(new StringBuilder("12"));
                } else if (timeToAPET.getText().toString().equals(charSequence.toString())) {
                    timeToAPET.setText(new StringBuilder("12"));
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Pass
    }

    @Override
    public void delete(int position) {
        appointments.remove(position);
        appointmentSetupRecyclerViewAdapter.setAppointments(appointments);
        appointmentSetupRecyclerViewAdapter.notifyDataSetChanged();
    }
}
