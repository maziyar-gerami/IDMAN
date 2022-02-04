package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StringResult {
    public static String COMMON_200_EN = "Successful";
    public static String COMMON_200_FA = "موفقیت آمیز بود";

    public static String COMMON_400_EN = "Unsuccessful";
    public static String COMMON_400_FA = "موفقیت آمیز نبود";

    public static String USER_400_FA = "مشکل در پارامترهای ارسالی";
    public static String USER_400_EN = "Parameter Problem";

    public static String USER_423_FA = "تعداد تغییرات گذرواژه در یک روز بیش از حد مجاز است.";
    public static String USER_423_EN = "Password change limit exceed";

    public static String TOKEN_202_EN = "Token Delete Successful";
    public static String TOKEN_202_FA = "پاک کردن توکن با موفقیت انجام شد.";

    public static String get(int code, String model, String lang) throws NoSuchFieldException, IllegalAccessException {
        String name = model.toUpperCase() + "_" + code + "_" + lang.toUpperCase();
        try {
            return (String) StringResult.class.getField(name).get(String.class);
        } catch (Exception e) {
            name = "COMMON" + "_" + code + "_" + lang.toUpperCase();
            return (String) StringResult.class.getField(name).get(String.class);
        }
    }
}
