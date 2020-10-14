package parsso.idman.Models;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.annotation.Generated;


@Getter
@Setter
@ToString
public class Group {



    @Id
    private String id;
    private String name;
    private String description;

    public Group(String name, String description){

        this.name = name;
        this.description = description;
    }
    public Group(){
    }

}
