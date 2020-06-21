package red.mohist.util;


import java.util.regex.Pattern;


/**
 * some Regular
 */
public class RegularExpressionUtil
{


    /**
     * 判断source是否符合pattern的正则
     * @param pattern  正则的规则
     * @param source  输入字符串
     * @return
     */
    public static boolean patternMatch(Pattern pattern, String source)
    {
        if (pattern == null)
            return false;
        return pattern.matcher(source).matches();
    }

}
