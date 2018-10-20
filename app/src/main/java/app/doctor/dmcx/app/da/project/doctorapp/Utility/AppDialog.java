package app.doctor.dmcx.app.da.project.doctorapp.Utility;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import app.doctor.dmcx.app.da.project.doctorapp.Controller.HomeServiceController;
import app.doctor.dmcx.app.da.project.doctorapp.R;

public class AppDialog {

    public static class HomeServiceDialog {

        public static void register() {
            final View view = LayoutInflater.from(RefActivity.refACActivity.get()).inflate(R.layout.dialog_register_home_service, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(RefActivity.refACActivity.get());
            builder.setView(view);

            final AlertDialog dialog = builder.create();
            dialog.show();

            // View Elements
            final Button cancelHSBTN = view.findViewById(R.id.cancelHSIB);
            final Button registerHSBTN = view.findViewById(R.id.registerHSBTN);

            final EditText doctorNameDHSET = view.findViewById(R.id.doctorNameDHSET);
            final EditText doctorPhoneDHSET = view.findViewById(R.id.doctorPhoneDHSET);
            final EditText doctorSpecialistDHSET = view.findViewById(R.id.doctorSpecialistDHSET);
            final EditText doctorLocationDHSET = view.findViewById(R.id.doctorLocationDHSET);
            final Spinner doctorTimeDHSSP = view.findViewById(R.id.doctorTimeDHSSP);

            cancelHSBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            registerHSBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String name = doctorNameDHSET.getText().toString();
                    final String phone = doctorPhoneDHSET.getText().toString();
                    final String specialist = doctorSpecialistDHSET.getText().toString();
                    final String location = doctorLocationDHSET.getText().toString();
                    final String time = doctorTimeDHSSP.getSelectedItem().toString();

                    if (name.equals("") || phone.equals("") || specialist.equals("") || location.equals("") || time.equals("")) {
                        Toast.makeText(RefActivity.refACActivity.get(), ValidationText.InformationRequired, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    HomeServiceController.RegisterHomeService(name, location, phone, specialist, time);
                    dialog.dismiss();
                }
            });
            // View Elements
        }

    }

}
