package parsso.idman.Utils.SMS.KaveNegar.excepctions;


import parsso.idman.Utils.SMS.KaveNegar.enums.MetaData;

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
