package app.doctor.dmcx.app.da.project.doctorapp.Activities.Common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Adapter.MedicineRecylerViewAdapter;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.PrescriptionController;
import app.doctor.dmcx.app.da.project.doctorapp.Firebase.AFModel;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IMedicine;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Medicine;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Prescription;
import app.doctor.dmcx.app.da.project.doctorapp.R;
import app.doctor.dmcx.app.da.project.doctorapp.Utility.ValidationText;
import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class PrescriptionActivity extends AppCompatActivity implements IMedicine {

    // Variables
    private NestedScrollView newMedicineNSV;
    private RecyclerView medicineListRV;
    private EditText medicineNameET;
    private Spinner medicineDoseSP;
    private EditText medicineQuantityET;
    private TextView doctorsNamePTV;
    private TextView patientNamePTV;
    private TextView prescriptionDatePTV;
    private TextView patientPhonePTV;
    private TextView patientAgePTV;
    private TextView patientAddressPTV;
    private ImageButton addMedicinePIB;
    private Button closePBTN;
    private Button sendPBTN;

    private BottomSheetBehavior bottomSheetBehavior;
    private MedicineRecylerViewAdapter medicineRecylerViewAdapter;
    private List<Medicine> medicines;
    private Prescription prescription;
    private int editingPosition;
    private boolean isViewPrescription = false;
    // Variables

    // Methods
    private void init() {
        medicineNameET = findViewById(R.id.medicineNameET);
        medicineDoseSP = findViewById(R.id.medicineDoseSP);
        medicineQuantityET = findViewById(R.id.medicineQuantityET);
        doctorsNamePTV = findViewById(R.id.doctorsNamePTV);
        patientNamePTV = findViewById(R.id.patientNamePTV);
        prescriptionDatePTV = findViewById(R.id.prescriptionDatePTV);
        patientPhonePTV = findViewById(R.id.patientPhonePTV);
        patientAgePTV = findViewById(R.id.patientAgePTV);
        patientAddressPTV = findViewById(R.id.patientAddressPTV);
        addMedicinePIB = findViewById(R.id.addMedicinePIB);
        closePBTN = findViewById(R.id.closePBTN);
        sendPBTN = findViewById(R.id.sendPBTN);

        medicineListRV = findViewById(R.id.medicineListRV);
        medicineListRV.setHasFixedSize(true);
        medicineListRV.setLayoutManager(new LinearLayoutManager(this));
        medicineRecylerViewAdapter = new MedicineRecylerViewAdapter(this, this);
        medicineListRV.setAdapter(medicineRecylerViewAdapter);

        newMedicineNSV = findViewById(R.id.newMedicineNSV);
        bottomSheetBehavior = BottomSheetBehavior.from(newMedicineNSV);

        medicines = new ArrayList<>();
        prescription = getIntent().getParcelableExtra(Vars.Connector.PERSCRIPTION_ACTIVITY_DATA);
        editingPosition = -1;
        isViewPrescription = prescription != null && prescription.getMedicines() != null;

        if (prescription == null) return;

        doctorsNamePTV.setText(prescription.getDoctor_name());
        patientNamePTV.setText(new StringBuilder("Name: ").append(prescription.getPatient_name()));
        patientPhonePTV.setText(new StringBuilder("Phone: ").append(prescription.getPatient_phone() == null || prescription.getPatient_phone().equals(AFModel.deflt) ? "NaN" : prescription.getPatient_phone()));
        patientAgePTV.setText(new StringBuilder("Age: ").append(prescription.getPatient_age() == null || prescription.getPatient_age().equals(AFModel.deflt) ? "NaN" : prescription.getPatient_age()));
        patientAddressPTV.setText(new StringBuilder("Address: ").append(prescription.getPatient_address() == null || prescription.getPatient_address().equals(AFModel.deflt) ? "NaN" : prescription.getPatient_address()));
        prescriptionDatePTV.setText(new StringBuilder("Date: ").append(prescription.getDate()));
    }

    private void event() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    addMedicinePIB.setImageResource(R.drawable.add_cyan);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        loadMedicineIfNonNull(prescription.getMedicines());
        viewOlderPrescription();
    }

    private void viewOlderPrescription() {
        if (isViewPrescription) {
            addMedicinePIB.setVisibility(View.GONE);
            sendPBTN.setVisibility(View.GONE);
            closePBTN.setVisibility(View.GONE);
        }
    }

    private void loadMedicineIfNonNull(String medicine) {
        if (medicine != null) {
            Gson gson = new Gson();
            medicines = gson.fromJson(medicine, new TypeToken<List<Medicine>>(){}.getType());

            medicineRecylerViewAdapter.setMedicines(medicines);
            medicineRecylerViewAdapter.notifyDataSetChanged();
        }
    }

    private void reset() {
        medicineNameET.setText("");
        medicineDoseSP.setSelection(0);
        medicineQuantityET.setText("");

        editingPosition = -1;

        medicineNameET.requestFocus();
        medicineQuantityET.clearFocus();
    }
    // Methods


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        init();
        event();
    }

    public void onAddMedicine(View view) {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            ((ImageButton) view).setImageResource(R.drawable.cross_cyan);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            ((ImageButton) view).setImageResource(R.drawable.add_cyan);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void onCancelPrescription(View view) {
        finish();
    }

    public void onSendPrescription(View view) {
        if (medicines.size() <= 0) {
            Toast.makeText(this, ValidationText.NoMedicineSet, Toast.LENGTH_LONG).show();
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(medicines);
        prescription.setMedicines(json);

        PrescriptionController.SendPrescription(prescription);
        finish();
    }

    public void onAddOrUpdateClick(View view) {
        final String name = medicineNameET.getText().toString();
        final String dose = medicineDoseSP.getSelectedItem().toString();
        final String quantity = medicineQuantityET.getText().toString();

        if (name.equals("") || quantity.equals("")) {
            return;
        }

        if (editingPosition == -1) {
            medicines.add(new Medicine(name, dose, quantity));
        } else {
            medicines.set(editingPosition, new Medicine(name, dose, quantity));
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        medicineRecylerViewAdapter.setMedicines(medicines);
        medicineRecylerViewAdapter.notifyDataSetChanged();

        reset();
    }

    @Override
    public void onEdit(int position) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        List<String> dose = Arrays.asList(getResources().getStringArray(R.array.per_day_medicine));

        medicineNameET.setText(medicines.get(position).getName());
        medicineDoseSP.setSelection(dose.indexOf(medicines.get(position).getDose()));
        medicineQuantityET.setText(medicines.get(position).getQuantity());

        medicineNameET.clearFocus();
        medicineQuantityET.clearFocus();
        editingPosition = position;
    }

    @Override
    public void onBackPressed() {
        if (isViewPrescription) {
            super.onBackPressed();
            return;
        }

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
