package parsso.idman.Models.Logs.EventsSubModel;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class Time {
	int year;
	int month;
	int day;
	int hours;
	int minutes;
	int seconds;
	int milliSeconds;

	public Time(int year, int month, int day, int hours, int minutes, int seconds) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.milliSeconds = 0;
	}
}
