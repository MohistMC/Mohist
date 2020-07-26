package red.mohist.util.i18n;

import red.mohist.util.I18NUtils;

import java.util.Arrays;
import java.util.Locale;

public class I18N {

    private static final String path = "assets.mohist.lang";

    /**
     * 返回对应key的值，不存在返回null
     *
     * @param key 键
     * @return 对应key的值
     */
    public static String get(String key) {
        return I18N.get(key, I18N.getLocale());
    }

    /**
     * 返回对应key的值，不存在返回 {@param def}
     *
     * @param key 键
     * @param def 不存在时的返回值
     * @return 对应key的值
     */
    public static String get(String key, String def) {
        return I18N.get(key, I18N.getLocale(), def);
    }

    /**
     * 获取当前的语言
     *
     * @return locale
     */
    private static Locale getLocale() {
        if (!Message.getCountry().equalsIgnoreCase("xx") && !Message.getLanguage().equalsIgnoreCase("xx")) {
            return new Locale(Message.getLanguage(), Message.getCountry());
        }
        return Locale.getDefault();
    }

    /**
     * 返回对应语言的key的值,不存在返回null
     *
     * @param key    键
     * @param locale 语言
     * @return 对应key的值
     */
    public static String get(String key, Locale locale) {
        return I18NUtils.getLanguages(locale, path).getProperty(key);
    }

    /**
     * 返回对应语言的key的值,不存在返回 {@param def}
     *
     * @param key    键
     * @param locale 语言
     * @param def    不存在时的返回值
     * @return 对应key的值
     */
    public static String get(String key, Locale locale, String def) {
        return I18NUtils.getLanguages(locale, path).getProperty(key, def);
    }

    /**
     * 返回对应key的值，并且将其中的{}依次换为{@param formats} 中的值
     *
     * @param key     键
     * @param formats 用作替换的内容
     * @return key对应的替换完毕的值
     */
    public static String get(String key, Object... formats) {
        return I18N.get(key, I18N.getLocale(), formats);
    }

    /**
     * 返回对应key的值，并且将其中的{}依次换为 {@param formats} 中的值
     *
     * @param key     键
     * @param formats 用作替换的内容
     * @return key对应的替换完毕的值
     */
    public static String get(String key, String def, Object... formats) {
        return I18N.get(key, I18N.getLocale(), def, formats);
    }

    /**
     * 返回对应key的值，并且将其中的{序号}依次换为 {@param formats} 中的值
     * 当语言所对应的值返回null时，将用 {@param def} 中的值进行替换
     *
     * @param key     键
     * @param locale  语言
     * @param def     不存在时返回值
     * @param formats 用作替换的内容
     * @return 语言所对应key值的替换完毕的值
     */
    public static String get(String key, Locale locale, String def, Object... formats) {
        String result = I18N.get(key, locale, def);
        for (int count = 0; count < formats.length; count++) {
            result = result.replaceAll("\\{" + count + "}", String.valueOf(formats[count]));
        }
        return result;
    }
}
