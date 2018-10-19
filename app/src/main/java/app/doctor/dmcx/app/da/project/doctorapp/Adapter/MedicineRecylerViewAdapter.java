package app.doctor.dmcx.app.da.project.doctorapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Interface.IMedicine;
import app.doctor.dmcx.app.da.project.doctorapp.Model.Medicine;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class MedicineRecylerViewAdapter extends RecyclerView.Adapter<MedicineRecylerViewAdapter.MedicineRecylerViewHolder> {

    private Context context;
    private List<Medicine> medicines = new ArrayList<>();
    private IMedicine iMedicine;

    public MedicineRecylerViewAdapter(Context context, IMedicine iMedicine) {
        this.context = context;
        this.iMedicine = iMedicine;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public MedicineRecylerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_single_prescription_medicine, parent, false);
        return new MedicineRecylerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineRecylerViewHolder holder, int position) {

        final int itemPosition = position;

        holder.medicineNameSMTV.setText(medicines.get(position).getName());
        holder.medicineQuantitySMTV.setText(new StringBuilder("Quantity: ").append(medicines.get(position).getQuantity()));
        holder.medicineDoseSMTV.setText(new StringBuilder("Dose: ").append(medicines.get(position).getDose()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iMedicine.onEdit(itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    class MedicineRecylerViewHolder extends RecyclerView.ViewHolder {

        private TextView medicineNameSMTV;
        private TextView medicineQuantitySMTV;
        private TextView medicineDoseSMTV;

        MedicineRecylerViewHolder(View itemView) {
            super(itemView);

            medicineNameSMTV = itemView.findViewById(R.id.medicineNameSMTV);
            medicineDoseSMTV = itemView.findViewById(R.id.medicineDoseSMTV);
            medicineQuantitySMTV = itemView.findViewById(R.id.medicineQuantitySMTV);
        }
    }
}
