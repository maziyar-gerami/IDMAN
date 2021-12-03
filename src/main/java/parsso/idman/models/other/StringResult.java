package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;
import parsso.idman.helpers.Variables;

import java.util.Locale;

@Setter
@Getter
public class StringResult {
    public static String COMMON_200_EN = "Successful";
    public static String COMMON_200_FA = "موفقیت آمیز بود";

    public static String TOKEN_202_EN = "Token Delete Successful";
    public static String TOKEN_202_FA = "پاک کردن توکن با موفقیت انجام شد.";

    public static String get(int code, String model,String lang) throws NoSuchFieldException, IllegalAccessException {
        String name = model.toUpperCase()+"_"+code+"_"+lang.toUpperCase();
        StringResult stringResult = new StringResult();
        return (String) stringResult.getClass().getField(name).get(String.class);
    }
}
