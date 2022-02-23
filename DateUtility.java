package invoices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {
    private DateUtility() {
    }

    public static String toString(Date date) {
        return dateFormat.format(date);
    }

    //It returns the actual date if the passed string is invalid
    public static Date fromString(String date) {
        try {
            return dateFormat.parse(date);
        }
        catch (ParseException e) {
            return new Date();
        }
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
}
