package red.mohist.reampper.model;

/**
 * @author pyz
 * @date 2019/6/30 11:50 PM
 */
public class MethodRedirectRule {
    private final String owner;
    private final String desc;
    private final String name;
    private final String remapOwner;

    public MethodRedirectRule(String owner, String name, String desc, String remapOwner) {
        this.owner = owner;
        this.desc = desc;
        this.name = name;
        this.remapOwner = remapOwner;
    }

    public String getRemapOwner() {
        return remapOwner;
    }

    public String getOwner() {
        return owner;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }
}
