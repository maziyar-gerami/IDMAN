package parsso.idman.Utils.Captcha.Repo;


import org.springframework.http.HttpStatus;
import parsso.idman.Utils.Captcha.Models.CAPTCHA;
import parsso.idman.Utils.Captcha.Models.CAPTCHAimage;

public interface CAPTCHARepo {

    CAPTCHAimage createCaptcha(int len, double alphabetRate);

    HttpStatus validateCaptcha(CAPTCHA captcha);
}
