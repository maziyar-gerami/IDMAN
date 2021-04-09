package parsso.idman.Models.Users;


import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
public class PublicMessage {
    String _id;
    String title;
    OffsetDateTime date;
    List<String> message;
}
