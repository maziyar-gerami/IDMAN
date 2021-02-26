package parsso.idman.Repos;


import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public interface SystemRefresh {

    HttpStatus userRefresh() throws IOException;

    HttpStatus captchaRefresh();

    HttpStatus serivceRefresh() throws IOException, ParseException;

    HttpStatus all() throws IOException, ParseException;
}
