package parsso.idman.Models.Services;


import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.Time;

@SuppressWarnings("rawtypes")
@Getter
@Setter
public class SimpleTime implements Comparable{
	int hour;
	int minute;

	public SimpleTime(){

	}

	public SimpleTime(Time time){
		this.hour = time.getHours();
		this.minute = time.getMinutes();
	}

	@Override
	public int compareTo(Object o) {
		if(this.hour>((SimpleTime) o).getHour())
			return 1;

		else if(this.hour==((SimpleTime) o).getHour())
			if (this.getMinute()>((SimpleTime) o).getMinute())
				return 1;

		return -1;
	}
}