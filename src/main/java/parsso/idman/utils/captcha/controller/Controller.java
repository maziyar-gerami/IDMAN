package parsso.idman.utils.captcha.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.utils.captcha.models.CAPTCHAimage;
import parsso.idman.utils.captcha.repoImp.CaptchaRepoImp;

@RestController
public class Controller {
    @Autowired
    CaptchaRepoImp captchaRepoImp;
    @Value("${captcha.length}")
    private String captchaLength;
    @Value("${captcha.alphabet.rate}")
    private String captchaAlphabetRate;

    @GetMapping("/api/captcha/request")
    private ResponseEntity<CAPTCHAimage> requestCaptcha() {

        CAPTCHAimage captchaImage = captchaRepoImp.createCaptcha(Integer.parseInt(captchaLength), Double.parseDouble(captchaAlphabetRate));
        if (captchaImage != null)
            return new ResponseEntity<>(captchaImage, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}