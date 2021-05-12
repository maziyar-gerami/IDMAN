package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import parsso.idman.Utils.SMS.Magfa.Classes.*;
import parsso.idman.Repos.MagfaSMSSendRepo;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class MagfaSMSSendRepoImpl implements MagfaSMSSendRepo {

    @Value("${SMS.Magfa.username}")
    String username;
    @Value("${SMS.Magfa.password}")
    String password;
    @Override
    public ArrayList<String> SendMessage(String message,String PhoneNumber, Long id) throws MalformedURLException {
        URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
        MagfaSoapServer service = new MagfaSoapServer_Service(url).getMagfaSoapServer();
        String domain = "magfa";
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

    @Override
    public ArrayList<String> InputMessage() throws MalformedURLException {
        URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
        MagfaSoapServer service = new MagfaSoapServer_Service(url).getMagfaSoapServer();
        String domain = "magfa";
        BindingProvider prov = (BindingProvider) service;
        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username + "/" + domain);
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
        HashMap httpHeaders = new HashMap<>();
        httpHeaders.put("Content-Encoding", Collections.singletonList("gzip"));//this indicates you're sending a compressed request
        httpHeaders.put("Accept-Encoding", Collections.singletonList("gzip")); //this says you're willing to accept a compressed response
        prov.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);
        MessagesResult result = service.messages(100, "");
        ArrayList<String> res=new ArrayList<>();
        if(result.getStatus() != 0 ){
            res.add(String.valueOf(result.getStatus()));
            return res;
        }
        else
            for (DatedCustomerReturnIncomingFormat msg : result.getMessages()) {
                res.add(msg.getDate()+":"+msg.getBody());
            }
        return res;
    }

    @Override
    public ArrayList<String> GetMessagesStatuses() throws MalformedURLException {
        URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
        MagfaSoapServer service = new MagfaSoapServer_Service(url).getMagfaSoapServer();

// credentials
        String domain = "magfa";

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

    @Override
    public String GetMessage(Long id) throws MalformedURLException {
        URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
        MagfaSoapServer service = new MagfaSoapServer_Service(url).getMagfaSoapServer();

// credentials
        String domain = "magfa";

// set basic auth
        BindingProvider prov = (BindingProvider)service;
        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username + "/" + domain);
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

// Compression
        HashMap httpHeaders = new HashMap<>();
        httpHeaders.put("Content-Encoding", Collections.singletonList("gzip"));//this indicates you're sending a compressed request
        httpHeaders.put("Accept-Encoding", Collections.singletonList("gzip")); //this says you're willing to accept a compressed response
        prov.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);

// call
        MessageIdResult result = service.mid(id);

// result
        if(result.getStatus() != 0 )
            return String.valueOf(result.getStatus());
        else {
            return String.valueOf(result.getMid());
        }
    }

    @Override
    public String GetBalanceRepo() throws MalformedURLException {
        URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
        MagfaSoapServer service = new MagfaSoapServer_Service(url).getMagfaSoapServer();
        String domain = "magfa";
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
