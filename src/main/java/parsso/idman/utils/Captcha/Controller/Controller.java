package parsso.idman.Utils.Captcha.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Utils.Captcha.Models.CAPTCHA;
import parsso.idman.Utils.Captcha.Models.CAPTCHAimage;
import parsso.idman.Utils.Captcha.RepoImp.CaptchaRepoImp;

@RestController
public class Controller {
    @Autowired
    CaptchaRepoImp captchaRepoImp;
    @Value("${captcha.lenght}")
    private String captchaLenght;
    @Value("${captcha.alphabet.rate}")
    private String captchaAlphabetRate;

    @GetMapping("/api/captcha/request")
    private ResponseEntity<CAPTCHAimage> requestCaptcha() {

        CAPTCHAimage captchaImage = captchaRepoImp.createCaptcha(Integer.parseInt(captchaLenght), Double.parseDouble(captchaAlphabetRate));
        if (captchaImage != null)
            return new ResponseEntity<>(captchaImage, HttpStatus.OK);
        else
            return new ResponseEntity<>(captchaImage, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/api/captcha/validate")
    private ResponseEntity<CAPTCHAimage> validateCaptcha(@RequestBody CAPTCHA captcha) {
        return new ResponseEntity<>(captchaRepoImp.validateCaptcha(captcha));
    }
}
