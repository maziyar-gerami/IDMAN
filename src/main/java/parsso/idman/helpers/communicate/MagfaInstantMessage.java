package parsso.idman.helpers.communicate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.utils.sms.magfa.classes.IntArray;
import parsso.idman.utils.sms.magfa.classes.LongArray;
import parsso.idman.utils.sms.magfa.classes.MagfaSoapServer;
import parsso.idman.utils.sms.magfa.classes.MagfaSoapServer_Service;
import parsso.idman.utils.sms.magfa.classes.StringArray;




public class MagfaInstantMessage {
  final MongoTemplate mongoTemplate;
  String message;

  public MagfaInstantMessage(MongoTemplate mongoTemplate, String message) {
    this.mongoTemplate = mongoTemplate;
    this.message = message;
  }

  public MagfaInstantMessage(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public void SendMessage(String PhoneNumber, Long id) {
    MagfaSoapServer service = null;
    try {
      URL url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
      service = new MagfaSoapServer_Service(url).getMagfaSoapServer();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    String domain = "magfa";
    BindingProvider prov = (BindingProvider) service;
    Objects.requireNonNull(prov).getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
        new Settings(mongoTemplate).retrieve(Variables.SMS_MAGFA_USERNAME).getValue() + "/" + domain);
    prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
        new Settings(mongoTemplate).retrieve(Variables.SMS_MAGFA_PASSWORD).getValue());
    HashMap<Object, Object> httpHeaders = new HashMap<>();
    httpHeaders.put("Content-Encoding", Collections.singletonList("gzip"));// this indicates you're sending a
    // compressed request
    httpHeaders.put("Accept-Encoding", Collections.singletonList("gzip")); // this says you're willing to accept a
    // compressed response
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
    service.send(messages, senders, recipients, uids, new IntArray(), new StringArray(), new IntArray());
  }

}
