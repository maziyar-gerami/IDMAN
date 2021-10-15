package parsso.idman.Utils.SMS.KaveNegar.utils;


import java.util.List;

public class StringUtils {
    public static String join(CharSequence delimiter, List<?> elements) {
        if (elements == null || elements.isEmpty()) {
            return "";
        }
        // Number of elements not likely worth Arrays.stream overhead.
        StringBuilder result = new StringBuilder(elements.get(0).toString());
        for (int i = 1; i < elements.size(); i++) {
            result.append(delimiter).append(elements.get(i).toString());
        }
        return result.toString();
    }

}
