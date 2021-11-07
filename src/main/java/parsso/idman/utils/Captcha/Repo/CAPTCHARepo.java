package parsso.idman.utils.captcha.repo;


import org.springframework.http.HttpStatus;
import parsso.idman.utils.captcha.models.CAPTCHA;
import parsso.idman.utils.captcha.models.CAPTCHAimage;

public interface CAPTCHARepo {
    CAPTCHAimage createCaptcha(int len, double alphabetRate);

    HttpStatus validateCaptcha(CAPTCHA captcha);
}
