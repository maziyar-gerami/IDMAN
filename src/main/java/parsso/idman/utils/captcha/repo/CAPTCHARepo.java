package parsso.idman.utils.captcha.repo;


import parsso.idman.utils.captcha.models.CAPTCHAimage;

public interface CAPTCHARepo {
    CAPTCHAimage createCaptcha(int len);
    boolean check(String cid, String answer);
}
