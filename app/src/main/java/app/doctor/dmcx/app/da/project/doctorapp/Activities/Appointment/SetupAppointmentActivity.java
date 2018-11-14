package app.doctor.dmcx.app.da.project.doctorapp.Activities.Appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Activities.HomeActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Adapter.AppointmentSetupRecyclerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AppointmentController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IAppointment;
import app.doctor.dmcx.app.da.project.doctorapp.Model.APDoctor;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Appointment;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class SetupAppointmentActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, IAppointment {

    // Variables
    private TextView doctorNameAPTV;
    private TextView doctorPhoneAPTV;
    private TextView doctorSpecialistAPTV;
    private TextView doctorClinicLocationAPTV;
    private EditText timeToAPET;
    private EditText timeFromAPET;
    private EditText doctorApptPasswordAPTV;
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
    private Button cancelAPBTN;
    private Button completeAPBTN;
    private ImageButton addAppointmentIB;
    private RecyclerView appointmentDayTimeListAPRV;

    private AppointmentSetupRecyclerViewAdapter appointmentSetupRecyclerViewAdapter;
    private List<String> days;
    private List<Appointment> appointments;
    // Variables

    // Methods
    private void init() {
        doctorNameAPTV = findViewById(R.id.doctorNameAPTV);
        doctorPhoneAPTV = findViewById(R.id.doctorPhoneAPTV);
        doctorSpecialistAPTV = findViewById(R.id.doctorApptPasswordAPTV);
        doctorClinicLocationAPTV = findViewById(R.id.doctorClinicLocationAPTV);
        doctorApptPasswordAPTV = findViewById(R.id.doctorApptPasswordAPTV);
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
        cancelAPBTN = findViewById(R.id.cancelAPBTN);
        completeAPBTN = findViewById(R.id.completeAPBTN);
        addAppointmentIB = findViewById(R.id.addAppointmentIB);
        appointmentDayTimeListAPRV = findViewById(R.id.appointmentDayTimeListAPRV);

        appointmentDayTimeListAPRV.setLayoutManager(new LinearLayoutManager(RefActivity.refACActivity.get()));
        appointmentDayTimeListAPRV.setHasFixedSize(true);

        days = new ArrayList<>();
        appointments = new ArrayList<>();

        appointmentSetupRecyclerViewAdapter = new AppointmentSetupRecyclerViewAdapter(this);
        appointmentDayTimeListAPRV.setAdapter(appointmentSetupRecyclerViewAdapter);
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

        cancelAPBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        completeAPBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onComplete();
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

        final String days = daysAPET.getText().toString();
        final String time = timeFrom + " " + fromAmPm + " - " + timeTo + " " + toAmPm;

        if (days.equals("") || timeFrom.equals("") || timeTo.equals("")) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.PleaseFillDaysAndTime, Toast.LENGTH_SHORT).show();
            return;
        }

        appointments.add(new Appointment(days, time));
        appointmentSetupRecyclerViewAdapter.setAppointments(appointments);
        appointmentSetupRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void onComplete() {
        Gson gson = new Gson();

        final String name = doctorNameAPTV.getText().toString();
        final String phone = doctorPhoneAPTV.getText().toString();
        final String specialist = doctorSpecialistAPTV.getText().toString();
        final String clinic = doctorClinicLocationAPTV.getText().toString();
        final String passcode = doctorApptPasswordAPTV.getText().toString();
        final String appoinments = gson.toJson(appointments);
        final String email = Vars.appFirebase.getCurrentUser().getEmail();

        if (name.equals("") || phone.equals("") || specialist.equals("") || clinic.equals("") || passcode.equals("") || appoinments.equals("[]")) {
            Toast.makeText(RefActivity.refACActivity.get(), ValidationText.PleaseFillAllTheFields, Toast.LENGTH_SHORT).show();
            return;
        }

        AppointmentController.SetupAppointmentDoctor(new APDoctor(name, specialist, phone, clinic, appoinments, email, passcode), new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if ((boolean) object) {
                    onBackPressed();
                }
            }
        });
    }
    // Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_appointment);
        RefActivity.updateACActivity(this);

        init();
        event();
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
                    timeFromAPET.setText("12");
                } else if (timeToAPET.getText().toString().equals(charSequence.toString())) {
                    timeToAPET.setText("12");
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
