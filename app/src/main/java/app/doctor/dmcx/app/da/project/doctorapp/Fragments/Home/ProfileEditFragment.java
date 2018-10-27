package app.doctor.dmcx.app.da.project.doctorapp.Fragments.Home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.ProfileController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Doctor;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditFragment extends Fragment {

    // Variables
    private CircleImageView profileImagePECIV;
    private FloatingActionButton changeProfileImagePEFAB;
    private EditText profileNamePEET;
    private EditText profilePhonePEET;
    private EditText profileSpecialistPEET;
    private EditText profileChamberPEET;
    private EditText profileHospitalPEET;
    private EditText profileDegreePEET;
    private EditText profileRegistrationCodePEET;
    private EditText profileCityPEET;
    private EditText profileCountryPEET;
    private EditText profileAboutPEET;
    private Button savePEBTN;

    private Doctor doctor;
    private Uri profileImageUri = null;
    // Variables

    // Methods
    private void init(View view) {
        profileImagePECIV = view.findViewById(R.id.profileImagePECIV);
        changeProfileImagePEFAB = view.findViewById(R.id.changeProfileImagePEFAB);
        profileNamePEET = view.findViewById(R.id.profileNamePEET);
        profilePhonePEET = view.findViewById(R.id.profilePhonePEET);
        profileSpecialistPEET = view.findViewById(R.id.profileSpecialistPEET);
        profileChamberPEET = view.findViewById(R.id.profileChamberPEET);
        profileHospitalPEET = view.findViewById(R.id.profileHospitalPEET);
        profileDegreePEET = view.findViewById(R.id.profileDegreePEET);
        profileRegistrationCodePEET = view.findViewById(R.id.profileRegistrationCodePEET);
        profileCityPEET = view.findViewById(R.id.profileCityPEET);
        profileCountryPEET = view.findViewById(R.id.profileCountryPEET);
        profileAboutPEET = view.findViewById(R.id.profileAboutPEET);
        savePEBTN = view.findViewById(R.id.savePEBTN);

        if (getArguments() != null) {
            doctor = getArguments().getParcelable(Vars.Connector.PROFILE_EDIT_FRAGMENT_DATA);
            if (doctor != null) {
                Picasso.with(RefActivity.refACActivity.get()).load(doctor.getImage_link()).placeholder(R.drawable.noperson).into(profileImagePECIV);

                profileNamePEET.setText(doctor.getName().equals("") || doctor.getName().equals(AFModel.deflt) ? "" : doctor.getName());
                profilePhonePEET.setText(doctor.getPhone().equals("") || doctor.getPhone().equals(AFModel.deflt) ? "" : doctor.getPhone());
                profileSpecialistPEET.setText(doctor.getSpecialist().equals("") || doctor.getSpecialist().equals(AFModel.deflt) ? "" : doctor.getSpecialist());
                profileChamberPEET.setText(doctor.getChamber().equals("") || doctor.getChamber().equals(AFModel.deflt) ? "" : doctor.getChamber());
                profileHospitalPEET.setText(doctor.getHospital().equals("") || doctor.getHospital().equals(AFModel.deflt) ? "" : doctor.getHospital());
                profileDegreePEET.setText(doctor.getDegree().equals("") || doctor.getDegree().equals(AFModel.deflt) ? "" : doctor.getDegree());
                profileRegistrationCodePEET.setText(doctor.getRegistration().equals("") || doctor.getRegistration().equals(AFModel.deflt) ? "" : doctor.getRegistration());
                profileCityPEET.setText(doctor.getCity().equals("") || doctor.getCity().equals(AFModel.deflt) ? "" : doctor.getCity());
                profileCountryPEET.setText(doctor.getCountry().equals("") || doctor.getCountry().equals(AFModel.deflt) ? "" : doctor.getCountry());
                profileAboutPEET.setText(doctor.getAbout().equals("") || doctor.getAbout().equals(AFModel.deflt) ? "" : doctor.getAbout());
            }
        }
    }

    private void event() {
        changeProfileImagePEFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    cropImager();
                }
            }
        });

        savePEBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctor.setName(profileNamePEET.getText().toString());
                doctor.setAbout(profileAboutPEET.getText().toString());
                doctor.setChamber(profileChamberPEET.getText().toString());
                doctor.setCity(profileCityPEET.getText().toString());
                doctor.setCountry(profileCountryPEET.getText().toString());
                doctor.setDegree(profileDegreePEET.getText().toString());
                doctor.setHospital(profileHospitalPEET.getText().toString());
                doctor.setPhone(profilePhonePEET.getText().toString());
                doctor.setRegistration(profileRegistrationCodePEET.getText().toString());
                doctor.setSpecialist(profileSpecialistPEET.getText().toString());

                ProfileController.UpdateProfileDetail(doctor, profileImageUri);
            }
        });
    }

    private void cropImager() {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .setMaxCropResultSize(1500, 1500)
                .start(RefActivity.refACActivity.get(), this);
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(RefActivity.refACActivity.get(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA }, Vars.RequestCode.REQUEST_ACCESS_IMAGE_CODE_PEF);
            return false;
        }

        return true;
    }
    // Methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_profile_edit, container, false);
        init(view);
        event();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (result != null) {
                profileImageUri = result.getUri();
                profileImagePECIV.setImageURI(profileImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                if (result != null) {
                    Exception error = result.getError();
                    Toast.makeText(RefActivity.refACActivity.get(), "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Vars.RequestCode.REQUEST_ACCESS_IMAGE_CODE_PEF) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
                builder.setTitle("Permission")
                        .setCancelable(false)
                        .setMessage("Need permission to get the image from the storage. We don't access any of your private data. Be safe stay safe.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkPermission();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                cropImager();
            }
        }
    }
}
