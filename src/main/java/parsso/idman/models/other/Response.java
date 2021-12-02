package parsso.idman.models.other;


import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;

@Setter
@Getter
public class Response {
    Result result;
    Object data;

    public Response(JSONObject jsonObject, String lang) {
        result = new Result(lang);
        data = jsonObject;
    }

    @Setter
    @Getter
    private class Result{
        int code;
        String status;

        public Result(String lang) {
            code = 200;
            if (lang.equals("en"))
                status = StringResult.COMMON_200_EN;
            else
                status = StringResult.COMMON_200_FA;
        }
    }
}
