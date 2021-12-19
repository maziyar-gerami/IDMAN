package parsso.idman.models.other;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Report;
import parsso.idman.utils.convertor.DateConverter;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter

public class Time {
    @JsonIgnore
    static final ZoneId zoneId = ZoneId.of(Variables.ZONE);

    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;
    private int seconds;
    private int milliSeconds;

    @SuppressWarnings("unused")
    public Time() {
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Time)) return false;
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

    public List<Report> reportSetDate(List<Report> allReports) {
        for (Report report : allReports) {
            OffsetDateTime logDate = report.getDate().toInstant()
                    .atOffset(ZoneId.of(Variables.ZONE).getRules().getOffset(Instant.now()));
            Time time1 = new Time(logDate.getYear(), logDate.getMonthValue(), logDate.getDayOfMonth(),
                    logDate.getHour(), logDate.getMinute(), logDate.getSecond());
            report.setDateTime(time1);
        }
        return allReports;
    }

    public Time stringInputToTime(String date) {
        return new Time(Integer.parseInt(date.substring(4)),
                Integer.parseInt(date.substring(2, 4)),
                Integer.parseInt(date.substring(0, 2)));
    }

    public String getExportEndTime(String input) {

        String Y = input.substring(0, 4);
        String M = input.substring(4, 6);
        String D = input.substring(6, 8);

        String H = input.substring(8, 10);
        String m = input.substring(10, 12);

        ZonedDateTime eventDate = OffsetDateTime.parse(Y + "-" + M + "-" + D + 'T' + H + ":" + m + ":00" + ".000Z").toZonedDateTime();

        Time time = longToPersianTime(eventDate.toInstant().toEpochMilli());

        return time.getYear() + "-"
                + String.format("%02d", time.getMonth()) + "-"
                + String.format("%02d", time.getDay()) + " "
                + String.format("%02d", time.getHours()) + ":"
                + String.format("%02d", time.getMinutes());
    }

    public String epochToDateLdapFormat(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String strDate = null;

        if (String.valueOf(timeInMilliseconds).charAt(0) == '1')
            strDate = formatter.format(new Date(timeInMilliseconds));
        else if (String.valueOf(timeInMilliseconds).charAt(0) == '2')
            strDate = String.valueOf(timeInMilliseconds);

        Instant instant = Instant.now(); //can be LocalDateTime
        ZoneId systemZone = ZoneId.of(Variables.ZONE); // my timezone
        ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(instant);

        return strDate + currentOffsetForMyZone.toString().replaceAll(":", "");
    }

    public long[] specificDateToEpochRange(Time time, ZoneId zoneId) {

        long[] result = new long[2];

        LocalDate day = LocalDate.of(time.getYear(), time.getMonth(), time.getDay());

        result[0] = day.atStartOfDay().atZone(zoneId).toEpochSecond() * 1000;
        result[1] = day.plusDays(1).atStartOfDay().atZone(zoneId).toEpochSecond() * 1000;

        return result;

    }

    public Date convertEpochToDate(long epoch) {
        return new Date(epoch);
    }

    public static Time longToPersianTime(Long in) {
        Instant instant = Instant.ofEpochMilli(in);
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(instant, zoneId);

        DateConverter dc = new DateConverter();

        dc.gregorianToPersian(offsetDateTime.getYear(), offsetDateTime.getMonthValue(), offsetDateTime.getDayOfMonth());

        int year = dc.getYear();
        int month = dc.getMonth();
        int day = dc.getDay();

        return new Time(year, month, day, offsetDateTime.getHour(), offsetDateTime.getMinute(), offsetDateTime.getSecond());
    }
}
