package xyz.fm.storerestapi.util;

public class PhoneNumberUtil {

    public static final String PHONE_REG_EXP = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$";

    public static String valueOf(String phoneNumber) {
        String[] split = phoneNumber.split("-");

        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(s);
        }
        return sb.toString();
    }
}
