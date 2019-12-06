package net.minecraft.launchwrapper;

public interface IClassNameTransformer {

    String unmapClassName(String name);

    String remapClassName(String name);

}
