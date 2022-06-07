package parsso.idman.utils.captcha.impl;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.utils.captcha.impl.subClass.Image;
import parsso.idman.utils.captcha.impl.subClass.Organization;
import parsso.idman.utils.captcha.impl.subClass.Phrase;
import parsso.idman.utils.captcha.models.CAPTCHA;
import parsso.idman.utils.captcha.models.CAPTCHAimage;
import parsso.idman.utils.captcha.repo.CAPTCHARepo;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Setter
@Getter
@Service
public class CaptchaRepoImp implements CAPTCHARepo {
  MongoTemplate mongoTemplate;

  @Autowired
  public CaptchaRepoImp(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  int len;
  double alphabetRate;

  public CaptchaRepoImp() {
    this.len = 5;
    this.alphabetRate = 0.5;
  }

  public CaptchaRepoImp(int len, double alphabetRate) {
    this.len = len;
    this.alphabetRate = alphabetRate;
  }

  public CaptchaRepoImp(int len) {
    this.alphabetRate = 0.5;
    this.len = len;
  }

  public CAPTCHAimage createCaptcha(int len) {
    int[] organization = new Organization().create(len);
    String phrase = new Phrase().create(organization);
    CAPTCHA captcha = new CAPTCHA(phrase);

    try {

      mongoTemplate.save(captcha, Variables.col_captchas);
      return new Image().create(phrase, captcha);

    } catch (Exception e) {
      return null;
    }

  }

  @Override
  public boolean check(String cid, String answer) {

    try {
      CAPTCHA captcha = mongoTemplate.findOne(new Query(Criteria.where("_id").is(cid)),
          CAPTCHA.class, Variables.col_captchas);
      return captcha.getPhrase().equalsIgnoreCase(answer);
    } catch (Exception e) {
      return false;
    }
  }
}
