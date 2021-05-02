package parsso.idman.Utils.SMSmagfa.RepoImbls;

import parsso.idman.Utils.SMSmagfa.Classes.CreditResult;
import parsso.idman.Utils.SMSmagfa.Classes.MagfaSoapServer;
import parsso.idman.Utils.SMSmagfa.Classes.MagfaSoapServer_Service;
import parsso.idman.Utils.SMSmagfa.Creditions;
import parsso.idman.Utils.SMSmagfa.Repos.GetBalanceRepo;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;

public class GetBalanceRepoImbl implements GetBalanceRepo {
    @Override
    public String GetBalanceRepo() throws MalformedURLException {
        URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
        MagfaSoapServer service = new MagfaSoapServer_Service(url).getMagfaSoapServer();
        Creditions creditions=new Creditions();
        String username = creditions.getUsername();
        String password = creditions.getPassword();
        String domain = creditions.getDomain();
        BindingProvider prov = (BindingProvider) service;
        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username + "/" + domain);
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
        HashMap httpHeaders = new HashMap<>();
        httpHeaders.put("Content-Encoding", Collections.singletonList("gzip"));//this indicates you're sending a compressed request
        httpHeaders.put("Accept-Encoding", Collections.singletonList("gzip")); //this says you're willing to accept a compressed response
        prov.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);
        CreditResult result = service.balance();
        if (result.getStatus() != 0)
            return String.valueOf(result.getStatus());
        else
            return String.valueOf(result.getBalance());
    }
}
