package parsso.idman.Utils.SMSmagfa;


import lombok.Getter;

@Getter
public class Variables {
    public String mainMessage;


    public void setMainMessage(String mainCode) {
        String p1= "سلام. لطفا از کد ";
        String p2 = " جهت احراز هویت و ورود به پارسو استفاده نمایید.";
        this.mainMessage = p1+ mainCode+p2;
    }
}
