package parsso.idman.Helpers;


import parsso.idman.Utils.Convertor.DateConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

public class TimeHelper {

    static ZoneId zoneId = ZoneId.of(Variables.ZONE);

    public static String getExportEndTime(String input) {

        Instant instant = Instant.now(); //can be LocalDateTime


        String Y = input.substring(0, 4);
        String M = input.substring(4, 6);
        String D = input.substring(6, 8);

        String H = input.substring(8, 10);
        String m = input.substring(10, 12);

        ZonedDateTime eventDate = OffsetDateTime.parse(Y + "-" + M + "-" + D + 'T' + H + ":" + m + ":00" + ".000Z").toZonedDateTime();

        parsso.idman.Models.Time time = longToPersianTime(eventDate.toInstant().toEpochMilli());

        return time.getYear() + "-"
                + String.format("%02d", time.getMonth()) + "-"
                + String.format("%02d", time.getDay()) + " "
                + String.format("%02d", time.getHours()) + ":"
                + String.format("%02d", time.getMinutes());
    }

    public static String getCurrentTimeStampOffset() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
        Date now = new Date();
        String strDate = sdfDate.format(now);

        Instant instant = Instant.now(); //can be LocalDateTime
        ZoneId systemZone = ZoneId.of(Variables.ZONE); // my timezone
        ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(instant);


        return strDate + currentOffsetForMyZone.toString().replaceAll(":", "");
    }

    public static String epochToDateLdapFormat(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss.SSS");

        String strDate = formatter.format(new Date(timeInMilliseconds));

        Instant instant = Instant.now(); //can be LocalDateTime
        ZoneId systemZone = ZoneId.of(Variables.ZONE); // my timezone
        ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(instant);

        return strDate + currentOffsetForMyZone.toString().replaceAll(":", "");
    }

    public static long[] specificDateToEpochRange(parsso.idman.Models.Time time, ZoneId zoneId) throws ParseException {

        long[] result = new long[2];

        LocalDate day = LocalDate.of(time.getYear(), time.getMonth(), time.getDay());

        result[0] = day.atStartOfDay().atZone(zoneId).toEpochSecond() * 1000;
        result[1] = day.plusDays(1).atStartOfDay().atZone(zoneId).toEpochSecond() * 1000;

        return result;

    }

    public static String setEndTime(String input) {
        //if is jalali
        if (Integer.valueOf(input.substring(0, 4)) < 2000) {

            if (!(input.contains("-")) && !(input.contains("/"))) {
                String Y = input.substring(0, 4);
                String M = input.substring(4, 6);
                String D = input.substring(6, 8);

                String H = input.substring(8, 10);
                String m = input.substring(10, 12);
                String s = input.substring(12, 14);
                String S = input.substring(15, 15);

                ZonedDateTime eventDate = OffsetDateTime.parse(Y + "-" + M + "-" + D + 'T' + H + ":" + m + ":" + s + "." + S).atZoneSameInstant(zoneId);

                return eventDate.getYear()
                        + String.format("%02d", eventDate.getMonth())
                        + String.format("%02d", eventDate.getDayOfMonth())
                        + String.format("%02d", eventDate.getHour())
                        + String.format("%02d", eventDate.getMinute())
                        + String.format("%02d", eventDate.getSecond())
                        + String.format("%03d", eventDate.getNano());
            } else if (input.contains("/")) {
                String Y = input.substring(0, 4);
                String M = input.substring(5, 7);
                String D = input.substring(8, 10);

                String H;
                String m;
                String s;
                String S;

                try {
                    H = input.substring(11, 13);
                    m = input.substring(14, 16);
                    s = input.substring(17, 19);
                    S = input.substring(20, 22);
                } catch (Exception e) {
                    H = "23";
                    m = "59";
                    s = "59";
                    S = "99";

                }

                String date = convertDateJalaliToGeorgian(Integer.valueOf(Y), Integer.valueOf(M), Integer.valueOf(D));

                Instant instant = Instant.now(); //can be LocalDateTime
                ZoneId systemZone = ZoneId.of(Variables.ZONE); // my timezone
                ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(instant);


                ZonedDateTime eventDate = OffsetDateTime.parse(date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + 'T' + H + ":" + m + ":" + s + "." + S + currentOffsetForMyZone).atZoneSameInstant(zoneId);

                return eventDate.getYear()
                        + String.format("%02d", eventDate.getMonthValue())
                        + String.format("%02d", eventDate.getDayOfMonth())
                        + String.format("%02d", eventDate.getHour())
                        + String.format("%02d", eventDate.getMinute())
                        + String.format("%02d", eventDate.getSecond());
            } else {
                String temp = convertDateTimeJalali(input);

                if (temp.contains("-")) {
                    temp = convertDateToNumber(input);


                }

                return temp;
            }

            //if is G
        } else {

            if (input.contains("-")) {
                return convertDateToNumber(input);
            }

            return input;

        }

    }

    public static String convertDateTimeJalali(String seTime) {

        parsso.idman.Models.Time timeObject = stringToTime(seTime);


        String date = convertDateJalaliToGeorgian(Integer.valueOf(timeObject.getYear()), Integer.valueOf(timeObject.getMonth()), Integer.valueOf(timeObject.getDay()));

        String time = String.format("%02d", Integer.valueOf(timeObject.getHours())) +
                String.format("%02d", Integer.valueOf(timeObject.getMinutes())) +
                String.format("%02d", Integer.valueOf(timeObject.getSeconds())) + "." +
                String.format("%03d", Integer.valueOf(timeObject.getMilliSeconds()));

        return date + time;

    }

    public static String convertDateJalaliToGeorgian(int Y, int M, int D) {

        DateConverter dateConverter = new DateConverter();
        dateConverter.persianToGregorian(Y, M, D);

        return dateConverter.getYear() + String.format("%02d", dateConverter.getMonth()) + String.format("%02d", dateConverter.getDay());
    }

    public static String convertDateToNumber(String seTime) {
        parsso.idman.Models.Time timeObject = stringToTime(seTime);

        Instant instant = Instant.now(); //can be LocalDateTime
        ZoneId systemZone = ZoneId.of(Variables.ZONE); // my timezone
        ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(instant);

        String time = String.format("%02d", Integer.valueOf(timeObject.getHours())) +
                String.format("%02d", Integer.valueOf(timeObject.getMinutes())) +
                String.format("%02d", Integer.valueOf(timeObject.getSeconds())) + "." +
                String.format("%03d", Integer.valueOf(timeObject.getMilliSeconds())) +
                currentOffsetForMyZone.toString().replaceAll(":","");

        return timeObject.getYear() + timeObject.getMonth() + timeObject.getDay() + time;
    }

    public static Date convertEpochToDate(long epoch) {
        return new Date(epoch);
    }

    public static parsso.idman.Models.Time longToPersianTime(Long in) {
        Instant instant = Instant.ofEpochMilli(in);
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(instant, zoneId);

        DateConverter dc = new DateConverter();

        dc.gregorianToPersian(offsetDateTime.getYear(), offsetDateTime.getMonthValue(), offsetDateTime.getDayOfMonth());

        int year = dc.getYear();
        int month = dc.getMonth();
        int day = dc.getDay();

        return new parsso.idman.Models.Time(year, month, day, offsetDateTime.getHour(), offsetDateTime.getMinute(), offsetDateTime.getSecond());
    }

    private static parsso.idman.Models.Time stringToTime(String seTime) {
        return new parsso.idman.Models.Time(Integer.valueOf(seTime.substring(0, 4)),
                Integer.valueOf(seTime.substring(5, 7)),
                Integer.valueOf(seTime.substring(8, 10)),
                Integer.valueOf(seTime.substring(11, 13)),
                Integer.valueOf(seTime.substring(14, 16)),
                Integer.valueOf(seTime.substring(17, 19)));
    }

}
