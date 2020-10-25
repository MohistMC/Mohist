package com.mohistmc.api.filestream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBT {
    public static Map<String, Object> read(InputStream in) throws IOException {
        return read(in, true);
    }
    public static Map<String, Object> read(InputStream in, boolean compressed) throws IOException {
        DataInputStream data = compressed
                ? new DataInputStream(new GZIPInputStream(in))
                : new DataInputStream(new BufferedInputStream(in));
        try {
            return read( (DataInput)data );
        } finally {
            data.close();
        }
    }
    public static void write(OutputStream out, Map<String, Object> map) throws IOException {
        write(out, map, true);
    }
    public static void write(OutputStream out, Map<String, Object> map, boolean compressed) throws IOException {
        DataOutputStream data = compressed
                ? new DataOutputStream(new GZIPOutputStream(out))
                : new DataOutputStream(new BufferedOutputStream(out));
        try {
            write((OutputStream) data, map );
        } finally {
            data.close();
        }
    }
    private static Map<String, Object> read(DataInput in) throws IOException {
        if (in.readByte() != 10)    // assume TypeID of NBT::TAG_Compound
            throw new IOException("Root tag must be a named compound tag");

        String name = readString(in);

        Map<String, Object> map = readCompound(in);
        if (!name.isEmpty()) {
            map.put("$this.name", name);
        }

        return map;
    }

    private static Object readTag(DataInput in, byte type) throws IOException {
        switch (type) {
            case 1: return in.readByte();
            case 2: return in.readShort();
            case 3: return in.readInt();
            case 4: return in.readLong();
            case 5: return in.readFloat();
            case 6: return in.readDouble();

            case 7: return readByteArray(in);
            case 8: return readString(in);
            case 9: return readList(in);
            case 10: return readCompound(in);
            case 11: return readIntArray(in);
            default: throw new IOException("Invalid NBT tag type (1-10): " + type);
        }
    }

    private static int[] readIntArray(DataInput in) throws IOException {
        int[] data = new int[ in.readInt() ];
        for (int i = 0; i < data.length; i++)
            data[i] = in.readInt();
        return data;
    }

    private static byte[] readByteArray(DataInput in) throws IOException {
        byte[] data = new byte[ in.readInt() ];
        in.readFully(data);
        return data;
    }

    private static String readString(DataInput in) throws IOException {
        return in.readUTF();
    }

    private static List<Object> readList(DataInput in) throws IOException {
        byte type = in.readByte();
        int length = in.readInt();
        List<Object> list = new ArrayList<Object>(length);
        for (int i = 0; i < length; ++i) {
            Object tag = readTag(in, type);
            list.add(tag);
        }
        return list;
    }

    private static Map<String, Object> readCompound(DataInput in) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        for (byte type; (type = in.readByte()) != 0; ) {
            String name = readString(in);
            Object tag = readTag(in, type);
            map.put(name, tag);
        }
        return map;
    }

    private static byte whichType(Object tag) {
        if (tag instanceof Byte) return 1;
        if (tag instanceof Short) return 2;
        if (tag instanceof Integer) return 3;
        if (tag instanceof Long) return 4;
        if (tag instanceof Float) return 5;
        if (tag instanceof Double) return 6;

        if (tag instanceof byte[]) return 7;
        if (tag instanceof String) return 8;
        if (tag instanceof List<?>) return 9;
        if (tag instanceof Map<?, ?>) return 10;
        if (tag instanceof int[]) return 11;

        throw new RuntimeException("Cannot serialize unknown type " + tag.getClass());
    }

    @SuppressWarnings("unchecked")
    private static void writeTag(DataOutput out, byte type, Object tag) throws IOException {
        switch (type) {
            case 1: out.writeByte( (Byte)tag ); break;
            case 2: out.writeShort( (Short)tag ); break;
            case 3: out.writeInt( (Integer)tag ); break;
            case 4: out.writeLong( (Long)tag ); break;
            case 5: out.writeFloat( (Float)tag ); break;
            case 6: out.writeDouble( (Double)tag ); break;

            case 7: writeByteArray(out, (byte[])tag); break;
            case 8: writeString(out, (String)tag); break;
            case 9: writeList(out, (List<Object>)tag); break;
            case 10: writeCompound(out, (Map<String, Object>)tag); break;
            case 11: writeIntArray(out, (int[]) tag); break;
            default: throw new IOException("Invalid NBT tag type (1-11): " + type);
        }
    }

    private static void writeByteArray(DataOutput out, byte[] array) throws IOException {
        out.writeInt(array.length);
        out.write(array);
    }

    private static void writeIntArray(DataOutput out, int[] array) throws IOException {
        out.writeInt(array.length);
        for (int i = 0; i < array.length; i++)
            out.writeInt(array[i]);
    }

    private static void writeString(DataOutput out, String str) throws IOException {
        out.writeUTF(str);
    }

    private static void writeList(DataOutput out, List<Object> list) throws IOException {
        byte type = list.isEmpty() ? 1 : whichType(list.get(0));
        out.writeByte(type);
        out.writeInt( list.size() );
        for (Object tag : list) {
            writeTag(out, type, tag);
        }
    }

    private static void writeCompound(DataOutput out, Map<String, Object> map) throws IOException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("$this.name")) {
                continue;   // skip internal name
            }
            byte type = whichType(entry.getValue());
            out.writeByte(type);
            writeString(out, entry.getKey());
            writeTag(out, type, entry.getValue());
        }
        out.writeByte(0);
    }
}
