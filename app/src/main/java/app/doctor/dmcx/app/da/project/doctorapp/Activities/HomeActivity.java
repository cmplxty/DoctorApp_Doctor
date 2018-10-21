package app.doctor.dmcx.app.da.project.doctorapp.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AppFirebase;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.AppFragmentManager;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.FragmentNames;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.AppointmentFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.DashboardFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.HomeServiceFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.IncomeFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.MessageUserListFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.PrescriptionPatientListFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.ProfileFragment;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LocalDB;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;


public class HomeActivity extends AppCompatActivity {

    // Variables
    public static HomeActivity instance;

    private DrawerLayout drawer_layout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private BottomNavigationViewEx mainBottomNavViewEx;
    // Variables

    // Methods
    private void init() {
        drawer_layout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);

        mainBottomNavViewEx = findViewById(R.id.mainBottomNavViewEx);
        mainBottomNavViewEx.enableAnimation(false);
        mainBottomNavViewEx.enableShiftingMode(false);
        mainBottomNavViewEx.enableItemShiftingMode(false);
        mainBottomNavViewEx.setTextSize(Vars.BNavBarProps.bottomNavBarTextSize);

        Vars.appFirebase = new AppFirebase();
        Vars.localDB = new LocalDB();
    }

    private void bottom_nav() {
        mainBottomNavViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profileBMI:
                        loadBottomNavFragment(getString(R.string.profile), new ProfileFragment(), FragmentNames.Profile, R.id.profileDMI);
                        break;
                    case R.id.dashboardBMI:
                        loadBottomNavFragment(getString(R.string.dashboard), new DashboardFragment(), FragmentNames.Dashboard, R.id.dashboardDMI);
                        break;
                    case R.id.incomeBMI:
                        loadBottomNavFragment(getString(R.string.income), new IncomeFragment(), FragmentNames.Income, R.id.incomeDMI);
                        break;
                }
                return false;
            }
        });
    }

    private void side_nav_header() {
        View nav_header = navigationView.getHeaderView(0);
        final ImageView pic = nav_header.findViewById(R.id.doctorHeaderPicIV);
        final TextView name = nav_header.findViewById(R.id.doctorHeaderNameTV);
        final TextView email = nav_header.findViewById(R.id.doctorHeaderEmailTV);

        ProfileController.LoadLocalProfile(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                Doctor doctor = (Doctor) object;

                if (doctor != null) {
                    Picasso.with(RefActivity.refACActivity.get()).load(doctor.getImage_link()).placeholder(R.drawable.noperson).into(pic);
                    name.setText(doctor.getName());
                    email.setText(doctor.getEmail());
                }
            }
        });
    }

    private void side_nav() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.dashboardDMI:
                        loadNavFragment(getString(R.string.dashboard), new DashboardFragment(), FragmentNames.Dashboard);
                        break;
                    case R.id.messagesDMI:
                        loadNavFragment(getString(R.string.message), new MessageUserListFragment(), FragmentNames.MessageUserList);
                        break;
                    case R.id.prescriptionPatientDMI:
                        loadNavFragment(getString(R.string.prescription), new PrescriptionPatientListFragment(), FragmentNames.PrescriptionPatient);
                        break;
                    case R.id.homeServiceDMI:
                        loadNavFragment(getString(R.string.home_service), new HomeServiceFragment(), FragmentNames.HomeService);
                        break;
                    case R.id.appointmentDMI:
                        loadNavFragment(getString(R.string.appointments), new AppointmentFragment(), FragmentNames.Appointment);
                        break;
                }

                navigationView.setCheckedItem(item.getItemId());
                drawer_layout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void loadToolbar() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(RefActivity.refACActivity.get(), drawer_layout, toolbar, R.string.open_navbar, R.string.close_navbar);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void onStartMethod() {
        toolbar.setTitle(getString(R.string.dashboard));
        AppFragmentManager.replace(RefActivity.refACActivity.get(), AppFragmentManager.homeFragmentContainer, new DashboardFragment(), FragmentNames.Dashboard);
        navigationView.setCheckedItem(R.id.dashboardDMI);
    }

    // Back Press Actions
    private void bp_LoadPatientPrescription() {
        loadNavFragment(getString(R.string.prescription), new PrescriptionPatientListFragment(), FragmentNames.PrescriptionPatient);
    }
    // Back Press Actions

    private void loadProfileData() {
        ProfileController.CheckForProfileData(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                if (object instanceof String) {
                    String errorCode = (String) object;
                    Toast.makeText(RefActivity.refACActivity.get(), errorCode, Toast.LENGTH_SHORT).show();
                } else {
                    side_nav_header();
                }
            }
        });
    }

    private void loadBottomNavFragment(String title, Fragment fragment, String tag, int id) {
        toolbar.setTitle(title);

        AppFragmentManager.replace(RefActivity.refACActivity.get(), AppFragmentManager.homeFragmentContainer, fragment, tag);
        navigationView.setCheckedItem(id);
    }

    private void loadNavFragment(String title, Fragment fragment, String tag) {
        toolbar.setTitle(title);
        AppFragmentManager.replace(RefActivity.refACActivity.get(), AppFragmentManager.homeFragmentContainer, fragment, tag);
    }
    // Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RefActivity.updateACActivity(this);
        instance = (HomeActivity) RefActivity.refACActivity.get();

        init();
        loadToolbar();
        side_nav();
        bottom_nav();
        loadProfileData();

        if (savedInstanceState == null)
            onStartMethod();
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else if (Vars.currentFragment.getTag().equals(FragmentNames.Prescription)) {
            bp_LoadPatientPrescription();
        } else if (!Vars.currentFragment.getTag().equals(FragmentNames.Dashboard)) {
            onStartMethod();
        } else
            super.onBackPressed();
    }

}
