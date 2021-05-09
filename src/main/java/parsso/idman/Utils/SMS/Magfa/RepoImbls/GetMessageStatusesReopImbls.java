package parsso.idman.Utils.SMS.Magfa.RepoImbls;


import parsso.idman.Utils.SMS.Magfa.Classes.*;
import parsso.idman.Utils.SMS.Magfa.Creditions;
import parsso.idman.Utils.SMS.Magfa.Repos.GetMessagesStatusesRepo;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GetMessageStatusesReopImbls implements GetMessagesStatusesRepo {
    @Override
    public ArrayList<String> GetMessagesStatuses(Long mid) throws MalformedURLException {
        URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
        MagfaSoapServer service = new MagfaSoapServer_Service(url).getMagfaSoapServer();

// credentials
        Creditions creditions=new Creditions();
        String username = creditions.getUsername();
        String password = creditions.getPassword();
        String domain = creditions.getDomain();

// set basic auth
        BindingProvider prov = (BindingProvider) service;
        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username + "/" + domain);
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

// Compression
        HashMap httpHeaders = new HashMap<>();
        httpHeaders.put("Content-Encoding", Collections.singletonList("gzip"));//this indicates you're sending a compressed request
        httpHeaders.put("Accept-Encoding", Collections.singletonList("gzip")); //this says you're willing to accept a compressed response
        prov.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);

// call
        LongArray mids = new LongArray();
        mids.getItem().add(123456L);
        mids.getItem().add(456789L);
        DeliveryResult result = service.statuses(mids);
// result
        ArrayList<String> res= new ArrayList<>();
        if (result.getStatus() != 0 )
            res.add(String.valueOf(result.getStatus()));
        else
            for (DeliveryStatus deliveryStatus : result.getDlrs()) {
                res.add(String.valueOf(deliveryStatus.getStatus()));
            }
        return res;
    }
}
