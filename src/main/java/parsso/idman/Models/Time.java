package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;
import parsso.idman.Utils.Convertor.DateConverter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

@Setter
@Getter

public class Time {
    static ZoneId zoneId = ZoneId.of("UTC+03:30");
    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;
    private int seconds;
    private int milliSeconds;

    public Time(Calendar cal) {
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DATE);

        hours = cal.get(Calendar.HOUR_OF_DAY);
        minutes = cal.get(Calendar.MINUTE);
        seconds = cal.get(Calendar.SECOND);

    }

    public Time(int year, int month, int day, int hours, int minutes, int seconds) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliSeconds = 0;
    }

    public Time(int year, int month, int day) {

        DateConverter dateConverter = new DateConverter();
        dateConverter.persianToGregorian(year, month, day);

        this.year = dateConverter.getYear();
        this.month = dateConverter.getMonth();
        this.day = dateConverter.getDay();
    }

    public Time(DateConverter dateConverter, Calendar myCal) {
        this.year = dateConverter.getYear();
        this.month = dateConverter.getMonth();
        this.day = dateConverter.getDay();

        this.hours = myCal.get(Calendar.HOUR_OF_DAY);
        this.minutes = myCal.get(Calendar.MINUTE);
        this.seconds = myCal.get(Calendar.SECOND);
        this.milliSeconds = myCal.get(Calendar.MILLISECOND);
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
                        + String.format("%02d", eventDate.getNano());
            }else if (input.contains("/")) {
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
                } catch (Exception e){
                    H="23";
                    m="59";
                    s = "59";
                    S = "99";

                }

                String date = convertDateJalaliToGeorgian(Integer.valueOf(Y), Integer.valueOf(M), Integer.valueOf(D));


                ZonedDateTime eventDate = OffsetDateTime.parse(date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8) + 'T' + H + ":" + m + ":" + s + "." + S+"+03:30").atZoneSameInstant(zoneId);

                return eventDate.getYear()
                        + String.format("%02d", eventDate.getMonthValue())
                        + String.format("%02d", eventDate.getDayOfMonth())
                        + String.format("%02d", eventDate.getHour())
                        + String.format("%02d", eventDate.getMinute());
                
            }

            else {
                return convertDateTimeJalali(input + "+03:30");

            }

            //if is G
        } else {

            if (input.contains("-"))
                return convertDateToNumber(input);


            return input;

        }

    }

    public static String convertDateTimeJalali(String seTime) {

        Time timeObject = stringToTime(seTime);


        String date = convertDateJalaliToGeorgian(Integer.valueOf(timeObject.getYear()), Integer.valueOf(timeObject.getMonth()), Integer.valueOf(timeObject.getDay()));

        String time = String.format("%02d", Integer.valueOf(timeObject.getYear())) +
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
        Time timeObject = stringToTime(seTime);

        String time = String.format("%02d", Integer.valueOf(timeObject.getHours())) +
                String.format("%02d", Integer.valueOf(timeObject.getMinutes())) +
                String.format("%02d", Integer.valueOf(timeObject.seconds)) + "." +
                String.format("%03d", Integer.valueOf(timeObject.getMilliSeconds())) + "+0330";

        return timeObject.getYear() + timeObject.getMonth() + timeObject.getDay() + time;
    }

    private static Time stringToTime(String seTime){
        return new Time(Integer.valueOf(seTime.substring(0, 4)),
                Integer.valueOf(seTime.substring(5, 7)),
                Integer.valueOf(seTime.substring(8, 10)),
                Integer.valueOf(seTime.substring(11, 13)),
                Integer.valueOf(seTime.substring(14, 16)),
                Integer.valueOf(seTime.substring(17, 19)));
    }

    @Override
    public boolean equals(Object obj) {
        return this.year == ((Time) obj).getYear() &&
                this.month == ((Time) obj).getMonth() &&
                this.day == ((Time) obj).getDay();
    }

    public String toString() {
        return getYear() + "-" + getMonth() + "-" + getDay() + "T"
                + getHours() + ":" + getMinutes() + ":" + getSeconds() + "." + getMilliSeconds();
    }

    public String toStringDate() {

        return String.format(("%4d"), getYear()) + "-" + String.format(("%02d"), getMonth()) + "-" + String.format(("%02d"), getDay());

    }
}
