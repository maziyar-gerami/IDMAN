package parsso.idman.utils.captcha.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.utils.captcha.models.CAPTCHAimage;
import parsso.idman.utils.captcha.repo.CAPTCHARepo;
import parsso.idman.utils.captcha.repoImp.CaptchaRepoImp;

@RestController
public class Controller {
    final CAPTCHARepo captchaRepo;
    final MongoTemplate mongoTemplate;

    @Autowired
    Controller(CaptchaRepoImp captchaRepoImp, MongoTemplate mongoTemplate) {
        this.captchaRepo = captchaRepoImp;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/api/captcha/request")
    private ResponseEntity<CAPTCHAimage> requestCaptcha() {

        CAPTCHAimage captchaImage = captchaRepo.createCaptcha(Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.CAPTCHA_LENGTH).getValue().toString()));
        if (captchaImage != null)
            return new ResponseEntity<>(captchaImage, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
