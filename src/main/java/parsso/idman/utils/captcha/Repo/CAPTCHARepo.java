package parsso.idman.utils.captcha.Repo;


import org.springframework.http.HttpStatus;
import parsso.idman.utils.captcha.Models.CAPTCHA;
import parsso.idman.utils.captcha.Models.CAPTCHAimage;

public interface CAPTCHARepo {
    CAPTCHAimage createCaptcha(int len, double alphabetRate);

    HttpStatus validateCaptcha(CAPTCHA captcha);
}
