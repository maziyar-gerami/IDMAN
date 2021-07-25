package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;
import parsso.idman.Utils.Convertor.DateConverter;

import java.util.Calendar;

@Setter
@Getter

public class Time {
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
