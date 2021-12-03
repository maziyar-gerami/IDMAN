package parsso.idman.models.response;

import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.other.StringResult;


@Setter
@Getter
public class Response {
    Status status;
    Object data;

    public Response(Object data, String lang) {
        this.status = new Status(lang);
        this.data = data;
    }

    @Setter
    @Getter
    private static class Status {
        int code;
        String result;

        public Status(String lang) {
            code = 200;
            if (lang.equals("fa"))
                this.result = StringResult.COMMON_200_FA;
            else
                result = StringResult.COMMON_200_EN;
        }
    }
}
