package parsso.idman.Captcha.Repo;


import org.springframework.http.HttpStatus;
import parsso.idman.Captcha.Models.CAPTCHA;
import parsso.idman.Captcha.Models.CAPTCHAimage;

public interface CAPTCHARepo {

    CAPTCHAimage createCaptcha(int len, double alphabetRate);

    HttpStatus validateCaptcha(CAPTCHA captcha);
}
