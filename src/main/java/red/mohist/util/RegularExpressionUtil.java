package red.mohist.util;


import java.util.regex.Pattern;


/**
 * some Regular
 */
public class RegularExpressionUtil
{
    //pwd strong check
    public final static Pattern password = Pattern.compile("^(?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$");
    //
    public final static Pattern checkChinese = Pattern.compile("^[\\\\u4e00-\\\\u9fa5]{0,}$");
    //common text
    public final static Pattern common = Pattern.compile("^\\\\w+$");
    //E-Mail
    public final static Pattern email = Pattern.compile("[\\\\w!#$%&'*+/=?^_`{|}~-]+(?:\\\\.[\\\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\\\w](?:[\\\\w-]*[\\\\w])?\\\\.)+[\\\\w](?:[\\\\w-]*[\\\\w])?");
    //Number
    public final static Pattern Number = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\\\d{8}$");
    //ID
    public final static Pattern ID = Pattern.compile("^[1-9]\\\\d{5}[1-9]\\\\d{3}((0\\\\d)|(1[0-2]))(([0|1|2]\\\\d)|3[0-1])\\\\d{3}([0-9]|X)$");
    //date
    public final static Pattern date = Pattern.compile("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$");
    //IPv4
    public final static Pattern IPV4 = Pattern.compile("\\\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\\\b");
    //IPv6
    public final static Pattern IPV6 = Pattern.compile("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))");
    //UrlpPrefix
    public final static Pattern URLPre = Pattern.compile("/^[a-zA-Z]+:\\\\/\\\\//");
    //getUrl
    public final static Pattern URLContent = Pattern.compile("^(f|ht){1}(tp|tps):\\\\/\\\\/([\\\\w-]+\\\\.)+[\\\\w-]+(\\\\/[\\\\w ./?%&=]*)?");
    //File path and stuff
    public final static Pattern filePathEx = Pattern.compile("^([a-zA-Z]\\\\:|\\\\\\\\)\\\\\\\\([^\\\\\\\\]+\\\\\\\\)*[^\\\\/:*?\"<>|]+\\\\.txt(l)?$");
    //color hex
    public final static Pattern ColorHexCodes = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    //notes
    public final static Pattern notes = Pattern.compile("<#(.*?)");
    //html tag
    public final static Pattern htmlTag = Pattern.compile("<\\\\/?\\\\w+((\\\\s+\\\\w+(\\\\s*=\\\\s*(?:\".*?\"|'.*?'|[\\\\^'\">\\\\s]+))?)+\\\\s*|\\\\s*)\\\\/?>");


    /**
     * Determine whether source meets the regular pattern
     * @param pattern  Regular rules
     * @param source  Input string
     * @return
     */
    public static boolean patternMatch(Pattern pattern, String source)
    {
        if (pattern == null)
            return false;
        return pattern.matcher(source).matches();
    }

}
