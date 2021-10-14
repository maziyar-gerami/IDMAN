package parsso.idman.repos;


import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@SuppressWarnings("SameReturnValue")
public interface SystemRefresh {
	HttpStatus userRefresh(String doer);

	HttpStatus captchaRefresh(String doer);

	HttpStatus serivceRefresh(String doer);

	HttpStatus all(String doer) throws IOException, ParseException;

	void refreshLockedUsers();
}
