/*
 * SRG Utils
 * Copyright (c) 2019
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package red.mohist.bukkit.nms.utils.srgutils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Locale;

public interface IMappingFile {
    static IMappingFile load(File path) throws IOException {
        try (InputStream in = new FileInputStream(path)) {
            return load(in);
        }
    }

    static IMappingFile load(InputStream in) throws IOException {
        return InternalUtils.load(in);
    }

    Collection<? extends IPackage> getPackages();

    IPackage getPackage(String original);

    Collection<? extends IClass> getClasses();

    IClass getClass(String original);

    String remapPackage(String pkg);

    String remapClass(String desc);

    String remapDescriptor(String desc);

    void write(Path path, Format format, boolean reversed) throws IOException;

    IMappingFile reverse();

    IMappingFile rename(IRenamer renamer);

    IMappingFile chain(IMappingFile other);

    enum Format {
        SRG(false),
        XSRG(false),
        CSRG(false),
        TSRG(true),
        PG(true);

        private boolean ordered = true;

        Format(boolean ordered) {
            this.ordered = ordered;
        }

        public static Format get(String name) {
            name = name.toUpperCase(Locale.ENGLISH);
            for (Format value : values())
                if (value.name().equals(name))
                    return value;
            return null;
        }

        public boolean isOrdered() {
            return this.ordered;
        }
    }

    interface INode {
        String getOriginal();

        String getMapped();

        String write(Format format, boolean reversed);
    }

    interface IPackage extends INode {
    }

    interface IClass extends INode {
        Collection<? extends IField> getFields();

        Collection<? extends IMethod> getMethods();

        String remapField(String field);

        String remapMethod(String name, String desc);
    }

    interface IOwnedNode<T> extends INode {
        T getParent();
    }

    interface IField extends IOwnedNode<IClass> {
        @Nullable
        String getDescriptor();

        @Nullable
        String getMappedDescriptor();
    }

    interface IMethod extends IOwnedNode<IClass> {
        String getDescriptor();

        String getMappedDescriptor();
    }
}
