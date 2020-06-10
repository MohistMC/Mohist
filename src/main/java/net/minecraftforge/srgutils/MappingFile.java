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

package net.minecraftforge.srgutils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

class MappingFile implements IMappingFile {
    private Map<String, Package> packages = new HashMap<>();
    private Collection<Package> packagesView = Collections.unmodifiableCollection(packages.values());
    private Map<String, Cls> classes = new HashMap<>();
    private Collection<Cls> classesView = Collections.unmodifiableCollection(classes.values());
    private Map<String, String> cache = new HashMap<>();
    private static final Pattern DESC = Pattern.compile("L(?<cls>[^;]+);");

    @Override
    public Collection<Package> getPackages() {
        return this.packagesView;
    }

    @Override
    @Nullable
    public Package getPackage(String original) {
        return packages.get(original);
    }

    Package addPackage(String original, String mapped) {
        return packages.put(original, new Package(original, mapped));
    }

    @Override
    public Collection<Cls> getClasses() {
        return this.classesView;
    }

    @Override
    @Nullable
    public Cls getClass(String original) {
        return classes.get(original);
    }

    Cls addClass(String original, String mapped) {
        Cls ret = new Cls(original, mapped);
        classes.put(original, ret);
        return ret;
    }

    Cls getOrCreateClass(String original) {
        return classes.computeIfAbsent(original, k -> new Cls(original, original));
    }

    @Override
    public String remapPackage(String pkg) {
        //TODO: Package bulk moves? Issue: moving default package will move EVERYTHING, it's what its meant to do but we shouldn't.
        Package ipkg = packages.get(pkg);
        return ipkg == null ? pkg : ipkg.getMapped();
    }

    @Override
    public String remapClass(String cls) {
        String ret = cache.get(cls);
        if (ret == null) {
            Cls _cls = classes.get(cls);
            if (_cls == null) {
                int idx = cls.lastIndexOf('$');
                if (idx != -1)
                    ret = remapClass(cls.substring(0, idx)) + '$' + cls.substring(idx + 1);
                else
                    ret = cls;
            } else
                ret = _cls.getMapped();
            //TODO: Package bulk moves? Issue: moving default package will move EVERYTHING, it's what its meant to do but we shouldn't.
            cache.put(cls, ret);
        }
        return ret;
    }

    @Override
    public String remapDescriptor(String desc) {
        Matcher matcher = DESC.matcher(desc);
        StringBuffer buf = new StringBuffer();
        while (matcher.find())
            matcher.appendReplacement(buf, Matcher.quoteReplacement("L" + remapClass(matcher.group("cls")) + ";"));
        matcher.appendTail(buf);
        return buf.toString();
    }

    @Override
    public void write(Path path, Format format, boolean reversed) throws IOException {
        List<String> lines = new ArrayList<>();
        Comparator<INode> sort = reversed ? (a,b) -> a.getMapped().compareTo(b.getMapped()) : (a,b) -> a.getOriginal().compareTo(b.getOriginal());
        getPackages().stream().sorted(sort).map(e -> e.write(format, reversed)).forEachOrdered(lines::add);
        getClasses().stream().sorted(sort).forEachOrdered(cls -> {
            lines.add(cls.write(format, reversed));
            cls.getFields().stream().sorted(sort).map(e -> e.write(format, reversed)).forEachOrdered(lines::add);
            cls.getMethods().stream().sorted(sort).map(e -> e.write(format, reversed)).forEachOrdered(lines::add);
        });
        if (!format.isOrdered()) {
            Comparator<String> linesort = (format == Format.SRG || format == Format.XSRG) ? InternalUtils::compareLines : (o1, o2) -> o1.compareTo(o2);
            Collections.sort(lines, linesort);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String line : lines) {
                writer.write(line);
                writer.write('\n');
            }
        }
    }

    @Override
    public MappingFile reverse() {
        MappingFile ret = new MappingFile();
        getPackages().stream().forEach(e -> ret.addPackage(e.getMapped(), e.getOriginal()));
        getClasses().stream().forEach(cls -> {
            Cls c = ret.addClass(cls.getMapped(), cls.getOriginal());
            cls.getFields().stream().forEach(fld -> c.addField(fld.getMapped(), fld.getOriginal(), fld.getMappedDescriptor()));
            cls.getMethods().stream().forEach(mtd -> c.addMethod(mtd.getMapped(), mtd.getMappedDescriptor(), mtd.getOriginal(), mtd.start, mtd.end));
        });
        return ret;
    }

    @Override
    public MappingFile rename(IRenamer renamer) {
        MappingFile ret = new MappingFile();
        getPackages().stream().forEach(e -> ret.addPackage(e.getOriginal(), renamer.rename(e)));
        getClasses().stream().forEach(cls -> {
            Cls c = ret.addClass(cls.getOriginal(), renamer.rename(cls));
            cls.getFields().stream().forEach(fld -> c.addField(fld.getOriginal(), renamer.rename(fld), fld.getDescriptor()));
            cls.getMethods().stream().forEach(mtd -> c.addMethod(mtd.getOriginal(), mtd.getDescriptor(), renamer.rename(mtd), mtd.start, mtd.end));
        });
        return ret;
    }

    @Override
    public MappingFile chain(final IMappingFile link) {
        return rename(new IRenamer() {
            public String rename(IPackage value) {
                return link.remapPackage(value.getMapped());
            }

            public String rename(IClass value) {
                return link.remapClass(value.getMapped());
            }

            public String rename(IField value) {
                IClass cls = link.getClass(value.getParent().getMapped());
                return cls == null ? value.getMapped() : cls.remapField(value.getMapped());
            }

            public String rename(IMethod value) {
                IClass cls = link.getClass(value.getParent().getMapped());
                return cls == null ? value.getMapped() : cls.remapMethod(value.getMapped(), value.getMappedDescriptor());
            }
        });
    }

    abstract class Node implements INode {
        protected String original;
        protected String mapped;

        protected Node(String original, String mapped) {
            this.original = original;
            this.mapped = mapped;
        }

        @Override
        public String getOriginal() {
            return this.original;
        }

        @Override
        public String getMapped() {
            return this.mapped;
        }
    }

    class Package extends Node implements IPackage {
        protected Package(String original, String mapped) {
            super(original, mapped);
        }

        @Override
        public String write(Format format, boolean reversed) {
            String sorig = getOriginal().isEmpty() ? "." : getOriginal();
            String smap = getMapped().isEmpty() ? "." : getMapped();

            if (reversed) {
                String tmp = sorig;
                sorig = smap;
                smap = tmp;
            }

            switch (format) {
                case SRG:
                case XSRG: return "PK: " + sorig + ' ' + smap;
                case CSRG:
                case TSRG: return getOriginal() + "/ " + getMapped() + '/';
                case PG: throw new UnsupportedOperationException("ProGuard format does not support packages.");
                default: throw new UnsupportedOperationException("Unknown format: " + format);
            }
        }

        @Override
        public String toString() {
            return this.write(Format.SRG, false);
        }
    }

    class Cls extends Node implements IClass {
        private Map<String, Field> fields = new HashMap<>();
        private Collection<Field> fieldsView = Collections.unmodifiableCollection(fields.values());
        private Map<String, Method> methods = new HashMap<>();
        private Collection<Method> methodsView = Collections.unmodifiableCollection(methods.values());

        protected Cls(String original, String mapped) {
            super(original, mapped);
        }

        @Override
        public String write(Format format, boolean reversed) {
            String oName = !reversed ? getOriginal() : getMapped();
            String mName = !reversed ? getMapped() : getOriginal();
            switch (format) {
                case SRG:
                case XSRG: return "CL: " + oName + ' ' + mName;
                case CSRG:
                case TSRG: return oName + ' ' + mName;
                case PG: return oName.replace('/', '.') + " -> " + mName.replace('/', '.') + ':';
                default: throw new UnsupportedOperationException("Unknown format: " + format);
            }
        }

        @Override
        public Collection<Field> getFields() {
            return this.fieldsView;
        }

        @Override
        public String remapField(String field) {
            Field fld = fields.get(field);
            return fld  == null ? field : fld.getMapped();
        }

        void addField(String original, String mapped) {
            this.fields.put(original, new Field(original, mapped));
        }

        void addField(String original, String mapped, String desc) {
            this.fields.put(original, new Field(original, mapped, desc));
        }

        @Override
        public Collection<Method> getMethods() {
            return this.methodsView;
        }

        void addMethod(String original, String desc, String mapped) {
            this.methods.put(original + desc, new Method(original, desc, mapped));
        }

        void addMethod(String original, String desc, String mapped, int start, int end) {
            this.methods.put(original + desc, new Method(original, desc, mapped, start, end));
        }

        @Override
        public String remapMethod(String name, String desc) {
            Method mtd = methods.get(name + desc);
            return mtd == null ? name : mtd.getMapped();
        }

        @Override
        public String toString() {
            return this.write(Format.SRG, false);
        }

        class Field extends Node implements IField {
            private String desc;

            private Field(String original, String mapped) {
                this(original, mapped, null);
            }

            private Field(String original, String mapped, String desc) {
                super(original, mapped);
                this.desc = desc;
            }

            @Override
            public String getDescriptor() {
                return desc;
            }

            @Override
            public String getMappedDescriptor() {
                return this.desc == null ? null : MappingFile.this.remapDescriptor(this.desc);
            }

            @Override
            public String write(Format format, boolean reversed) {
                if ((format == Format.PG || format == Format.XSRG) && this.desc == null)
                    throw new IllegalStateException("Can not write ProGuard format, field is missing descriptor");

                String oOwner = !reversed ? Cls.this.getOriginal() : Cls.this.getMapped();
                String mOwner = !reversed ? Cls.this.getMapped() : Cls.this.getOriginal();
                String oName = !reversed ? this.getOriginal() : this.getMapped();
                String mName = !reversed ? this.getMapped() : this.getOriginal();
                String oDesc = !reversed ? this.getDescriptor() : this.getMappedDescriptor();
                String mDesc = !reversed ? this.getMappedDescriptor() : this.getDescriptor();

                switch (format) {
                    case SRG:  return "FD: " + oOwner+ '/' + oName + ' ' + mOwner + '/' + mName + (oDesc == null ? "" : " # " + oDesc + " " + mDesc);
                    case XSRG: return "FD: " + oOwner + '/' + oName + (oDesc == null ? "" : ' ' + oDesc) + ' ' + mOwner + '/' + mName + (mDesc == null ? "" : ' ' + mDesc);
                    case CSRG: return oOwner + ' ' + oName + ' ' + mName;
                    case TSRG: return '\t' + oName + ' ' + mName;
                    case PG:   return "    " + InternalUtils.toSource(oDesc) + ' ' + oName + " -> " + mName;
                    default: throw new UnsupportedOperationException("Unknown format: " + format);
                }
            }

            @Override
            public String toString() {
                return this.write(Format.SRG, false);
            }

            @Override
            public Cls getParent() {
                return Cls.this;
            }
        }

        class Method extends Node implements IMethod {
            private String desc;
            private int start, end = 0;

            private Method(String original, String desc, String mapped) {
                this(original, desc, mapped, 0, 0);
            }

            private Method(String original, String desc, String mapped, int start, int end) {
                super(original, mapped);
                this.desc = desc;
                this.start = start;
                this.end = end;
            }

            @Override
            public String getDescriptor() {
                return this.desc;
            }
            @Override
            public String getMappedDescriptor() {
                return MappingFile.this.remapDescriptor(this.desc);
            }

            @Override
            public String write(Format format, boolean reversed) {
                String oName = !reversed ? getOriginal() : getMapped();
                String mName = !reversed ? getMapped() : getOriginal();
                String oOwner = !reversed ? Cls.this.getOriginal() : Cls.this.getMapped();
                String mOwner = !reversed ? Cls.this.getMapped() : Cls.this.getOriginal();
                String oDesc = !reversed ? getDescriptor() : getMappedDescriptor();
                String mDesc = !reversed ? getMappedDescriptor() : getDescriptor();

                switch (format) {
                    case SRG:
                    case XSRG: return "MD: " + oOwner + '/' + oName + ' ' + oDesc + ' ' + mOwner + '/' + mName + ' ' + mDesc;
                    case CSRG: return oOwner + ' ' + oName + ' ' + oDesc + ' ' + mName;
                    case TSRG: return '\t' + oName + ' ' + oDesc + ' ' + mName;
                    case PG:   return "    " + (start == 0 && end == 0 ? "" : start + ":" + end + ":") + InternalUtils.toSource(oName, oDesc) + " -> " + mName;
                    default: throw new UnsupportedOperationException("Unknown format: " + format);
                }
            }

            @Override
            public String toString() {
                return this.write(Format.SRG, false);
            }

            @Override
            public Cls getParent() {
                return Cls.this;
            }
        }
    }
}
