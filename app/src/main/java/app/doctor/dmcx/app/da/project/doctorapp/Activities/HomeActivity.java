package app.doctor.dmcx.app.da.project.doctorapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;

import org.webrtc.sinch.VideoCapturerAndroid;

import java.lang.ref.WeakReference;
import java.util.Currency;
import java.util.Objects;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.AuthController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.HomeController;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.IAction;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AppFirebase;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.AppFragmentManager;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.FragmentNames;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.AppointmentFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.AudioCallFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.BlogFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.DashboardFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.HomeServiceFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.MessageUserListFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.PrescriptionPatientListFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.ProfileEditFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home.ProfileFragment;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.INavHeader;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LDBModel;
import app.doctor.dmcx.app.da.project.doctorapp.LocalDatabase.LocalDB;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Service.SinchService;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;


public class HomeActivity extends BaseActivity implements INavHeader, SinchService.StartFailedListener  {

    // Variables
    public static WeakReference<HomeActivity> instance;

    private ImageView navHeaderPic;
    private TextView navHeaderName;
    private TextView navHeaderEmail;

    private DrawerLayout drawer_layout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private NavBarHandler navBarHandler = new NavBarHandler();
    private SinchServiceHandler sinchServiceHandler = new SinchServiceHandler();
    private Doctor doctor;
    // Variables

    // Class
    private static class NavBarHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class NavBarRunnable implements Runnable {
        private MenuItem item;

        NavBarRunnable(MenuItem item) {
            this.item = item;
        }

        @Override
        public void run() {
            switch (item.getItemId()) {
                case R.id.dashboardDMI:
                    loadNavFragment(getString(R.string.dashboard), new DashboardFragment(), FragmentNames.Dashboard);
                    break;
                case R.id.profileDMI:
                    loadNavFragment(getString(R.string.profile), new ProfileFragment(), FragmentNames.Profile);
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
                case R.id.audioCallDMI:
                    loadNavFragment(getString(R.string.audio_call), new AudioCallFragment(), FragmentNames.AudioCall);
                    break;
                case R.id.blogDMI:
                    loadNavFragment(getString(R.string.blog), new BlogFragment(), FragmentNames.Blog);
                    break;
            }
        }
    }

    private class SinchServiceHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    }

    private class SinchServiceRunnable implements Runnable {
        @Override
        public void run() {
            startSinchService();
        }
    }
    // Class

    // Methods
    private boolean checkPermission() {
        String record_audio = Manifest.permission.RECORD_AUDIO;
        String modify_audio_settings = Manifest.permission.MODIFY_AUDIO_SETTINGS;
        String read_phone_state = Manifest.permission.READ_PHONE_STATE;
        if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), record_audio) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), modify_audio_settings) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), read_phone_state) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(RefActivity.refACActivity.get(), new String[] {record_audio, read_phone_state, modify_audio_settings}, Vars.RequestCode.REQUEST_FOR_ONLINE_AUDIO_CALL);
            return false;
        }

        return true;
    }

    @SuppressLint("HardwareIds")
    private void init() {
        drawer_layout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);

        Vars.appFirebase = new AppFirebase();
        Vars.localDB = new LocalDB();
        doctor = new Doctor();

        Vars.localDB.saveString(LDBModel.SAVE_DEVICE_ID, Settings.Secure.getString(RefActivity.refACActivity.get().getContentResolver(), Settings.Secure.ANDROID_ID));
        HomeController.UpdateTokenId();

        // Service
        sinchServiceHandler.postDelayed(new SinchServiceRunnable(), 1000);
    }

    private void startSinchService() {
        if (checkPermission())
            if (!getSinchServiceBinder().isStarted())
                getSinchServiceBinder().startClient();
    }

    private void load_side_nav_views() {
        View nav_header = navigationView.getHeaderView(0);
        navHeaderPic = nav_header.findViewById(R.id.doctorHeaderPicIV);
        navHeaderName = nav_header.findViewById(R.id.doctorHeaderNameTV);
        navHeaderEmail = nav_header.findViewById(R.id.doctorHeaderEmailTV);
    }

    private boolean check_side_nav_view() {
        return navHeaderPic != null && navHeaderName != null && navHeaderEmail != null;
    }

    private void side_nav_header() {
        load_side_nav_views();

        ProfileController.LoadLocalProfile(new IAction() {
            @Override
            public void onCompleteAction(Object object) {
                doctor = (Doctor) object;
                if (doctor != null) {
                    onNavHeaderUpdate(doctor);
                }
            }
        });
    }

    private void side_nav() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigationView.setCheckedItem(item.getItemId());
                drawer_layout.closeDrawer(GravityCompat.START);

                navBarHandler.postDelayed(new NavBarRunnable(item), 400);
                return true;
            }
        });
    }

    private void loadToolbar() {
        setSupportActionBar(toolbar);

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
        instance = new WeakReference<>(this);

        init();
        loadToolbar();
        side_nav();
        loadProfileData();

        if (savedInstanceState == null)
            onStartMethod();
    }

    @Override
    public void onNavHeaderUpdate(Doctor doctor) {
        if (check_side_nav_view()) {
            Picasso.with(RefActivity.refACActivity.get())
                    .load(doctor.getImage_link())
                    .placeholder(R.drawable.noperson).into(navHeaderPic);

            navHeaderName.setText(doctor.getName());
            navHeaderEmail.setText(doctor.getEmail());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Vars.currentFragment != null) {
            if (Objects.equals(Vars.currentFragment.getTag(), FragmentNames.Blog)) {
                getMenuInflater().inflate(R.menu.blog_menu, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editProfileMIT:
                Bundle bundle = new Bundle();
                bundle.putParcelable(Vars.Connector.PROFILE_EDIT_FRAGMENT_DATA, doctor);
                AppFragmentManager.replace(RefActivity.refACActivity.get(), AppFragmentManager.homeFragmentContainer, new ProfileEditFragment(), FragmentNames.ProfileEdit, bundle);
                break;
            case R.id.logoutMIT:
                AuthController.SignOut();
                Vars.localDB.clearLocalDB();
                ActivityTrigger.AuthActivity();
                break;
            case R.id.createBMI:
                ActivityTrigger.BlogEditorActivity(null);
                break;
            case R.id.myBlogsBMI:
                ActivityTrigger.MyBlogActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_FOR_ONLINE_AUDIO_CALL) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            } else {
                if (getSinchServiceBinder().isStarted())
                    getSinchServiceBinder().startClient();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else if (Vars.currentFragment != null) {
            if (Vars.currentFragment.getTag().equals(FragmentNames.Prescription)) {
                bp_LoadPatientPrescription();
            } else if (Vars.currentFragment.getTag().equals(FragmentNames.ProfileEdit)) {
                loadBottomNavFragment(getString(R.string.profile), new ProfileFragment(), FragmentNames.Profile, R.id.profileDMI);
            } else if (!Vars.currentFragment.getTag().equals(FragmentNames.Dashboard)) {
                onStartMethod();
            } else
                super.onBackPressed();
        }
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceBinder().setStartListener(this);
    }


    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        Toast.makeText(this, "Sinch Service Started", Toast.LENGTH_LONG).show();
    }
}
