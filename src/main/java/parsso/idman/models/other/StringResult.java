package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import parsso.idman.helpers.Variables;

import java.util.Locale;

@Setter
@Getter
public class StringResult {
    public static String COMMON_200_EN = "Successful";
    public static String COMMON_200_FA = "موفقیت آمیز بود";

    public static String USER_400_FA = "مشکل در پارامترهای ارسالی";
    public static String USER_400_EN = "Parameter Problem";

    public static String TOKEN_202_EN = "Token Delete Successful";
    public static String TOKEN_202_FA = "پاک کردن توکن با موفقیت انجام شد.";

    public static String get(int code, String model,String lang) throws NoSuchFieldException, IllegalAccessException {
        String name = model.toUpperCase()+"_"+code+"_"+lang.toUpperCase();
        final StringResult stringResult = new StringResult();
        return (String) stringResult.getClass().getField(name).get(String.class);
    }
}
