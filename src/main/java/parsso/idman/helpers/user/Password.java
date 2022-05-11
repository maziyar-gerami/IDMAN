package parsso.idman.helpers.user;

import java.util.regex.Pattern;


import org.springframework.data.mongodb.core.MongoTemplate;

import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;

public class Password {
  MongoTemplate mongoTemplate;
  int length;
  boolean smallalphabet;
  boolean capitalalphabet;
  boolean number;
  boolean special;
  final Pattern[] inputRegexes = new Pattern[4];

  public Password(MongoTemplate mongoTemplate) {
    inputRegexes[0] = Pattern.compile(".*[A-Z].*");
    inputRegexes[1] = Pattern.compile(".*[a-z].*");
    inputRegexes[2] = Pattern.compile(".*\\d.*");
    inputRegexes[3] = Pattern.compile(".*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?].*");
    this.mongoTemplate = mongoTemplate;
  }

  public boolean check(String pass) {
    length = Integer.valueOf(new Settings(mongoTemplate).retrieve(Variables.PASSWORD_LENGTH).getValue());
    smallalphabet = Boolean.valueOf(new Settings(mongoTemplate).retrieve(Variables.PASSWORD_SMALL_ALPHABET).getValue());
    capitalalphabet = Boolean
        .valueOf(new Settings(mongoTemplate).retrieve(Variables.PASSWORD_CAPITAL_ALPHABET).getValue());
    number = Boolean.valueOf(new Settings(mongoTemplate).retrieve(Variables.PASSWORD_NUMBER).getValue());
    special = Boolean.valueOf(new Settings(mongoTemplate).retrieve(Variables.PASSWORD_SPECIAL).getValue());

    if (pass.length() < length) {
      return false;
    }
    if (smallalphabet) {
      if (!inputRegexes[0].matcher(pass).matches())
        return false;
    }

    if (capitalalphabet) {
      if (!inputRegexes[1].matcher(pass).matches())
        return false;
    }

    if (number) {
      if (!inputRegexes[2].matcher(pass).matches())
        return false;
    }

    if (special) {
      if (!inputRegexes[3].matcher(pass).matches())
        return false;
    }

    return true;
  }

}
