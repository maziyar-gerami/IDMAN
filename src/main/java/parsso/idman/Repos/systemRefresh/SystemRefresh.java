package parsso.idman.Repos.systemRefresh;


import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public interface SystemRefresh {
	HttpStatus userRefresh(String doer) throws IOException, ParseException;

	HttpStatus captchaRefresh(String doer) throws IOException, ParseException;

	HttpStatus serivceRefresh(String doer) throws IOException, ParseException;

	HttpStatus all(String doer) throws IOException, ParseException;

	HttpStatus refreshLockedUsers() throws IOException, ParseException;
}
