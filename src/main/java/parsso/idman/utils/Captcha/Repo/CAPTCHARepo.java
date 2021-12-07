package parsso.idman.utils.captcha.repo;


import parsso.idman.utils.captcha.models.CAPTCHAimage;

public interface CAPTCHARepo {
    @SuppressWarnings("unused")
    CAPTCHAimage createCaptcha(int len, double alphabetRate);

}
