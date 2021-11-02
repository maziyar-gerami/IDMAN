package parsso.idman.utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
    public ObjectFactory() {
    }

    public LongArray createLongArray() {
        return new LongArray();
    }

    public StringArray createStringArray() {
        return new StringArray();
    }

    public IntArray createIntArray() {
        return new IntArray();
    }

    public CustomerReturnIncomingFormat createCustomerReturnIncomingFormat() {
        return new CustomerReturnIncomingFormat();
    }

    public MessagesResult createMessagesResult() {
        return new MessagesResult();
    }

    public SendResult createSendResult() {
        return new SendResult();
    }

    public CreditResult createCreditResult() {
        return new CreditResult();
    }

    public DeliveryResult createDeliveryResult() {
        return new DeliveryResult();
    }

    public SendMessage createSendMessage() {
        return new SendMessage();
    }

    public DatedCustomerReturnIncomingFormat createDatedCustomerReturnIncomingFormat() {
        return new DatedCustomerReturnIncomingFormat();
    }

    public MessageIdResult createMessageIdResult() {
        return new MessageIdResult();
    }

    public DeliveryStatus createDeliveryStatus() {
        return new DeliveryStatus();
    }

}
