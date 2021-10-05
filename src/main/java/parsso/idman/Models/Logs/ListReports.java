package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ListReports {
	long size;
	int pages;
	List<Report> reportsList;

	public ListReports(long size, int pages, List<Report> relativeEvents) {
		this.size = size;
		this.pages = pages;
		this.reportsList = relativeEvents;
	}

	public ListReports() {

	}

	public ListReports(List<Report> reports, long size, int pages) {
		this.size = size;
		this.pages = pages;
		this.reportsList = reports;
	}
}
