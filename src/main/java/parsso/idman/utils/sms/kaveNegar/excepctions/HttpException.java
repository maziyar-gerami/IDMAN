package parsso.idman.utils.sms.kaveNegar.excepctions;


public class HttpException extends BaseException {
    private final int code;

    public HttpException(String message, int code) {
        super(message);
        this.code = code;
    }

    @SuppressWarnings("unused")
    public int getCode() {
        return code;
    }
}
