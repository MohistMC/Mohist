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

import com.mohistmc.mohistremap.utils.RemapUtils;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.md_5.specialsource.provider.InheritanceProvider;
import org.objectweb.asm.tree.ClassNode;

public class MohistInheritanceProvider implements InheritanceProvider {
    @Override
    public Set<String> getParents(String className) {
        if (className.startsWith("org/springframework/")) {
            return null;
        }
        return fineParents(className, true);
    }

    protected Set<String> fineParents(String className, boolean remap) {
        if (className.startsWith("net/minecraft/")) {
            return fineNMSParents(className, remap);
        } else {
            return findNormalParents(className, remap);
        }
    }

    protected Set<String> fineNMSParents(String className, boolean remap) {
        if (remap) {
            className = RemapUtils.map(className);
        }
        Set<String> parents = new HashSet<>();
        try {
            Class<?> reference = Class.forName(className.replace('/', '.'), false, this.getClass().getClassLoader());
            Class<?> extend = reference.getSuperclass();
            if (extend != null) {
                parents.add(RemapUtils.reverseMap(extend));
            }
            for (Class<?> inter : reference.getInterfaces()) {
                if (inter != null) {
                    parents.add(RemapUtils.reverseMap(inter));
                }
            }
            return parents;
        } catch (Exception e) {
            // Empty catch block
        }
        return parents;
    }

    protected Set<String> findNormalParents(String className, boolean remap) {
		//TODO: ScriptManager -> java.lang.ArrayIndexOutOfBoundsException: 27210
        ClassNode cn = MohistClassRepo.getInstance().findClass(className);
        if (cn == null) {
            if (!remap) {
                return null;
            }
            String remapClassName = RemapUtils.map(className);
            if (Objects.equals(remapClassName, className)) {
                return null;
            }
            return fineParents(remapClassName, false);
        }
        Set<String> parents = new HashSet<>();
        if (cn.superName != null) {
            parents.add(RemapUtils.reverseMap(cn.superName));
        }
        if (cn.interfaces != null) {
            for (String anInterface : cn.interfaces) {
                parents.add(RemapUtils.reverseMap(anInterface));
            }
        }
        return parents.isEmpty() ? null : parents;
    }
}