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
    public Response(Object data, String model, String lang) throws NoSuchFieldException, IllegalAccessException {
        this.status = new Status(model,lang);
        this.data = data;
    }

    public Response(String lang, String model, int code) throws NoSuchFieldException, IllegalAccessException {
        status = new Status(code,model,lang);

    }

    @Setter
    @Getter
    private static class Status {
        int code;
        String result;
        String model;

        public Status(String lang) {
            code = 200;
            if (lang.equals("fa"))
                this.result = StringResult.COMMON_200_FA;
            else
                result = StringResult.COMMON_200_EN;
        }

        public Status(String model,String lang) throws NoSuchFieldException, IllegalAccessException {
            code = 200;
            this.model = model;
            this.result = StringResult.get(code,model,lang);

        }

        public Status(int code,String model, String lang) throws NoSuchFieldException, IllegalAccessException {
            this.code= code;
            this.model = model;
            this.result = StringResult.get(code,model,lang);
        }
    }
}
