package parsso.idman.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.UUID;

@Setter
@Getter
public class PublicMessage {
    @JsonIgnore
    private  ObjectId _id;
    private String ID;
    private String title;
    private String creator;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updater;
    private boolean visible;
    private Long createDate;
    @JsonInclude (JsonInclude.Include.NON_NULL)
    private Long updateDate;
    private String body;

    public PublicMessage(){

    }

    public PublicMessage(String title, String body, boolean visible, String creator) {
        this.ID = UUID.randomUUID().toString();
        this.title = title;
        this.visible = visible;
        this.createDate = System.currentTimeMillis();
        this.creator = creator;
        this.body = body;
    }

    public PublicMessage(PublicMessage publicMessage, String body,String title, boolean visible, String updater){
        this._id = publicMessage.get_id();
        this.createDate = publicMessage.getCreateDate();
        this.updateDate = System.currentTimeMillis();
        this.creator = publicMessage.creator;
        this.updater = updater;
        this.title = title;
        this.body = body;
        this.visible = visible;
    }
}

