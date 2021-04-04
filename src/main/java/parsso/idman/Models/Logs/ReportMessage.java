package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportMessage {
    String model;
    String instance;
    String attribute;
    String result;
    String action;
    String reason;
    final char separator = ',';

    public ReportMessage() {
        model = "";
        instance = "";
        attribute = "";
        action = "";
        result = "";
        reason = "";
    }

    public ReportMessage(String model) {
        this.model = "";
        instance = "";
        attribute = "";
        action = "";
        result = "";
        reason = "";
    }

    public ReportMessage(String model, String instance, String attribute,String action, String result, String reason) {
        this.model = model;
        this.instance = instance;
        this.attribute = attribute;
        this.result = result;
        this.action = action;
        this.reason = reason;
    }

    public ReportMessage(String message, int i) {
        int separateModel = message.indexOf(separator);
        model = message.substring(0,separateModel);
        int separateInstance = message.indexOf(separator,separateModel+1);
        instance = message.substring(separateModel,separateInstance);
        int separateAttribute = message.indexOf(separator,separateInstance+1);
        attribute = message.substring(separateAttribute);
        int separateAction = message.indexOf(separator,separateAttribute+1);
        action =message.substring(separateAction);
        int separateResult = message.indexOf(separator,separateAction+1);
        result = message.substring(separateResult);
        int separateReason = message.indexOf(separator,separateResult+1);
        reason = message.substring(separateReason);
    }

    @Override
    public String toString() {
        String first = model + separator +
                instance + separator +
                attribute + separator +
                action + separator +
                result + separator +
                reason;
        String last = first.replaceAll(",,", String.valueOf(separator));
        if ((last.charAt(last.length()-1))==separator)
            return last.substring(0,last.length()-1);
        return last;
    }
}
