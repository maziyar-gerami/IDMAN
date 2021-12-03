package parsso.idman.models.other;

import parsso.idman.helpers.Variables;

public class StringResult {
    public static String COMMON_200_EN = "Successful";
    public static String COMMON_200_FA = "موفقیت آمیز بود";

    public static String TOKEN_202_EN = "Token Delete Successful";
    public static String TOKEN_202_FA = "پاک کردن توکن با موفقیت انجام شد.";

    public static String get(String model,String lang){
        if (model.equals(Variables.MODEL_TOKEN)){
            if (lang.equals("fa"))
                return TOKEN_202_FA;
            else
                return TOKEN_202_EN;
        }
        return null;
    }
}
