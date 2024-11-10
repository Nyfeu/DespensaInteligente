package shared.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static Date parseString(String date) throws ParseException {

        return sdf.parse(date);

    }
    public static String parseDate(Date date) {

        return sdf.format(date);

    }

    public static String parseDateDB(Date date) {

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf2.format(date);

    }

}
