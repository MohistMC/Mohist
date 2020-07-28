package red.mohist.reampper.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pyz
 * @date 2019/7/7 12:04 PM
 */
public class ClassMapping {
    private String nmsSrcName;
    private String nmsSimpleName;
    private String nmsName;
    private String yarnSrcName;
    private String yarnName;
    private String yarnSimpleName;
    private final BiMap<String, String> fieldMapping = HashBiMap.create();
    /**
     * args nmsMethodName mcpMethodName
     */
    private final Map<String, Map<String, String>> methodMapping = new HashMap<>();
    /**
     * args mcpMethodName nmsMethodName
     */
    private final Map<String, Map<String, String>> inverseMethodMapping = new HashMap<>();
    /**
     * args nmsMethodName mcpMethodName
     */
    private final Map<String, Map<String, String>> srcMethodMapping = new HashMap<>();
    /**
     * args mcpMethodName nmsMethodName
     */
    private final Map<String, Map<String, String>> inverseSrcMethodMapping = new HashMap<>();

    public void setNmsSrcName(String nmsSrcName) {
        nmsSrcName = nmsSrcName.intern();
        this.nmsSrcName = nmsSrcName;
        int dot = this.nmsSrcName.lastIndexOf('$');
        if (dot > 0) {
            this.nmsSimpleName = this.nmsSrcName.substring(dot + 1).intern();
        } else {
            this.nmsSimpleName = this.nmsSrcName.substring(this.nmsSrcName.lastIndexOf('/') + 1).intern();
        }
        this.nmsName = this.nmsSrcName.replace('/', '.').intern();
    }

    public void setMcpSrcName(String mcpSrcName) {
        mcpSrcName = mcpSrcName.intern();
        this.yarnSrcName = mcpSrcName;
        this.yarnSrcName = mcpSrcName;
        int dot = this.yarnSrcName.lastIndexOf('$');
        if (dot > 0) {
            this.yarnSimpleName = this.yarnSrcName.substring(dot + 1).intern();
        } else {
            this.yarnSimpleName = this.yarnSrcName.substring(this.yarnSrcName.lastIndexOf('/') + 1).intern();
        }
        this.yarnName = this.yarnSrcName.replace('/', '.').intern();
    }

    public Map<String, Map<String, String>> getSrcMethodMapping() {
        return srcMethodMapping;
    }

    public Map<String, Map<String, String>> getInverseSrcMethodMapping() {
        return inverseSrcMethodMapping;
    }

    public String getNmsSrcName() {
        return nmsSrcName;
    }

    public String getNmsSimpleName() {
        return nmsSimpleName;
    }

    public String getNmsName() {
        return nmsName;
    }

    public String getMcpSrcName() {
        return yarnSrcName;
    }

    public String getMcpName() {
        return yarnName;
    }

    public String getMcpSimpleName() {
        return yarnSimpleName;
    }

    public BiMap<String, String> getFieldMapping() {
        return fieldMapping;
    }

    public Map<String, Map<String, String>> getMethodMapping() {
        return methodMapping;
    }

    public Map<String, Map<String, String>> getInverseMethodMapping() {
        return inverseMethodMapping;
    }
}
