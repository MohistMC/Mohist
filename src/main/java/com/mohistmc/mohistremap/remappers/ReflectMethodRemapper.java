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

import com.google.common.collect.Maps;
import com.mohistmc.mohistremap.model.MethodRedirectRule;
import com.mohistmc.mohistremap.proxy.ProxyClass;
import com.mohistmc.mohistremap.proxy.ProxyMethodHandlesLookup;
import com.mohistmc.mohistremap.proxy.asm.ProxyClassWriter;
import com.mohistmc.mohistremap.utils.ASMUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 *
 * @author pyz
 * @date 2019/7/2 8:51 PM
 */
public class ReflectMethodRemapper extends MethodRemapper {

    private static final Map<String, Map<String, Map<String, MethodRedirectRule>>> methodRedirectMapping = new HashMap<>();
    private static final Map<String, Class<?>> virtualMethod = Maps.newHashMap();
    public static final String className = "java/lang/Class";
    public static final String LookupName = "java/lang/invoke/MethodHandles$Lookup";
    public static final String reflectName = "java/lang/reflect/";
    public static Set<String> proxyClass = new HashSet<>(Arrays.asList(className + ";getField", className + ";getDeclaredField", className + ";getMethod", className + ";getDeclaredMethod", className + ";getSimpleName"));
    public static Set<String> Lookup = new HashSet<>(Arrays.asList(className + ";unreflect", className + ";findSpecial", className + ";findStatic", className + ";findVirtual", className + ";findGetter", className + ";findSetter", className + ";findStaticGetter", className + ";findStaticSetter", className + ";findVarHandle"));
    public static Set<String> reflect = new HashSet<>(Arrays.asList(reflectName + "Method;getName", reflectName + "Field;getName"));



    public static void init() {
        for (String s : proxyClass) {
            virtualMethod.put(s, ProxyClass.class);
        }
        for (String s : Lookup) {
            virtualMethod.put(s, ProxyMethodHandlesLookup.class);
        }
        for (String s : reflect) {
            virtualMethod.put(s, ProxyClass.class);
        }
        virtualMethod.put(ASMUtils.classLoaderdesc + ";loadClass", ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "forName", Class.class, new Class[]{String.class}, ProxyClass.class);
        registerMethodRemapper("java/lang/Class", "forName", Class.class, new Class[]{String.class, Boolean.TYPE, ClassLoader.class}, ProxyClass.class);
        registerMethodRemapper(className, "getField", Field.class, new Class[]{String.class}, ProxyClass.class);
        registerMethodRemapper(className, "getDeclaredField", Field.class, new Class[]{String.class}, ProxyClass.class);
        registerMethodRemapper(className, "getMethod", Method.class, new Class[]{String.class, Class[].class}, ProxyClass.class);
        registerMethodRemapper(className, "getDeclaredMethod", Method.class, new Class[]{String.class, Class[].class}, ProxyClass.class);
        registerMethodRemapper(className, "getName", String.class, new Class[]{}, ProxyClass.class);
        registerMethodRemapper(className, "getSimpleName", String.class, new Class[]{}, ProxyClass.class);
        registerMethodRemapper(className, "getDeclaredMethods", Method.class, new Class[]{}, ProxyClass.class);
        registerMethodRemapper(reflectName + "Method", "getName", String.class, new Class[]{}, ProxyClass.class);
        registerMethodRemapper(reflectName + "Field", "getName", String.class, new Class[]{}, ProxyClass.class);

        registerMethodRemapper("java/lang/invoke/MethodType", "fromMethodDescriptorString", MethodType.class, new Class[]{String.class, ClassLoader.class}, ProxyMethodHandlesLookup.class);
        registerMethodRemapper(LookupName, "unreflect", MethodHandle.class, new Class[]{String.class, ClassLoader.class}, ProxyMethodHandlesLookup.class);
        registerMethodRemapper(LookupName, "findSpecial", MethodHandle.class, new Class[]{Class.class, String.class, MethodType.class, Class.class}, ProxyMethodHandlesLookup.class);
        registerMethodRemapper(LookupName, "findStatic", MethodHandle.class, new Class[]{Class.class, String.class, MethodType.class}, ProxyMethodHandlesLookup.class);
        registerMethodRemapper(LookupName, "findVirtual", MethodHandle.class, new Class[]{Class.class, String.class, MethodType.class}, ProxyMethodHandlesLookup.class);
        registerMethodRemapper(LookupName, "findGetter", MethodHandle.class, new Class[]{Class.class, String.class, Class.class}, ProxyMethodHandlesLookup.class);
        registerMethodRemapper(LookupName, "findSetter", MethodHandle.class, new Class[]{Class.class, String.class, Class.class}, ProxyMethodHandlesLookup.class);
        registerMethodRemapper(LookupName, "findStaticGetter", MethodHandle.class, new Class[]{Class.class, String.class, Class.class}, ProxyMethodHandlesLookup.class);
        registerMethodRemapper(LookupName, "findStaticSetter", MethodHandle.class, new Class[]{Class.class, String.class, Class.class}, ProxyMethodHandlesLookup.class);

        registerMethodRemapper(LookupName, "findVarHandle", VarHandle.class, new Class[]{Class.class, String.class, MethodType.class, Class.class}, ProxyMethodHandlesLookup.class);
    }

    public ReflectMethodRemapper(MethodVisitor mv, Remapper remapper) {
        super(mv, remapper);
    }

    public ReflectMethodRemapper(int api, MethodVisitor mv, Remapper remapper) {
        super(api, mv, remapper);
    }

    private static void registerMethodRemapper(String owner, String name, Class<?> returnType, Class<?>[] args, Class<?> remapOwner) {
        Map<String, Map<String, MethodRedirectRule>> byName = methodRedirectMapping.computeIfAbsent(owner, k -> new HashMap<>());
        Map<String, MethodRedirectRule> byDesc = byName.computeIfAbsent(name, k -> new HashMap<>());
        String methodDescriptor = ASMUtils.toMethodDescriptor(returnType, args);
        byDesc.put(methodDescriptor, new MethodRedirectRule(owner, name, methodDescriptor, remapOwner.getName().replace('.', '/')));
    }

    public static Map<String, Class<?>> getVirtualMethod() {
        return virtualMethod;
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (Opcodes.INVOKEVIRTUAL == opcode) {
            redirectVirtual(opcode, owner, name, desc, itf);
        } else if (Opcodes.INVOKESTATIC == opcode) {
            redirectStatic(opcode, owner, name, desc, itf);
        } else if (Opcodes.INVOKESPECIAL == opcode) {
            redirectSpecial(opcode, owner, name, desc, itf);
        } else {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

    private MethodRedirectRule findRule(int opcode, String owner, String name, String desc, boolean itf) {
        Map<String, Map<String, MethodRedirectRule>> byOwner = methodRedirectMapping.get(owner);
        if (byOwner == null) {
            return null;
        }
        Map<String, MethodRedirectRule> byName = byOwner.get(name);
        if (byName == null) {
            return null;
        }
        MethodRedirectRule rule = byName.get(desc);
        return rule;
    }

    private void redirectSpecial(int opcode, String owner, String name, String desc, boolean itf) {
        MethodRedirectRule rule = findRule(opcode, owner, name, desc, itf);
        if (rule != null) {
            owner = rule.remapOwner();
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    private void redirectVirtual(int opcode, String owner, String name, String desc, boolean itf) {
        if (desc.equals("()[B")) {
            if (name.equals("toByteArray")) {
                if (owner.equals("com/comphenix/net/sf/cglib/asm/$ClassWriter")) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, ProxyClassWriter.class.getName().replace('.', '/'), "remapClass", "([B)[B", false);
                    return;
                }
            }
        }
        MethodRedirectRule rule = findRule(opcode, owner, name, desc, itf);
        if (rule != null) {
            opcode = Opcodes.INVOKESTATIC;
            Type r = Type.getReturnType(desc);
            Type[] args = Type.getArgumentTypes(desc);
            Type[] newArgs = new Type[args.length + 1];
            newArgs[0] = Type.getObjectType(owner);

            owner = rule.remapOwner();
            System.arraycopy(args, 0, newArgs, 1, args.length);
            desc = Type.getMethodDescriptor(r, newArgs);
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    private void redirectStatic(int opcode, String owner, String name, String desc, boolean itf) {
        MethodRedirectRule rule = findRule(opcode, owner, name, desc, itf);
        if (rule != null) {
            owner = rule.remapOwner();
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

}