package app.doctor.dmcx.app.da.project.doctorapp.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static String currentDate() {
        Date currentDate = new Date();
        return SimpleDateFormat.getDateInstance().format(currentDate);
    }

    public static String generateNumber() {
        return Math.abs(new Random().nextLong()) + UUID.randomUUID().toString();
    }

}
