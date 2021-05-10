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
    private String messageId;
    private String title;
    private String creator;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updater;
    private boolean visible;
    @JsonIgnore
    private Long createDate;
    @JsonIgnore
    private Long updateDate;

    private  Time createTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Time updateTime;
    private String body;

    public PublicMessage(){

    }

    public Time getCreateTime() {
        return Time.longToPersianTime(getCreateDate());
    }

    public Time getUpdateTime() {
        if (updateDate!= null)
        return Time.longToPersianTime(updateDate);

        return null;
    }

    public PublicMessage(String title, String body, boolean visible, String creator) {
        this.messageId = UUID.randomUUID().toString();
        this.title = title;
        this.visible = visible;
        this.createDate = System.currentTimeMillis();
        this.creator = creator;
        this.body = body;
    }
}

