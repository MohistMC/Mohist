package com.mohistmc.bukkit.remapping;

import org.objectweb.asm.tree.ClassNode;

/**
 * PluginTransformer
 *
 * @author Mainly by IzzelAliz
 * @originalClassName PluginTransformer
 */
@FunctionalInterface
public interface PluginTransformer {

    void handleClass(ClassNode node, ClassLoaderRemapper remapper);

    default int priority() {
        return 0;
    }
}
