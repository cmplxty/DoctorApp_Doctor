package app.doctor.dmcx.app.da.project.doctorapp.Utility;

import android.app.AlertDialog;

import app.doctor.dmcx.app.da.project.doctorapp.Common.RefActivity;
import dmax.dialog.SpotsDialog;

public class LoadingDialog {

    private static AlertDialog spotDialog;

    public static void start(String message) {
        spotDialog = new SpotsDialog(RefActivity.refACActivity.get(), message);
        spotDialog.show();
    }

    public static void stop() {
        if (spotDialog != null)
            spotDialog.dismiss();

        spotDialog = null;
    }

}
