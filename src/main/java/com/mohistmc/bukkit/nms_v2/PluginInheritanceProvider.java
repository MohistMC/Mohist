package com.mohistmc.bukkit.nms_v2;

import com.google.common.collect.ImmutableSet;
import net.md_5.specialsource.provider.InheritanceProvider;
import net.md_5.specialsource.repo.ClassRepo;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * PluginInheritanceProvider
 *
 * @author Mainly by IzzelAliz and modified Mgazul
 * @originalClassName PluginInheritanceProvider
 * @classFrom <a href="https://github.com/IzzelAliz/Arclight/blob/1.19/arclight-common/src/main/java/io/izzel/arclight/common/mod/util/remapper/PluginInheritanceProvider.java">Click here to get to github</a>
 * <p>
 * These classes are modified by MohistMC to support the Mohist software.
 */
public class PluginInheritanceProvider implements InheritanceProvider {

    private static final Map<String, Collection<String>> SHARED_INHERITANCE_MAP = new ConcurrentHashMap<>();

    private final ClassRepo classRepo;

    public PluginInheritanceProvider(ClassRepo classRepo) {
        this.classRepo = classRepo;
    }

    @Override
    public Collection<String> getParents(String className) {
        ClassNode node = classRepo.findClass(className);
        if (node == null) {
            return Collections.emptyList();
        }

        Collection<String> parents = new HashSet<>(node.interfaces);
        if (node.superName != null) {
            parents.add(node.superName);
        }

        return parents;
    }

    public Collection<String> getAll(String className) {
        Collection<String> collection = SHARED_INHERITANCE_MAP.get(className);
        if (collection != null) {
            return collection;
        }

        ClassNode node = classRepo.findClass(className);
        if (node == null) {
            return ImmutableSet.of("java/lang/Object");
        }
        Collection<String> parents = new HashSet<>(node.interfaces);
        parents.add(node.name);
        if (node.superName != null) {
            parents.add(node.superName);
            parents.addAll(getAll(node.superName));
        } else {
            parents.add("java/lang/Object");
        }

        SHARED_INHERITANCE_MAP.put(className, parents);
        return parents;
    }

    public static class Remapping extends PluginInheritanceProvider {

        private final PluginInheritanceProvider provider;

        public Remapping(ClassRepo classRepo, PluginInheritanceProvider provider) {
            super(classRepo);
            this.provider = provider;
        }

        @Override
        public Collection<String> getAll(String className) {
            return provider.getAll(className).stream().map(MohistRemapper.getNmsMapper()::map).collect(Collectors.toSet());
        }

        @Override
        public Collection<String> getParents(String className) {
            return provider.getParents(className).stream().map(MohistRemapper.getNmsMapper()::map).collect(Collectors.toSet());
        }
    }
}
