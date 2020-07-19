package red.mohist.bukkit.nms.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pyz
 * @date 2019/7/7 12:04 PM
 */
public class ClassMapping {
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
    private String nmsSrcName;
    private String nmsSimpleName;
    private String nmsName;
    private String mcpSrcName;
    private String mcpName;
    private String mcpSimpleName;

    public Map<String, Map<String, String>> getSrcMethodMapping() {
        return srcMethodMapping;
    }

    public Map<String, Map<String, String>> getInverseSrcMethodMapping() {
        return inverseSrcMethodMapping;
    }

    public String getNmsSrcName() {
        return nmsSrcName;
    }

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

    public String getNmsSimpleName() {
        return nmsSimpleName;
    }

    public String getNmsName() {
        return nmsName;
    }

    public String getMcpSrcName() {
        return mcpSrcName;
    }

    public void setMcpSrcName(String mcpSrcName) {
        mcpSrcName = mcpSrcName.intern();
        this.mcpSrcName = mcpSrcName;
        this.mcpSrcName = mcpSrcName;
        int dot = this.mcpSrcName.lastIndexOf('$');
        if (dot > 0) {
            this.mcpSimpleName = this.mcpSrcName.substring(dot + 1).intern();
        } else {
            this.mcpSimpleName = this.mcpSrcName.substring(this.mcpSrcName.lastIndexOf('/') + 1).intern();
        }
        this.mcpName = this.mcpSrcName.replace('/', '.').intern();
    }

    public String getMcpName() {
        return mcpName;
    }

    public String getMcpSimpleName() {
        return mcpSimpleName;
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
