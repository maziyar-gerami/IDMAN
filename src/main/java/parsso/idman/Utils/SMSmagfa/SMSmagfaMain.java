package parsso.idman.Utils.SMSmagfa;

import parsso.idman.Utils.SMSmagfa.RepoImbls.SendRepoImbl;

import java.net.MalformedURLException;

public class SMSmagfaMain {

    public static void main(String[] args) throws MalformedURLException {

        SendRepoImbl sendRepoImbl = new SendRepoImbl();
        System.out.println(sendRepoImbl.SendMessage("hello","09183097717",1L));
    }
}
