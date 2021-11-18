package parsso.idman.repoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.repos.MagfaSMSSendRepo;
import parsso.idman.utils.sms.magfa.classes.*;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class MagfaSMSSendRepoImpl implements MagfaSMSSendRepo {
    @Autowired
    UniformLogger uniformLogger;
    @Value("${SMS.Magfa.username}")
    String username;
    @Value("${SMS.Magfa.password}")
    String password;

    @Override
    public void SendMessage(String message, String PhoneNumber, Long id) {
        MagfaSoapServer service = null;
        try {
            URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
            service = new MagfaSoapServer_Service(url).getMagfaSoapServer();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String domain = "magfa";
        BindingProvider prov = (BindingProvider) service;
        Objects.requireNonNull(prov).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username + "/" + domain);
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
        HashMap<Object, Object> httpHeaders = new HashMap<>();
        httpHeaders.put("Content-Encoding", Collections.singletonList("gzip"));//this indicates you're sending a compressed request
        httpHeaders.put("Accept-Encoding", Collections.singletonList("gzip")); //this says you're willing to accept a compressed response
        prov.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);
        StringArray messages = new StringArray();
        List<String> messageItems = messages.getItem();
        messageItems.add(message);
        StringArray recipients = new StringArray();
        List<String> recipientItems = recipients.getItem();
        recipientItems.add(PhoneNumber);
        StringArray senders = new StringArray();
        List<String> senderItems = senders.getItem();
        senderItems.add("300002394");
        LongArray uids = new LongArray();
        List<Long> uidItems = uids.getItem();
        uidItems.add(id);
        SendResult result = service.send(messages, senders, recipients, uids, new IntArray(), new StringArray(), new IntArray());
        ArrayList<String> res = new ArrayList<>();
        res.add(String.valueOf(result.getStatus()));
        for (SendMessage sendMessage : result.getMessages()) {
            res.add(sendMessage.toString());
        }
    }

}
