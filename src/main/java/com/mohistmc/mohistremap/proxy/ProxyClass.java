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

package com.mohistmc.mohistremap.proxy;


import com.mohistmc.mohistremap.utils.ASMUtils;
import com.mohistmc.mohistremap.utils.ReflectionUtils;
import com.mohistmc.mohistremap.utils.RemapUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author pyz
 * @date 2019/7/1 12:24 AM
 */
public class ProxyClass {

    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true, ReflectionUtils.getCallerClassLoader());
    }

    public static Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(ASMUtils.toClassName(RemapUtils.map(className.replace('.', '/'))), initialize, loader);
    }

    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.needRemap(clazz.getName())) {
            name = RemapUtils.mapMethodName(clazz, name, parameterTypes);
        }
        return clazz.getDeclaredMethod(name, parameterTypes);
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        if (RemapUtils.needRemap(clazz.getName())) {
            name = RemapUtils.mapMethodName(clazz, name, parameterTypes);
        }
        return clazz.getMethod(name, parameterTypes);
    }

    public static Field getDeclaredField(Class<?> clazz, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.needRemap(clazz.getName())) {
            name = RemapUtils.mapFieldName(clazz, name);
        }
        return clazz.getDeclaredField(name);
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException, SecurityException {
        if (RemapUtils.needRemap(clazz.getName())) {
            name = RemapUtils.mapFieldName(clazz, name);
        }
        return clazz.getField(name);
    }

    public static String getName(Class<?> clazz) {
        return RemapUtils.inverseMapName(clazz);
    }

    public static String getSimpleName(Class<?> clazz) {
        return RemapUtils.inverseMapSimpleName(clazz);
    }

    public static Method[] getDeclaredMethods(Class<?> inst) {
        try {
            return inst.getDeclaredMethods();
        } catch (NoClassDefFoundError e) {
            return new Method[]{};
        }
    }

    public static String getName(Field field) {
        return RemapUtils.inverseMapFieldName(field.getDeclaringClass(), field.getName());
    }

    public static String getName(Method method) {
        return RemapUtils.inverseMapMethodName(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
    }

    public static Class<?> loadClass(ClassLoader inst, String className) throws ClassNotFoundException {
        if (className.startsWith("net.minecraft.")) {
            className = ASMUtils.toClassName(RemapUtils.map(ASMUtils.toInternalName(className)));
        }
        return inst.loadClass(className);
    }
}
