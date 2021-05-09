package parsso.idman.Utils.SMS.Magfa.RepoImbls;

import parsso.idman.Utils.SMS.Magfa.Classes.*;
import parsso.idman.Utils.SMS.Magfa.Creditions;
import parsso.idman.Utils.SMS.Magfa.Repos.SendRepo;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SendRepoImbl implements SendRepo {
    @Override
    public ArrayList<String> SendMessage(String message, String PhoneNumber, Long id) throws MalformedURLException {
        URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
        MagfaSoapServer service = new MagfaSoapServer_Service(url).getMagfaSoapServer();
        Creditions creditions =new Creditions();
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
        StringArray messages = new StringArray();
        List messageItems = messages.getItem();
        messageItems.add(message);
        StringArray recipients = new StringArray();
        List recipientItems = recipients.getItem();
        recipientItems.add(PhoneNumber);
        StringArray senders = new StringArray();
        List senderItems = senders.getItem();
        senderItems.add("300002394");
        LongArray uids = new LongArray();
        List uidItems = uids.getItem();
        uidItems.add(id);
        SendResult result = service.send(messages, senders, recipients, uids, new IntArray(), new StringArray(), new IntArray());
        ArrayList<String> res= new ArrayList<>();
        res.add(String.valueOf(result.getStatus()));
        for (SendMessage sendMessage : result.getMessages()) {
            res.add(sendMessage.toString());
        }
        return res;
    }
}
