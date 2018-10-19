package app.doctor.dmcx.app.da.project.doctorapp.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FormetterDateTime {

    public static String getDate(String timestamp) {
        DateFormat simpleDateFormat = SimpleDateFormat.getDateInstance();
        return simpleDateFormat.format(new Date(Long.valueOf(timestamp)));
    }

}
