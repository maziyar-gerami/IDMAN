package parsso.idman.utils.Captcha.Repo;


import org.springframework.http.HttpStatus;
import parsso.idman.utils.Captcha.Models.CAPTCHA;
import parsso.idman.utils.Captcha.Models.CAPTCHAimage;

public interface CAPTCHARepo {
    CAPTCHAimage createCaptcha(int len, double alphabetRate);

    HttpStatus validateCaptcha(CAPTCHA captcha);
}
