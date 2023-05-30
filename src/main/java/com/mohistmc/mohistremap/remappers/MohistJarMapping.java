/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.mohistremap.remappers;

import com.google.common.collect.BiMap;
import com.mohistmc.mohistremap.model.ClassMapping;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import net.md_5.specialsource.InheritanceMap;
import net.md_5.specialsource.NodeType;
import net.md_5.specialsource.provider.InheritanceProvider;
import net.md_5.specialsource.transformer.MappingTransformer;
import net.md_5.specialsource.transformer.MavenShade;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Remapper;

/**
 *
 * @author pyz
 * @date 2019/7/2 10:02 PM
 */
public class MohistJarMapping implements ClassRemapperSupplier {

    public final Map<String, ClassMapping> byNMSInternalName = new HashMap<>();
    public final Map<String, ClassMapping> byNMSSrcName = new HashMap<>();
    public final Map<String, ClassMapping> byNMSName = new HashMap<>();
    public final Map<String, ClassMapping> byMCPName = new HashMap<>();
    public final Map<String, String> classes = new HashMap<>();
    public final Map<String, String> fields = new HashMap<>();
    public final Map<String, String> methods = new HashMap<>();
    public final Map<String, String> fastMapping = new HashMap<>();

    public MohistJarMapping() {
    }

    public void initFastMethodMapping(Remapper remapper) {
        for (ClassMapping classMapping : byNMSSrcName.values()) {
            classMapping.getSrcMethodMapping().forEach((nmsSrcMethodDescriptor, map) -> {
                String methodDesc = nmsSrcMethodDescriptor;
                methodDesc = remapper.mapMethodDesc(methodDesc);
                Type[] ts = Type.getArgumentTypes(methodDesc);
                StringJoiner sj = new StringJoiner(",");
                for (Type t : ts) {
                    String part = t.getClassName();
                    if (part.contains("[]")) {
                        sj.add(t.getInternalName());
                    } else {
                        sj.add(part);
                    }
                }
                String methodArgumentsDesc = sj.toString().intern();
                map.forEach((k, v) -> {
                    classMapping.getMethodMapping()
                            .computeIfAbsent(methodArgumentsDesc, kk -> new HashMap<>())
                            .put(k, v);
                    classMapping.getInverseMethodMapping()
                            .computeIfAbsent(methodArgumentsDesc, kk -> new HashMap<>())
                            .put(v, k);
                });
            });
        }
    }

    public String fastMapFieldName(Class<?> clazz, String name) {
        String mapped = fastMapFieldName(false, clazz, clazz.getName() + " " + name, name);
        if (mapped == null) {
            return name;
        } else {
            return mapped;
        }
    }

    public String fastReverseMapFieldName(Class<?> clazz, String name) {
        String mapped = fastMapFieldName(true, clazz, clazz.getName() + " " + name, name);
        if (mapped == null) {
            return name;
        } else {
            return mapped;
        }
    }

    public String fastMapMethodName(Class<?> clazz, String name, Class<?>... args) {
        String mapped = fastMapMethodName(false, clazz, clazz.getName() + " " + name + " " + join(args), name, args);
        if (mapped == null) {
            return name;
        } else {
            return mapped;
        }
    }

    public String fastReverseMapMethodName(Class<?> clazz, String name, Class<?>... args) {
        String mapped = fastMapMethodName(true, clazz, clazz.getName() + " " + name + " " + join(args), name, args);
        if (mapped == null) {
            return name;
        } else {
            return mapped;
        }
    }

    private String fastMapFieldName(boolean inverse, Class<?> clazz, String key, String name) {
        if (clazz == null) {
            return null;
        }
        String mapName = fastMapping.get(key);
        if (mapName != null) {
            return mapName;
        }
        mapName = directFastMapFieldName(inverse, clazz, name);
        if (mapName != null) {
            fastMapping.put(key.intern(), mapName.intern());
            return mapName;
        }
        mapName = fastMapMethodName(inverse, clazz.getSuperclass(), key, name);
        return mapName;
    }

    private String directFastMapFieldName(boolean inverse, Class<?> clazz, String name) {
        String className = clazz.getName();
        if (!className.startsWith("net.minecraft.")) {
            return null;
        }
        ClassMapping mapping = byMCPName.get(clazz.getName());
        if (mapping == null) {
            return null;
        }
        BiMap<String, String> map = mapping.getFieldMapping();
        if (inverse) {
            map = map.inverse();
        }
        return map.get(name);
    }

    private String fastMapMethodName(boolean inverse, Class<?> clazz, String key, String name, Class<?>... args) {
        if (clazz == null) {
            return null;
        }
        String mapName = fastMapping.get(key);
        if (mapName != null) {
            return mapName;
        }
        mapName = directFastMapMethodName(inverse, clazz, name, args);
        if (mapName != null) {
            fastMapping.put(key.intern(), mapName.intern());
            return mapName;
        }
        mapName = fastMapMethodName(inverse, clazz.getSuperclass(), key, name, args);
        if (mapName != null) {
            return mapName;
        }
        for (Class<?> aClass : clazz.getInterfaces()) {
            mapName = fastMapMethodName(inverse, aClass, key, name, args);
            if (mapName != null) {
                return mapName;
            }
        }
        return null;
    }

    private String directFastMapMethodName(boolean inverse, Class<?> clazz, String name, Class<?>... args) {
        String className = clazz.getName();
        if (!className.startsWith("net.minecraft.")) {
            return null;
        }
        ClassMapping mapping = byMCPName.get(clazz.getName());
        if (mapping != null) {
            Map<String, String> map;
            if (inverse) {
                map = mapping.getInverseMethodMapping().get(join(args));
            } else {
                map = mapping.getMethodMapping().get(join(args));
            }
            if (map != null) {
                return map.get(name);
            }
        }
        return null;

    }

    private String join(Class<?>... args) {
        if (args == null) {
            return "";
        }
        StringJoiner sj = new StringJoiner(",");
        for (Class<?> arg : args) {
            sj.add(arg.getName());
        }
        return sj.toString();
    }

    public final LinkedHashMap<String, String> packages = new LinkedHashMap<>();
    protected InheritanceMap inheritanceMap = new InheritanceMap();
    protected InheritanceProvider fallbackInheritanceProvider = null;
    protected String currentClass = null;


    /**
     * Set the inheritance map used for caching superclass/interfaces. This call
     * be omitted to use a local cache, or set to your own global cache.
     */
    public void setInheritanceMap(InheritanceMap inheritanceMap) {
        this.inheritanceMap = inheritanceMap;
    }

    /**
     * Set the inheritance provider to be consulted if the inheritance map has
     * no information on the requested class (results will be cached in the
     * inheritance map).
     */
    public void setFallbackInheritanceProvider(InheritanceProvider fallbackInheritanceProvider) {
        this.fallbackInheritanceProvider = fallbackInheritanceProvider;
    }

    public String tryClimb(Map<String, String> map, NodeType type, String owner, String name, int access) {
        String key = owner + "/" + name;

        String mapped = map.get(key);
        if (mapped == null && (access == -1 || (!Modifier.isPrivate(access) && !Modifier.isStatic(access)))) {
            Collection<String> parents = null;

            if (inheritanceMap.hasParents(owner)) {
                parents = inheritanceMap.getParents(owner);
            } else if (fallbackInheritanceProvider != null) {
                parents = fallbackInheritanceProvider.getParents(owner);
                inheritanceMap.setParents(owner, parents);
            }

            if (parents != null) {
                // climb the inheritance tree
                for (String parent : parents) {
                    mapped = tryClimb(map, type, parent, name, access);
                    if (mapped != null) {
                        return mapped;
                    }
                }
            }
        }
        return mapped;
    }

    /**
     * Load a mapping given a .csrg file
     *
     * @param reader Mapping file reader
     * @param inputTransformer Transformation to apply on input
     * @param outputTransformer Transformation to apply on output
     * @param reverse Swap input and output mappings (after applying any
     * input/output transformations)
     * @throws IOException
     */
    public void loadMappings(BufferedReader reader, MappingTransformer inputTransformer, MappingTransformer outputTransformer, boolean reverse) throws IOException {
        if (inputTransformer == null) {
            inputTransformer = MavenShade.IDENTITY;
        }
        if (outputTransformer == null) {
            outputTransformer = MavenShade.IDENTITY;
        }

        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            int commentIndex = line.indexOf('#');
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex);
            }
            if (line.isEmpty()) {
                continue;
            }
            lines.add(line);
        }


        //Gather class mappings here so that we can support reversing csrg/tsrg.
        final Map<String, String> clsMap = new HashMap<>();
        for (String l : lines) {
            if (l.contains(":")) {
                if (!l.startsWith("CL:")) {
                    continue;
                }
                String[] tokens = l.split(" ");
                clsMap.put(tokens[1], tokens[1]);
            } else {
                if (l.startsWith("\t")) {
                    continue;
                }
                String[] tokens = l.split(" ");
                clsMap.put(tokens[0], tokens[1]);
            }
        }

        // We use a Remapper so that we don't have to duplicate the logic of remapping method descriptors.
        Remapper reverseMapper = new Remapper() {
            @Override
            public String map(String cls) {
                return clsMap.getOrDefault(cls, cls);
            }
        };

        for (String l : lines) {
            if (l.contains(":")) {
                // standard srg
                parseSrgLine(l, inputTransformer, outputTransformer);
            } else {
                // better 'compact' srg format
                parseCsrgLine(l, inputTransformer, outputTransformer, reverseMapper);
            }
        }

        currentClass = null;
    }

    public ClassMapping registerClassMapping(String nmsSrcName, String mcpSrcName) {
        nmsSrcName = nmsSrcName.intern();
        mcpSrcName = mcpSrcName.intern();
        classes.put(nmsSrcName, mcpSrcName);
        ClassMapping mapping = byNMSSrcName.get(nmsSrcName);
        if (mapping == null) {
            mapping = new ClassMapping();
            mapping.setNmsSrcName(nmsSrcName);
            mapping.setMcpSrcName(mcpSrcName);
            byNMSSrcName.put(mapping.getNmsSrcName(), mapping);
            byNMSInternalName.put(mapping.getNmsSrcName(), mapping);
            byMCPName.put(mapping.getMcpName(), mapping);
            byNMSName.put(mapping.getNmsName(), mapping);
        }
        return mapping;
    }

    public void registerFieldMapping(String nmsSrcName, String nmsName, String mcpSrcName, String mcpName) {
        nmsName = nmsName.intern();
        mcpName = mcpName.intern();
        fields.put((nmsSrcName + "/" + nmsName).intern(), mcpName);
        ClassMapping mapping = registerClassMapping(nmsSrcName, mcpSrcName);
        mapping.getFieldMapping().put(nmsName, mcpName);
    }

    public void registerMethodMapping(String nmsSrcName, String nmsName, String nmsSrcMethodDescriptor, String mcpSrcName, String mcpName, String mcpSrcMethodDescriptor) {
        nmsName = nmsName.intern();
        mcpName = mcpName.intern();
        nmsSrcMethodDescriptor = nmsSrcMethodDescriptor.intern();
        methods.put((nmsSrcName + "/" + nmsName + " " + nmsSrcMethodDescriptor).intern(), mcpName);
        ClassMapping mapping = registerClassMapping(nmsSrcName, mcpSrcName);
        mapping.getSrcMethodMapping()
                .computeIfAbsent(nmsSrcMethodDescriptor, kk -> new HashMap<>())
                .put(nmsName, mcpName);
        mapping.getInverseSrcMethodMapping()
                .computeIfAbsent(nmsSrcMethodDescriptor, kk -> new HashMap<>())
                .put(mcpName, nmsName);
    }

    /**
     * Parse a 'csrg' mapping format line and populate the data structures
     */
    private void parseCsrgLine(String line, MappingTransformer inputTransformer, MappingTransformer outputTransformer, Remapper reverseMap) throws IOException {
        //Tsrg format, identical to Csrg, except the field and method lines start with \t and should use the last class the was parsed.
        if (line.startsWith("\t")) {
            if (this.currentClass == null) {
                throw new IOException("Invalid tsrg file, tsrg field/method line before class line: " + line);
            }
            line = currentClass + " " + line.substring(1);
        }

        String[] tokens = line.split(" ");

        if (tokens.length == 2) {
            String oldClassName = inputTransformer.transformClassName(tokens[0]);
            String newClassName = outputTransformer.transformClassName(tokens[1]);

            if (oldClassName.endsWith("/")) {
                // Special case: mapping an entire hierarchy of classes
                packages.put(oldClassName.substring(0, oldClassName.length() - 1), newClassName);
            } else {
                registerClassMapping(oldClassName, newClassName);
                currentClass = tokens[0];
            }
        } else if (tokens.length == 3) {
            String oldClassName = inputTransformer.transformClassName(tokens[0]);
            ClassMapping mapping = byNMSSrcName.get(oldClassName);
            String newClassName = mapping == null ? oldClassName : mapping.getMcpSrcName();
            String oldFieldName = inputTransformer.transformFieldName(tokens[0], tokens[1]);
            String newFieldName = outputTransformer.transformFieldName(tokens[0], tokens[2]);

            registerFieldMapping(oldClassName, oldFieldName, newClassName, newFieldName);
        } else if (tokens.length == 4) {
            String oldClassName = inputTransformer.transformClassName(tokens[0]);
            String oldMethodName = inputTransformer.transformMethodName(tokens[0], tokens[1], tokens[2]);
            String oldMethodDescriptor = inputTransformer.transformMethodDescriptor(tokens[2]);
            ClassMapping mapping = byNMSSrcName.get(oldClassName);
            String newClassName = mapping == null ? oldClassName : mapping.getMcpSrcName();
            String newMethodName = outputTransformer.transformMethodName(tokens[0], tokens[3], tokens[2]);
            registerMethodMapping(oldClassName, oldMethodName, oldMethodDescriptor, newClassName, newMethodName, null);
        } else {
            throw new IOException("Invalid csrg file line, token count " + tokens.length + " unexpected in " + line);
        }
    }

    /**
     * Parse a standard 'srg' mapping format line and populate the data
     * structures
     */
    private void parseSrgLine(String line, MappingTransformer inputTransformer, MappingTransformer outputTransformer) throws IOException {
        String[] tokens = line.split(" ");
        String kind = tokens[0];

        switch (kind) {
            case "CL:" -> {
                String oldClassName = inputTransformer.transformClassName(tokens[1]);
                String newClassName = outputTransformer.transformClassName(tokens[2]);


                if (byNMSSrcName.containsKey(oldClassName) && !newClassName.equals(byNMSSrcName.get(oldClassName).getNmsSrcName())) {
                    throw new IllegalArgumentException("Duplicate class mapping: " + oldClassName + " -> " + newClassName
                            + " but already mapped to " + byNMSSrcName.get(oldClassName) + " in line=" + line);
                }

                if (oldClassName.endsWith("/*") && newClassName.endsWith("/*")) {
                    // extension for remapping class name prefixes
                    oldClassName = oldClassName.substring(0, oldClassName.length() - 1);
                    newClassName = newClassName.substring(0, newClassName.length() - 1);

                    packages.put(oldClassName, newClassName);
                } else {
                    registerClassMapping(oldClassName, newClassName);
                    currentClass = tokens[1];
                }
            }
            case "PK:" -> {
                String oldPackageName = inputTransformer.transformClassName(tokens[1]);
                String newPackageName = outputTransformer.transformClassName(tokens[2]);

                // package names always either 1) suffixed with '/', or 2) equal to '.' to signify default package
                if (!newPackageName.equals(".") && !newPackageName.endsWith("/")) {
                    newPackageName += "/";
                }
                if (!oldPackageName.equals(".") && !oldPackageName.endsWith("/")) {
                    oldPackageName += "/";
                }
                if (packages.containsKey(oldPackageName) && !newPackageName.equals(packages.get(oldPackageName))) {
                    throw new IllegalArgumentException("Duplicate package mapping: " + oldPackageName + " ->" + newPackageName
                            + " but already mapped to " + packages.get(oldPackageName) + " in line=" + line);
                }
                packages.put(oldPackageName, newPackageName);
            }
            case "FD:" -> {
                String oldFull = tokens[1];
                String newFull = tokens[2];

                // Split the qualified field names into their classes and actual names
                int splitOld = oldFull.lastIndexOf('/');
                int splitNew = newFull.lastIndexOf('/');
                if (splitOld == -1 || splitNew == -1) {
                    throw new IllegalArgumentException("Field name is invalid, not fully-qualified: " + oldFull
                            + " -> " + newFull + " in line=" + line);
                }

                String oldClassName = inputTransformer.transformClassName(oldFull.substring(0, splitOld));
                String oldFieldName = inputTransformer.transformFieldName(oldFull.substring(0, splitOld), oldFull.substring(splitOld + 1));
                String newClassName = outputTransformer.transformClassName(newFull.substring(0, splitNew)); // TODO: verify with existing class map? (only used for reverse)

                String newFieldName = outputTransformer.transformFieldName(oldFull.substring(0, splitOld), newFull.substring(splitNew + 1));

                registerFieldMapping(oldClassName, oldFieldName, newClassName, newFieldName);
            }
            case "MD:" -> {
                String oldFull = tokens[1];
                String newFull = tokens[3];

                // Split the qualified field names into their classes and actual names TODO: refactor with below
                int splitOld = oldFull.lastIndexOf('/');
                int splitNew = newFull.lastIndexOf('/');
                if (splitOld == -1 || splitNew == -1) {
                    throw new IllegalArgumentException("Field name is invalid, not fully-qualified: " + oldFull
                            + " -> " + newFull + " in line=" + line);
                }

                String oldClassName = inputTransformer.transformClassName(oldFull.substring(0, splitOld));
                String oldMethodName = inputTransformer.transformMethodName(oldFull.substring(0, splitOld), oldFull.substring(splitOld + 1), tokens[2]);
                String oldMethodDescriptor = inputTransformer.transformMethodDescriptor(tokens[2]);
                String newClassName = outputTransformer.transformClassName(newFull.substring(0, splitNew)); // TODO: verify with existing class map? (only used for reverse)

                String newMethodName = outputTransformer.transformMethodName(oldFull.substring(0, splitOld), newFull.substring(splitNew + 1), tokens[2]);
                String newMethodDescriptor = outputTransformer.transformMethodDescriptor(tokens[4]); // TODO: verify with existing class map? (only used for reverse)

                // TODO: support isClassIgnored() on reversed method descriptors

                registerMethodMapping(oldClassName, oldMethodName, oldMethodDescriptor, newClassName, newMethodName, newMethodDescriptor);
            }
            default ->
                    throw new IllegalArgumentException("Unable to parse srg file, unrecognized mapping type in line=" + line);
        }
    }

}
