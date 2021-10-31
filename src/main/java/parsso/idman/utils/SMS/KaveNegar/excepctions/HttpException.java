package parsso.idman.utils.SMS.KaveNegar.excepctions;


public class HttpException extends BaseException {
    private final int code;

    public HttpException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
