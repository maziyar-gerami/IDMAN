package parsso.idman.models.other;


import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Response {
    Status result;
    Object data;

    public Response(Object data, String lang) {
        this.result = new Status();
        this.data = data;
    }

    @Setter
    @Getter
    private static class Status {
        int code;
        String result_fa;
        String result_en;

        public Status() {
            code = 200;
            result_en = StringResult.COMMON_200_EN;
            result_fa = StringResult.COMMON_200_FA;
        }
    }
}
