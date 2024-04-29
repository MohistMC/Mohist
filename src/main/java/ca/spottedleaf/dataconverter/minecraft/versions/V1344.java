package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.MapType;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public final class V1344 {

    protected static final int VERSION = MCVersions.V1_12_2 + 1;

    private static final Int2ObjectOpenHashMap<String> BUTTON_ID_TO_NAME = new Int2ObjectOpenHashMap<>();
    static {
        BUTTON_ID_TO_NAME.put(0, "key.unknown");
        BUTTON_ID_TO_NAME.put(11, "key.0");
        BUTTON_ID_TO_NAME.put(2, "key.1");
        BUTTON_ID_TO_NAME.put(3, "key.2");
        BUTTON_ID_TO_NAME.put(4, "key.3");
        BUTTON_ID_TO_NAME.put(5, "key.4");
        BUTTON_ID_TO_NAME.put(6, "key.5");
        BUTTON_ID_TO_NAME.put(7, "key.6");
        BUTTON_ID_TO_NAME.put(8, "key.7");
        BUTTON_ID_TO_NAME.put(9, "key.8");
        BUTTON_ID_TO_NAME.put(10, "key.9");
        BUTTON_ID_TO_NAME.put(30, "key.a");
        BUTTON_ID_TO_NAME.put(40, "key.apostrophe");
        BUTTON_ID_TO_NAME.put(48, "key.b");
        BUTTON_ID_TO_NAME.put(43, "key.backslash");
        BUTTON_ID_TO_NAME.put(14, "key.backspace");
        BUTTON_ID_TO_NAME.put(46, "key.c");
        BUTTON_ID_TO_NAME.put(58, "key.caps.lock");
        BUTTON_ID_TO_NAME.put(51, "key.comma");
        BUTTON_ID_TO_NAME.put(32, "key.d");
        BUTTON_ID_TO_NAME.put(211, "key.delete");
        BUTTON_ID_TO_NAME.put(208, "key.down");
        BUTTON_ID_TO_NAME.put(18, "key.e");
        BUTTON_ID_TO_NAME.put(207, "key.end");
        BUTTON_ID_TO_NAME.put(28, "key.enter");
        BUTTON_ID_TO_NAME.put(13, "key.equal");
        BUTTON_ID_TO_NAME.put(1, "key.escape");
        BUTTON_ID_TO_NAME.put(33, "key.f");
        BUTTON_ID_TO_NAME.put(59, "key.f1");
        BUTTON_ID_TO_NAME.put(68, "key.f10");
        BUTTON_ID_TO_NAME.put(87, "key.f11");
        BUTTON_ID_TO_NAME.put(88, "key.f12");
        BUTTON_ID_TO_NAME.put(100, "key.f13");
        BUTTON_ID_TO_NAME.put(101, "key.f14");
        BUTTON_ID_TO_NAME.put(102, "key.f15");
        BUTTON_ID_TO_NAME.put(103, "key.f16");
        BUTTON_ID_TO_NAME.put(104, "key.f17");
        BUTTON_ID_TO_NAME.put(105, "key.f18");
        BUTTON_ID_TO_NAME.put(113, "key.f19");
        BUTTON_ID_TO_NAME.put(60, "key.f2");
        BUTTON_ID_TO_NAME.put(61, "key.f3");
        BUTTON_ID_TO_NAME.put(62, "key.f4");
        BUTTON_ID_TO_NAME.put(63, "key.f5");
        BUTTON_ID_TO_NAME.put(64, "key.f6");
        BUTTON_ID_TO_NAME.put(65, "key.f7");
        BUTTON_ID_TO_NAME.put(66, "key.f8");
        BUTTON_ID_TO_NAME.put(67, "key.f9");
        BUTTON_ID_TO_NAME.put(34, "key.g");
        BUTTON_ID_TO_NAME.put(41, "key.grave.accent");
        BUTTON_ID_TO_NAME.put(35, "key.h");
        BUTTON_ID_TO_NAME.put(199, "key.home");
        BUTTON_ID_TO_NAME.put(23, "key.i");
        BUTTON_ID_TO_NAME.put(210, "key.insert");
        BUTTON_ID_TO_NAME.put(36, "key.j");
        BUTTON_ID_TO_NAME.put(37, "key.k");
        BUTTON_ID_TO_NAME.put(82, "key.keypad.0");
        BUTTON_ID_TO_NAME.put(79, "key.keypad.1");
        BUTTON_ID_TO_NAME.put(80, "key.keypad.2");
        BUTTON_ID_TO_NAME.put(81, "key.keypad.3");
        BUTTON_ID_TO_NAME.put(75, "key.keypad.4");
        BUTTON_ID_TO_NAME.put(76, "key.keypad.5");
        BUTTON_ID_TO_NAME.put(77, "key.keypad.6");
        BUTTON_ID_TO_NAME.put(71, "key.keypad.7");
        BUTTON_ID_TO_NAME.put(72, "key.keypad.8");
        BUTTON_ID_TO_NAME.put(73, "key.keypad.9");
        BUTTON_ID_TO_NAME.put(78, "key.keypad.add");
        BUTTON_ID_TO_NAME.put(83, "key.keypad.decimal");
        BUTTON_ID_TO_NAME.put(181, "key.keypad.divide");
        BUTTON_ID_TO_NAME.put(156, "key.keypad.enter");
        BUTTON_ID_TO_NAME.put(141, "key.keypad.equal");
        BUTTON_ID_TO_NAME.put(55, "key.keypad.multiply");
        BUTTON_ID_TO_NAME.put(74, "key.keypad.subtract");
        BUTTON_ID_TO_NAME.put(38, "key.l");
        BUTTON_ID_TO_NAME.put(203, "key.left");
        BUTTON_ID_TO_NAME.put(56, "key.left.alt");
        BUTTON_ID_TO_NAME.put(26, "key.left.bracket");
        BUTTON_ID_TO_NAME.put(29, "key.left.control");
        BUTTON_ID_TO_NAME.put(42, "key.left.shift");
        BUTTON_ID_TO_NAME.put(219, "key.left.win");
        BUTTON_ID_TO_NAME.put(50, "key.m");
        BUTTON_ID_TO_NAME.put(12, "key.minus");
        BUTTON_ID_TO_NAME.put(49, "key.n");
        BUTTON_ID_TO_NAME.put(69, "key.num.lock");
        BUTTON_ID_TO_NAME.put(24, "key.o");
        BUTTON_ID_TO_NAME.put(25, "key.p");
        BUTTON_ID_TO_NAME.put(209, "key.page.down");
        BUTTON_ID_TO_NAME.put(201, "key.page.up");
        BUTTON_ID_TO_NAME.put(197, "key.pause");
        BUTTON_ID_TO_NAME.put(52, "key.period");
        BUTTON_ID_TO_NAME.put(183, "key.print.screen");
        BUTTON_ID_TO_NAME.put(16, "key.q");
        BUTTON_ID_TO_NAME.put(19, "key.r");
        BUTTON_ID_TO_NAME.put(205, "key.right");
        BUTTON_ID_TO_NAME.put(184, "key.right.alt");
        BUTTON_ID_TO_NAME.put(27, "key.right.bracket");
        BUTTON_ID_TO_NAME.put(157, "key.right.control");
        BUTTON_ID_TO_NAME.put(54, "key.right.shift");
        BUTTON_ID_TO_NAME.put(220, "key.right.win");
        BUTTON_ID_TO_NAME.put(31, "key.s");
        BUTTON_ID_TO_NAME.put(70, "key.scroll.lock");
        BUTTON_ID_TO_NAME.put(39, "key.semicolon");
        BUTTON_ID_TO_NAME.put(53, "key.slash");
        BUTTON_ID_TO_NAME.put(57, "key.space");
        BUTTON_ID_TO_NAME.put(20, "key.t");
        BUTTON_ID_TO_NAME.put(15, "key.tab");
        BUTTON_ID_TO_NAME.put(22, "key.u");
        BUTTON_ID_TO_NAME.put(200, "key.up");
        BUTTON_ID_TO_NAME.put(47, "key.v");
        BUTTON_ID_TO_NAME.put(17, "key.w");
        BUTTON_ID_TO_NAME.put(45, "key.x");
        BUTTON_ID_TO_NAME.put(21, "key.y");
        BUTTON_ID_TO_NAME.put(44, "key.z");
    }

    public static void register() {
        MCTypeRegistry.OPTIONS.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                for (final String key : data.keys()) {
                    if (!key.startsWith("key_")) {
                        continue;
                    }
                    final String value = data.getString(key);
                    final int code;
                    try {
                        code = Integer.parseInt(value);
                    } catch (final NumberFormatException ex) {
                        continue;
                    }

                    final String newEntry;

                    if (code < 0) {
                        final int mouseCode = code + 100;
                        switch (mouseCode) {
                            case 0:
                                newEntry = "key.mouse.left";
                                break;
                            case 1:
                                newEntry = "key.mouse.right";
                                break;
                            case 2:
                                newEntry = "key.mouse.middle";
                                break;
                            default:
                                newEntry = "key.mouse." + (mouseCode + 1);
                                break;
                        }
                    } else {
                        newEntry = BUTTON_ID_TO_NAME.getOrDefault(code, "key.unknown");
                    }

                    // No CMEs occur for existing entries in maps.
                    data.setString(key, newEntry);
                }
                return null;
            }
        });
    }

    private V1344() {}

}
