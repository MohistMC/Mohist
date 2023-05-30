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

import com.mohistmc.mohistremap.model.ClassMapping;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import net.md_5.specialsource.CustomRemapper;
import net.md_5.specialsource.NodeType;
import net.md_5.specialsource.RemapperProcessor;
import net.md_5.specialsource.RemappingClassAdapter;
import net.md_5.specialsource.SpecialSource;
import net.md_5.specialsource.repo.ClassRepo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;


import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

/**
 *
 * @author pyz
 * @date 2019/7/3 10:38 PM
 */
public class MohistJarRemapper extends CustomRemapper {

    private RemapperProcessor preProcessor;
    public final MohistJarMapping jarMapping;
    private RemapperProcessor postProcessor;
    private final int writerFlags = COMPUTE_MAXS;
    private int readerFlags = 0;
    private boolean copyResources = true;

    @Override
    public String mapSignature(String signature, boolean typeSignature) {
        try {
            return super.mapSignature(signature, typeSignature);
        } catch (Exception e) {
            return signature;
        }
    }

    public MohistJarRemapper(RemapperProcessor preProcessor, MohistJarMapping jarMapping, RemapperProcessor postProcessor) {
        this.preProcessor = preProcessor;
        this.jarMapping = jarMapping;
        this.postProcessor = postProcessor;
    }

    public MohistJarRemapper(RemapperProcessor remapperPreprocessor, MohistJarMapping jarMapping) {
        this(remapperPreprocessor, jarMapping, null);
    }

    public MohistJarRemapper(MohistJarMapping jarMapping) {
        this.jarMapping = jarMapping;
    }

    /**
     * Enable or disable API-only generation.
     *
     * If enabled, only symbols will be output to the remapped jar, suitable for
     * use as a library. Code and resources will be excluded.
     */
    public void setGenerateAPI(boolean generateAPI) {
        if (generateAPI) {
            readerFlags |= ClassReader.SKIP_CODE;
            copyResources = false;
        } else {
            readerFlags &= ~ClassReader.SKIP_CODE;
            copyResources = true;
        }
    }

    @Override
    public String map(String typeName) {
        return mapTypeName(typeName, jarMapping.packages, jarMapping.byNMSSrcName, typeName);
    }

    public static String mapTypeName(String typeName, Map<String, String> packageMap, Map<String, ClassMapping> classMap, String defaultIfUnmapped) {
        String mapped = mapClassName(typeName, packageMap, classMap);
        return mapped != null ? mapped : defaultIfUnmapped;
    }

    /**
     * Helper method to map a class name by package (prefix) or class (exact)
     */
    private static String mapClassName(String className, Map<String, String> packageMap, Map<String, ClassMapping> classMap) {
        if (classMap != null && classMap.containsKey(className)) {
            ClassMapping mapping = classMap.get(className);
            return mapping.getMcpSrcName();
        }

        int index = className.lastIndexOf('$');
        if (index != -1) {
            String outer = className.substring(0, index);
            String mapped = mapClassName(outer, packageMap, classMap);
            if (mapped == null) {
                return null;
            }
            return mapped + className.substring(index);
        }

        if (packageMap != null) {
            for (String oldPackage : packageMap.keySet()) {
                if (matchClassPackage(oldPackage, className)) {
                    String newPackage = packageMap.get(oldPackage);

                    return moveClassPackage(newPackage, getSimpleName(oldPackage, className));
                }
            }
        }

        return null;
    }

    private static boolean matchClassPackage(String packageName, String className) {
        if (packageName.equals(".")) {
            return isDefaultPackage(className);
        }

        return className.startsWith(packageName);
    }

    private static String moveClassPackage(String packageName, String classSimpleName) {
        if (packageName.equals(".")) {
            return classSimpleName;
        }

        return packageName + classSimpleName;
    }

    private static boolean isDefaultPackage(String className) {
        return className.indexOf('/') == -1;
    }

    private static String getSimpleName(String oldPackage, String className) {
        if (oldPackage.equals(".")) {
            return className;
        }

        return className.substring(oldPackage.length());
    }

    @Override
    public String mapFieldName(String owner, String name, String desc, int access) {
        String mapped = jarMapping.tryClimb(jarMapping.fields, NodeType.FIELD, owner, name, access);
        return mapped == null ? name : mapped;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc, int access) {
        String mapped = jarMapping.tryClimb(jarMapping.methods, NodeType.METHOD, owner, name + " " + desc, access);
        return mapped == null ? name : mapped;
    }

    /**
     * Remap an individual class given an InputStream to its bytecode
     */
    public byte[] remapClassFile(InputStream is, ClassRepo repo) throws IOException {
        return remapClassFile(new ClassReader(is), repo);
    }

    public byte[] remapClassFile(byte[] in, ClassRepo repo) {
        return remapClassFile(new ClassReader(in), repo);
    }

    @SuppressWarnings("unchecked")
    private byte[] remapClassFile(ClassReader reader, final ClassRepo repo) {
        if (preProcessor != null) {
            byte[] pre = preProcessor.process(reader);
            if (pre != null) {
                reader = new ClassReader(pre);
            }
        }

        ClassNode node = new ClassNode();
        RemappingClassAdapter mapper = new RemappingClassAdapter(node, this, repo);
        reader.accept(mapper, readerFlags);

        ClassWriter wr = new ClassWriter(writerFlags);
        node.accept(wr);
        if (SpecialSource.identifier != null) {
            wr.newUTF8(SpecialSource.identifier);
        }

        return (postProcessor != null) ? postProcessor.process(wr.toByteArray()) : wr.toByteArray();
    }

}
