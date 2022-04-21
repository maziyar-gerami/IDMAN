package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StringResult {
  public static String COMMON_200_EN = "Successful";
  public static String COMMON_200_FA = "موفقیت آمیز بود";

  public static String COMMON_206_EN = "Partially Successful";
  public static String COMMON_206_FA = "به صورت ناکامل موفقیت آمیز بود";

  public static String COMMON_400_EN = "Unsuccessful";
  public static String COMMON_400_FA = "موفقیت آمیز نبود";

  public static String USER_406_FA = "قابلیت تغییر گذرواژه وجود ندارد.";
  public static String USER_406_EN = "Changing password is not possible";

  public static String USER_400_FA = "مشکل در پارامترهای ارسالی";
  public static String USER_400_EN = "Parameter Problem";

  public static String USER_404_FA = "کاربر مورد نظر یافت نشد";
  public static String USER_404_EN = "User not found";

  public static String PASSWORD_404_FA = "کاربر مورد نظر یافت نشد";
  public static String PASSWORD_404_EN = "User not found";

  public static String USER_300_FA = "چندین کاربر با خصوصیت وارد شده وجود دارد.";
  public static String USER_300_EN = "There are multiple users with this specified case address";

  public static String USER_302_FA = "کاربری با شناسه تعیین شده، از پیش وجود دارد.";
  public static String USER_302_EN = "User with this 'id' is repetitive";

  public static String USER_201_FA = "ایجاد کاربر موفقیت آمیز بود.";
  public static String USER_201_EN = "Creating the user was successful";

  public static String USER_204_FA = "حذف کاربر موفقیت آمیز بود.";
  public static String USER_204_EN = "Deleting the user was successful";

  public static String USER_206_FA = "حذف کاربر[ان] به صورت ناقص انجام شد.";
  public static String USER_206_EN = "Deleting the user[s] was partially successful";

  public static String USER_207_FA = "عملیات به صورت ناقص انجام شد.";
  public static String USER_207_EN = "Opration has been done, partially";

  public static String USER_403_EN = "This user have changed the first password, earlier";
  public static String USER_403_FA = "کاربر اولین رمز خود را پیشتر تغییر داده است.";

  public static String USER_417_EN = "Captcha is incorrect";
  public static String USER_417_FA = "کپچا صحیح نیست";

  public static String PASSWORD_423_FA = "تعداد تغییرات گذرواژه در یک روز بیش از حد مجاز است.";
  public static String PASSWORD_423_EN = "Password change limit exceed";

  public static String PASSWORD_302_EN = "New password is repetitive";
  public static String PASSWORD_302_FA = "گذرواژه جدید تکراری است";

  public static String PASSWORD_403_EN = "Verification code is incorrect.";
  public static String PASSWORD_403_FA = "کد تایید صحیح نیست";

  public static String PASSWORD_408_EN = "Verification code is expired.";
  public static String PASSWORD_408_FA = "کد تایید منقضی شده است";

  public static String PASSWORD_429_EN = "The number of requests to change the password in one day, is too much ";
  public static String PASSWORD_429_FA = "تعداد درخواست های تغییر گذرواژه در یک روز بیش از حد مجاز است";

  public static String GROUP_400_FA = "مشکل در پارامترهای ارسالی";
  public static String GROUP_400_EN = "Parameter Problem";

  public static String GROUP_503_FA = "مشکل در ذخیره سازی داده ها";
  public static String GROUP_503_EN = "Data saving problem";

  public static String GROUP_302_FA = "گروهی با شناسه تعیین شده، از پیش وجود دارد.";
  public static String GROUP_302_EN = "A group with this 'id' is repetitive";

  public static String GROUP_201_FA = "ایجاد گروه موفقیت آمیز بود.";
  public static String GROUP_201_EN = "Creating the group was successful";

  public static String GROUP_204_FA = "حذف گروه موفقیت آمیز بود.";
  public static String GROUP_204_EN = "Deleting the group was successful";

  public static String TOKEN_202_EN = "Token Delete Successful";
  public static String TOKEN_202_FA = "پاک کردن توکن با موفقیت انجام شد.";

  public static String AUTHENTICATOR_200_FA = "دستگاه مورد نظر یافت شد.";
  public static String AUTHENTICATOR_200_EN = "The device was found";

  public static String AUTHENTICATOR_204_FA = "دستگاه[های] مورد نظر حذف شد.";
  public static String AUTHENTICATOR_204_EN = "The device[s] was deleted";

  public static String AUTHENTICATOR_403_FA = "دستگاه یا کاربر مورد نظر یافت نشد.";
  public static String AUTHENTICATOR_403_EN = "The device or the user was not found";

  public static String AUTHENTICATOR_400_FA = "خطا در پاک کردن دستگاه مورد نظر";
  public static String AUTHENTICATOR_400_EN = "Error when deleting the device";

  public static String TICKETING_200_FA = "عملیات با موفقیت انجام شد.";
  public static String TICKETING_200_EN = "The opration was successful";

  public static String TICKETING_204_FA = "تیکت[های] مورد نظر حذف شد.";
  public static String TICKETING_204_EN = "The ticket[s] was deleted";

  public static String TICKETING_400_FA = "عملیات موفقیت آمیز نبود";
  public static String TICKETING_400_EN = "The action failed";

  public static String TICKETING_403_FA = "عمل مورد نظر مجاز نمی باشد.";
  public static String TICKETING_403_EN = "The action is not allowed";

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
