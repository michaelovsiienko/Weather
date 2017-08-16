package androidprojectsw.com.weather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {
    public static String getFormattedTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
