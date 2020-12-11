package parsso.idman.Models.ServicesSubModel;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExtraInfo {
    long id;
    String url;
    int position;
    public ExtraInfo(){
        position = 0;
    }
}
