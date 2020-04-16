package red.mohist.bukkit.nms.cache;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pyz
 * @date 2019/7/7 12:04 PM
 */
public class ClassMapping {

    /**
     * MD:
     */
    public static HashMap<String, String> map_MD = Maps.newHashMap();
    public static Map<String, Class> VirtualMethod = Maps.newHashMap();
    public static Map<String, Class> VirtualMethodToStatic = Maps.newHashMap();
    public static HashMap<String, String> classDeMapping = Maps.newHashMap();
    public static Multimap<String, String> methodDeMapping = ArrayListMultimap.create();
    public static Multimap<String, String> fieldDeMapping = ArrayListMultimap.create();
    public static Multimap<String, String> methodFastMapping = ArrayListMultimap.create();

}
