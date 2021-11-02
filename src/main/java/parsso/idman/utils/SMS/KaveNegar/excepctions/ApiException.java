package parsso.idman.utils.SMS.KaveNegar.excepctions;


import parsso.idman.utils.SMS.KaveNegar.enums.MetaData;

public class ApiException extends BaseException {
    final int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    @SuppressWarnings("unused")
    public MetaData getCode() {
        return MetaData.valueOf(code);
    }
}
