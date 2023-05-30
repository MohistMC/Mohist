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

import com.mohistmc.mohistremap.utils.RemapUtils;
import java.security.ProtectionDomain;
import net.md_5.specialsource.repo.RuntimeRepo;

public class DelegateClassLoder extends ClassLoader{

    public static final String desc = DelegateClassLoder.class.getName().replace('.', '/');

    protected DelegateClassLoder() {
        super();
    }

    protected DelegateClassLoder(ClassLoader parent) {
        super(parent);
    }

    public final Class<?> defineClassMohist(byte[] b, int off, int len) throws ClassFormatError {
        return defineClassMohist(null, b, off, len, null);
    }

    public final Class<?> defineClassMohist(String name, byte[] b, int off, int len) throws ClassFormatError {
        return defineClassMohist(name, b, off, len, null);
    }

    public final Class<?> defineClassMohist(String name, java.nio.ByteBuffer b, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (!b.isDirect() && b.hasArray()) {
            return remappedFindClass(name, b.array(), protectionDomain);
        }
        return defineClass(name, b, protectionDomain);
    }

    public final Class<?> defineClassMohist(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) throws ClassFormatError {
        if (off == 0) {
            return remappedFindClass(name, b, protectionDomain);
        }

        return defineClass(name, b, off, len, protectionDomain);
    }

    private Class<?> remappedFindClass(String name, byte[] stream, ProtectionDomain protectionDomain) throws ClassFormatError {
        Class<?> result = null;
        try {
            byte[] bytecode = RemapUtils.jarRemapper.remapClassFile(stream, RuntimeRepo.getInstance());
            bytecode = RemapUtils.remapFindClass(bytecode);
            result = this.defineClass(name, bytecode, 0, bytecode.length, protectionDomain);
        } catch (Throwable t) {
            throw new ClassFormatError("Failed to remap class " + name);
        }
        return result;
    }
}
