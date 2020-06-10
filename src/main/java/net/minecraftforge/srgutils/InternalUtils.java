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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class InternalUtils {
    static IMappingFile load(InputStream in) throws IOException {
        List<String> lines = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines()
            .map(InternalUtils::stripComment)
            .filter(l -> !l.isEmpty()) //Remove Empty lines
            .collect(Collectors.toList());

        MappingFile ret = new MappingFile();

        String firstLine = lines.get(0);
        String test = firstLine.split(" ")[0];
        if ("PK:".equals(test) || "CL:".equals(test) || "FD:".equals(test) || "MD:".equals(test)) { //SRG
            for (String line : lines) {
                String[] pts = line.split(" ");
                switch (pts[0]) {
                    case "PK:": ret.addPackage(pts[1], pts[2]); break;
                    case "CL:": ret.addClass(pts[1], pts[2]); break;
                    case "FD:":
                        if (pts.length == 5)
                            ret.getOrCreateClass(rsplit(pts[1], '/', 1)[0]).addField(rsplit(pts[1], '/', 1)[1], rsplit(pts[3], '/', 1)[1], pts[2]);
                        else
                            ret.getOrCreateClass(rsplit(pts[1], '/', 1)[0]).addField(rsplit(pts[1], '/', 1)[1], rsplit(pts[2], '/', 1)[1]);
                        break;
                    case "MD:": ret.getOrCreateClass(rsplit(pts[1], '/', 1)[0]).addMethod(rsplit(pts[1], '/', 1)[1], pts[2], rsplit(pts[3], '/', 1)[1]); break;
                    default:
                        throw new IOException("Invalid SRG file, Unknown type: " + line);
                }
            }
        } else if(firstLine.contains(" -> ")) { // ProGuard
            for (String line : lines) {
                if (!line.startsWith("    ") && line.endsWith(":")) {
                    String[] pts = line.replace('.', '/').split(" -> ");
                    ret.addClass(pts[0], pts[1].substring(0, pts[1].length() - 1));
                }
            }

            MappingFile.Cls cls = null;
            for (String line : lines) {
                line = line.replace('.', '/');
                if (!line.startsWith("    ") && line.endsWith(":")) {
                    //Classes we already did this in the first pass
                    cls = ret.getClass(line.split(" -> ")[0]);
                } else if (line.contains("(") && line.contains(")")) {
                    if (cls == null)
                        throw new IOException("Invalid PG line, missing class: " + line);

                    line = line.trim();
                    int start = 0;
                    int end = 0;
                    if (line.indexOf(':') != -1) {
                        int i = line.indexOf(':');
                        int j = line.indexOf(':', i + 1);
                        start = Integer.parseInt(line.substring(0,     i));
                        end   = Integer.parseInt(line.substring(i + 1, j));
                        line = line.substring(j + 1);
                    }

                    String obf = line.split(" -> ")[1];
                    String _ret = toDesc(line.split(" ")[0]);
                    String name = line.substring(line.indexOf(' ') + 1, line.indexOf('('));
                    String[] args = line.substring(line.indexOf('(') + 1, line.indexOf(')')).split(",");

                    StringBuffer desc = new StringBuffer();
                    desc.append('(');
                    for (String arg : args) {
                        if (arg.isEmpty()) break;
                        desc.append(toDesc(arg));
                    }
                    desc.append(')').append(_ret);

                    /*
                    if (("<init>".equals(name) || "<clinit>".equals(name)) && name.equals(obf))
                        ; // We don't care about initializers, they keep their name by virtue of the JVM spec.
                    else
                    */
                        cls.addMethod(name, desc.toString(), obf, start, end);
                } else {
                    if (cls == null)
                        throw new IOException("Invalid PG line, missing class: " + line);
                    String[] pts = line.trim().split(" ");
                    cls.addField(pts[1], pts[3], toDesc(pts[0]));
                }
            }
        } else { // TSRG/CSRG
            lines.stream().filter(l -> l.charAt(0) != '\t')
            .map(l -> l.split(" "))
            .filter(pts -> pts.length == 2)
            .forEach(pts -> {
                if (pts[0].endsWith("/"))
                    ret.addPackage(pts[0].substring(0, pts[0].length() - 1), pts[1].substring(0, pts[1].length() -1));
                else
                    ret.addClass(pts[0], pts[1]);
            });

            MappingFile.Cls cls = null;
            for (String line : lines) {
                String[] pts = line.split(" ");
                if (pts[0].charAt(0) == '\t') {
                    if (cls == null)
                        throw new IOException("Invalid TSRG line, missing class: " + line);
                    pts[0] = pts[0].substring(1);
                    if (pts.length == 2)
                        cls.addField(pts[0], pts[1]);
                    else if (pts.length == 3)
                        cls.addMethod(pts[0], pts[1], pts[2]);
                    else
                        throw new IOException("Invalid TSRG line, to many parts: " + line);
                } else {
                    if (pts.length == 2) {
                        if (!pts[0].endsWith("/"))
                            cls = ret.getClass(pts[0]);
                    }
                    else if (pts.length == 3)
                        ret.getClass(pts[0]).addField(pts[1], pts[2]);
                    else if (pts.length == 4)
                        ret.getClass(pts[0]).addMethod(pts[1], pts[2], pts[3]);
                    else
                        throw new IOException("Invalid CSRG line, to many parts: " + line);
                }
            }
        }
        return ret;
    }

    static String toDesc(String type) {
        if (type.endsWith("[]"))    return "[" + toDesc(type.substring(0, type.length() - 2));
        if (type.equals("int"))     return "I";
        if (type.equals("void"))    return "V";
        if (type.equals("boolean")) return "Z";
        if (type.equals("byte"))    return "B";
        if (type.equals("char"))    return "C";
        if (type.equals("short"))   return "S";
        if (type.equals("double"))  return "D";
        if (type.equals("float"))   return "F";
        if (type.equals("long"))    return "J";
        if (type.contains("/"))     return "L" + type + ";";
        throw new RuntimeException("Invalid toDesc input: " + type);
    }

    static String toSource(String desc) {
        char first = desc.charAt(0);
        switch (first) {
            case 'I': return "int";
            case 'V': return "void";
            case 'Z': return "boolean";
            case 'B': return "byte";
            case 'C': return "char";
            case 'S': return "short";
            case 'D': return "double";
            case 'F': return "float";
            case 'J': return "long";
            case '[': return toSource(desc.substring(1)) + "[]";
            case 'L': return desc.substring(1, desc.length() - 1).replace('/', '.');
            default: throw new IllegalArgumentException("Unknown descriptor: " + desc);
        }
    }

    static String toSource(String name, String desc) {
        StringBuilder buf = new StringBuilder();
        int endParams = desc.lastIndexOf(')');
        String ret = desc.substring(endParams + 1);
        buf.append(toSource(ret)).append(' ').append(name).append('(');

        int idx = 1;
        while (idx < endParams) {
            int array = 0;
            char c = desc.charAt(idx);
            if (c == '[') {
                while (desc.charAt(idx) == '[') {
                    array++;
                    idx++;
                }
                c = desc.charAt(idx);
            }
            if (c == 'L') {
                int end = desc.indexOf(';', idx);
                buf.append(toSource(desc.substring(idx, end + 1)));
                idx = end;
            } else {
                buf.append(toSource(c + ""));
            }

            while (array-- > 0)
                buf.append("[]");

            idx++;
            if (idx < endParams)
                buf.append(',');
        }
        buf.append(')');
        return buf.toString();
    }

    private static String[] rsplit(String str, char chr, int count) {
        List<String> pts = new ArrayList<>();
        int idx;
        while ((idx = str.lastIndexOf(chr)) != -1 && count > 0) {
            pts.add(str.substring(idx + 1));
            str = str.substring(0, idx);
            count--;
        }
        pts.add(str);
        Collections.reverse(pts);
        return pts.toArray(new String[pts.size()]);
    }

    private static final List<String> ORDER = Arrays.asList("PK:", "CL:", "FD:", "MD:");
    public static int compareLines(String o1, String o2) {
        String[] pt1 = o1.split(" ");
        String[] pt2 = o2.split(" ");
        if (!pt1[0].equals(pt2[0]))
            return ORDER.indexOf(pt1[0]) - ORDER.lastIndexOf(pt2[0]);

        if ("PK:".equals(pt1[0]))
            return o1.compareTo(o2);
        if ("CL:".equals(pt1[0]))
            return compareCls(pt1[1], pt2[1]);
        if ("FD:".equals(pt1[0]) || "MD:".equals(pt1[0]))
        {
            String[][] y = {
                {pt1[1].substring(0, pt1[1].lastIndexOf('/')), pt1[1].substring(pt1[1].lastIndexOf('/') + 1)},
                {pt2[1].substring(0, pt2[1].lastIndexOf('/')), pt2[1].substring(pt2[1].lastIndexOf('/') + 1)}
            };
            int ret = compareCls(y[0][0], y[1][0]);
            if (ret != 0)
                return ret;
            return y[0][1].compareTo(y[1][1]);
        }
        return o1.compareTo(o2);
    }

    public static int compareCls(String cls1, String cls2) {
        if (cls1.indexOf('/') > 0 && cls2.indexOf('/') > 0)
            return cls1.compareTo(cls2);
        String[][] t = { cls1.split("\\$"), cls2.split("\\$") };
        int max = Math.min(t[0].length, t[1].length);
        for (int i = 0; i < max; i++)
        {
            if (!t[0][i].equals(t[1][i]))
            {
                if (t[0][i].length() != t[1][i].length())
                    return t[0][i].length() - t[1][i].length();
                return t[0][i].compareTo(t[1][i]);
            }
        }
        return Integer.compare(t[0].length, t[1].length);
    }

    public static String stripComment(String str) {
        int idx = str.indexOf('#');
        if (idx == 0)
            return "";
        if (idx != -1)
            str = str.substring(0, idx - 1);
        int end = str.length();
        while (end > 1 && str.charAt(end - 1) == ' ')
            end--;
        return end == 0 ? "" : str.substring(0, end);
    }
}
