package parsso.idman.utils.captcha.impl.subClass;

import java.util.Random;

public class Organization {

  public int[] create(int len) {
    int nAlphabet = (int) Math.ceil(0.5 * len);
    int rAlphabet = nAlphabet;
    int rNumbers = len - nAlphabet;
    int[] org = new int[len];
    Random rand = new Random();
    int temp;
    for (int i = 0; i < len; i++) {
      temp = rand.nextInt(2);
      if (temp == 0) {
        if (rAlphabet > 0) {
          rAlphabet--;
          org[i] = 0;
        } else {
          rNumbers--;
          org[i] = 1;
        }

      } else {
        if (rNumbers > 0) {
          rNumbers--;
          org[i] = 1;
        } else {
          rAlphabet--;
          org[i] = 0;
        }

      }

    }
    return org;
  }

}
