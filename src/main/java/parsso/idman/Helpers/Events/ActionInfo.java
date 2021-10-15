package parsso.idman.Helpers.Events;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ActionInfo {
    String actionIn;
    String action;

    public ActionInfo(String action) {
        this.actionIn = action;
    }

    public String getAction() {
        if (actionIn.contains("CasTicketGrantingTicketCreatedEvent")) {

            action = "Successful Login";

        } else if (actionIn.contains("CasAuthenticationTransactionFailureEvent")) {

            action = "Unsuccessful Login";

        } else {
            action = "Other";
        }
        return action;
    }


}
