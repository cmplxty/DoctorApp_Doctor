package app.doctor.dmcx.app.da.project.doctorapp.Controller;

import app.doctor.dmcx.app.da.project.doctorapp.Variables.Vars;

public class HomeController {

    public static void UpdateTokenId() {
        Vars.appFirebase.setTokenId();
    }

}
