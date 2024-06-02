package model.utils;

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

}
