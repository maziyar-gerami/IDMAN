package parsso.idman.Models;

import lombok.Getter;
import lombok.Setter;
import parsso.idman.utils.Convertor.DateConverter;

@Setter
@Getter

public class Time {
    int year;
    int month;
    int day;

    int hours;
    int minutes;
    int seconds;
    int miliseconds;


    public Time(int year, int month, int day, int hours, int minutes, int seconds) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.miliseconds = 0;
    }



    public static String setEndTime(String input) {
        //if is jalali
        if (Integer.valueOf(input.substring(0, 4)) < 2000) {

            if (!(input.contains("-"))) {
                int Y = Integer.valueOf(input.substring(0, 4));
                int M = Integer.valueOf(input.substring(4, 6));
                int D = Integer.valueOf(input.substring(6, 8));

                DateConverter dateConverter = new DateConverter();
                dateConverter.persianToGregorian(Y, M, D);

                return dateConverter.getYear()
                        + String.format("%02d", dateConverter.getMonth())
                        + String.format("%02d", dateConverter.getDay())
                        + input.substring(8, 10)
                        + input.substring(10, 12)
                        + input.substring(12, 14)
                        + input.substring(15, 17);
            } else {
                return convertDateTimeJalali(input);


            }


            //if is G
        } else {

            if (!(input.contains("-"))) {

                return convertDateNumber(input);


            } else {

                return input;

            }


        }

    }

    public static String convertDateTimeJalali(String seTime) {

        String year = seTime.substring(0, 4);
        String month = seTime.substring(5, 7);
        String day = seTime.substring(8, 10);

        String hours = seTime.substring(11, 13);
        String minutes = seTime.substring(14, 16);
        String seconds = seTime.substring(17, 19);

        String miliSeconds = seTime.substring(20, 23);

        String date = convertDateJalaliToGeorgian(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));

        String time = String.format("%02d", Integer.valueOf(hours)) +
                String.format("%02d", Integer.valueOf(minutes)) +
                String.format("%02d", Integer.valueOf(seconds)) + "." +
                String.format("%03d", Integer.valueOf(miliSeconds)) + "Z";

        return date + time;


    }

    public static String convertDateGeorgianToJalali(int Y, int M, int D) {

        DateConverter dateConverter = new DateConverter();
        dateConverter.gregorianToPersian(Y, M, D);

        return dateConverter.getYear() + String.format("%02d", dateConverter.getMonth()) + String.format("%02d", dateConverter.getDay());
    }


    public static String convertDateJalaliToGeorgian(int Y, int M, int D) {

        DateConverter dateConverter = new DateConverter();
        dateConverter.persianToGregorian(Y, M, D);

        return dateConverter.getYear() + String.format("%02d", dateConverter.getMonth()) + String.format("%02d", dateConverter.getDay());
    }

    public static String convertDateNumber(String date) {
        String y = date.substring(0, 4);
        String M = date.substring(4, 6);
        String d = date.substring(6, 8);

        String h = date.substring(8, 10);
        String m = date.substring(10, 12);
        String s = date.substring(12, 14);
        String S = date.substring(15, 18);

        return y + "-" + M + "-" + d + "T" + h + ":" + m + ":" + s + "." + S;
    }

    public static String convertDateTimeGeorgian(String seTime) {

        String year = seTime.substring(0, 4);
        String month = seTime.substring(5, 7);
        String day = seTime.substring(8, 10);

        String hours = seTime.substring(11, 13);
        String minutes = seTime.substring(14, 16);
        String seconds = seTime.substring(17, 19);

        String miliSeconds = seTime.substring(20, 23);

        String date = convertDateGeorgianToJalali(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));

        String time = String.format("%02d", Integer.valueOf(hours)) + "-" +
                String.format("%02d", Integer.valueOf(minutes)) + "-" +
                String.format("%02d", Integer.valueOf(seconds)) + "." +
                String.format("%03d", Integer.valueOf(miliSeconds));

        return date.substring(0, 4) + '-' + date.substring(4, 6) + '-' + date.substring(6, 8) + 'T' + time;


    }




}
