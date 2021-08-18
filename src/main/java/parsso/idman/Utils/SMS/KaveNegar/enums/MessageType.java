package parsso.idman.Utils.SMS.KaveNegar.enums;


public enum MessageType {
    Flash(0),
    MobileMemory(1),
    SimMemory(2),
    AppMemory(3);
    private final int value;

    MessageType(int type) {
        this.value = type;
    }

    public static MessageType valueOf(int type) {
        for (MessageType code : MessageType.values()) {
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
