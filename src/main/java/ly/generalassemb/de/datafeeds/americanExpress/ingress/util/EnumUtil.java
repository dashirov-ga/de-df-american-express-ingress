package ly.generalassemb.de.datafeeds.americanExpress.ingress.util;

import java.util.ArrayList;
import java.util.List;

public class EnumUtil {
    public static <T extends Enum<T>> List<T> stringToEnums(Class<T> clazz, List<String> values) {
        List<T> list = new ArrayList<>();
        for (String level : values) {
            list.add(Enum.valueOf(clazz, level));
        }
        return list;
    }
}
