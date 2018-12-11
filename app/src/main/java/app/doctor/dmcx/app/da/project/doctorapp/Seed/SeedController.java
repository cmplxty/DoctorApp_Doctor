package app.doctor.dmcx.app.da.project.doctorapp.Seed;

public class SeedController {

    public static void SeedAppointment() {
        Seeder.Instance().create().showToast().seedAppointment();
    }

}
