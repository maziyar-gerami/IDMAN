package parsso.idman.utils.sms.kaveNegar.excepctions;

import parsso.idman.utils.sms.kaveNegar.enums.MetaData;

public class ApiException extends BaseException {
  final int code;

  public ApiException(String message, int code) {
    super(message);
    this.code = code;
  }

  public MetaData getCode() {
    return MetaData.valueOf(code);
  }
}
