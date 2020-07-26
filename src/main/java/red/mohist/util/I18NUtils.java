package red.mohist.util;

import lombok.SneakyThrows;
import red.mohist.util.i18n.LocalizedException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

public class I18NUtils {

    /**
     * 获取指定路径的语言文件
     *
     * @param locale 要获取的语言
     * @param path   例: lang.mohist = /lang/mohist/
     * @return 对应语言的Properties文件
     */
    public static Properties getLanguages(String locale, String path) {
        return I18NUtils.getLanguages(new Locale(locale), path);
    }

    /**
     * 获取指定路径的语言文件
     *
     * @param locale 要获取的语言
     * @param path   例: lang.mohist = /lang/mohist/
     * @return 对应语言的Properties文件
     */
    @SneakyThrows
    public static Properties getLanguages(Locale locale, String path) {
        Properties language = new Properties();
        String p = "/" + path.replace(".", "/") + "/" + locale.getLanguage() + "_" + locale.getCountry() + ".lang";
        if (!FileUtil.has(p)) {
            if (locale != Locale.US) return getLanguages(Locale.US, path);
            throw new RuntimeException("The default lang file not found, please redownload mohist");
        }
        InputStream is = FileUtil.read(p);
        language.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        return language;
    }
}
