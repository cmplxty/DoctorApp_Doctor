package app.doctor.dmcx.app.da.project.doctorapp.Activities.HomeService;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import app.doctor.dmcx.app.da.project.doctorapp.Controller.HomeServiceController;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.Model.HomeService;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class HomeServiceRegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView doctorNameDHSET;
    private TextView doctorPhoneDHSET;
    private TextView doctorSpecialistDHSET;
    private TextView doctorLocationDHSET;
    private Spinner doctorTimeDHSSP;

    private Doctor doctor;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        doctorNameDHSET = findViewById(R.id.doctorNameDHSET);
        doctorPhoneDHSET = findViewById(R.id.doctorPhoneDHSET);
        doctorSpecialistDHSET = findViewById(R.id.doctorSpecialistDHSET);
        doctorLocationDHSET = findViewById(R.id.doctorLocationDHSET);
        doctorTimeDHSSP = findViewById(R.id.doctorTimeDHSSP);

        doctor = getIntent().getParcelableExtra(Vars.Connector.HOME_SERVICE_REGISTER_ACTIVITY_DATA);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void updateUI() {
        doctorNameDHSET.setText(doctor.getName());
        doctorPhoneDHSET.setText(doctor.getPhone());
        doctorSpecialistDHSET.setText(doctor.getSpecialist());
    }

    private HomeService bindData() {
        HomeService homeService = new HomeService();
        homeService.setDoctor_name(doctorNameDHSET.getText().toString());
        homeService.setDoctor_phone(doctorPhoneDHSET.getText().toString());
        homeService.setDoctor_specialist(doctorSpecialistDHSET.getText().toString());
        homeService.setDoctor_location(doctorLocationDHSET.getText().toString());
        homeService.setDoctor_time(doctorTimeDHSSP.getSelectedItem().toString());
        return homeService;
    }

    private void registerHomeService() {
        HomeServiceController.RegisterHomeService(bindData());
        onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_service_registration);
        init();
        setupToolbar();
        updateUI();
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
                registerHomeService();
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
        super.onBackPressed();
    }
}
