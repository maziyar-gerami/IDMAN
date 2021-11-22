package parsso.idman.utils.filesStorageService;


public class ResponseMessage {
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public void setMessage(String message) {
        this.message = message;
    }

}
