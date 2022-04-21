package parsso.idman.utils.captcha.impl.subClass;

import java.util.Random;

public class Phrase {
  public String create(int[] organization) {

    StringBuilder phrase = new StringBuilder();
    Random rand = new Random();

    for (int value : organization) {
      if (value == 0) {
        if (rand.nextInt(2) == 1)
          phrase.append((char) (rand.nextInt(123 - 97) + 97));
        else
          phrase.append((char) (rand.nextInt(123 - 97) + 97 - 32));

      } else
        phrase.append(rand.nextInt(10));
    }

    return phrase.toString();
  }

}
