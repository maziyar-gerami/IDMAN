package parsso.idman.utils.SMS.KaveNegar.enums;


public enum MessageStatus {
    Queued(1),
    Schulded(2),
    SentToCenter(4),
    Delivered(10),
    Undelivered(11),
    Canceled(13),
    Filtered(14),
    Received(50),
    Incorrect(100);
    private final int value;

    MessageStatus(int type) {
        this.value = type;
    }

    public static MessageStatus valueOf(int type) {
        for (MessageStatus code : MessageStatus.values()) {
            if (type == code.getValue()) {
                return code;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
