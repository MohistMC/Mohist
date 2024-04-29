package ca.spottedleaf.dataconverter.minecraft.versions;

import ca.spottedleaf.dataconverter.converters.DataConverter;
import ca.spottedleaf.dataconverter.minecraft.MCVersions;
import ca.spottedleaf.dataconverter.minecraft.datatypes.MCTypeRegistry;
import ca.spottedleaf.dataconverter.types.ListType;
import ca.spottedleaf.dataconverter.types.MapType;
import ca.spottedleaf.dataconverter.types.ObjectType;
import ca.spottedleaf.dataconverter.types.Types;
import com.mojang.datafixers.DataFixUtils;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.datafix.PackedBitStorage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class V1496 {

    private V1496() {}

    protected static final int VERSION = MCVersions.V18W21B;

    private static final int[][] DIRECTIONS = new int[][] {
            new int[] {-1, 0, 0},
            new int[] {1, 0, 0},
            new int[] {0, -1, 0},
            new int[] {0, 1, 0},
            new int[] {0, 0, -1},
            new int[] {0, 0, 1}
    };

    private static final Object2IntOpenHashMap<String> LEAVES_TO_ID = new Object2IntOpenHashMap<>();
    static {
        LEAVES_TO_ID.put("minecraft:acacia_leaves", 0);
        LEAVES_TO_ID.put("minecraft:birch_leaves", 1);
        LEAVES_TO_ID.put("minecraft:dark_oak_leaves", 2);
        LEAVES_TO_ID.put("minecraft:jungle_leaves", 3);
        LEAVES_TO_ID.put("minecraft:oak_leaves", 4);
        LEAVES_TO_ID.put("minecraft:spruce_leaves", 5);
    }

    private static final Set<String> LOGS = new HashSet<>(
            Arrays.asList(
                    "minecraft:acacia_bark",
                    "minecraft:birch_bark",
                    "minecraft:dark_oak_bark",
                    "minecraft:jungle_bark",
                    "minecraft:oak_bark",
                    "minecraft:spruce_bark",
                    "minecraft:acacia_log",
                    "minecraft:birch_log",
                    "minecraft:dark_oak_log",
                    "minecraft:jungle_log",
                    "minecraft:oak_log",
                    "minecraft:spruce_log",
                    "minecraft:stripped_acacia_log",
                    "minecraft:stripped_birch_log",
                    "minecraft:stripped_dark_oak_log",
                    "minecraft:stripped_jungle_log",
                    "minecraft:stripped_oak_log",
                    "minecraft:stripped_spruce_log"
            )
    );

    public static void register() {
        MCTypeRegistry.CHUNK.addStructureConverter(new DataConverter<>(VERSION) {
            @Override
            public MapType<String> convert(final MapType<String> data, final long sourceVersion, final long toVersion) {
                final MapType<String> level = data.getMap("Level");
                if (level == null) {
                    return null;
                }

                final ListType sectionsNBT = level.getList("Sections", ObjectType.MAP);
                if (sectionsNBT == null) {
                    return null;
                }

                int newSides = 0;

                final LeavesSection[] sections = new LeavesSection[16];
                boolean skippable = true;
                for (int i = 0, len = sectionsNBT.size(); i < len; ++i) {
                    final LeavesSection section = new LeavesSection(sectionsNBT.getMap(i));
                    sections[section.sectionY] = section;

                    skippable &= section.isSkippable();
                }

                if (skippable) {
                    return null;
                }

                final IntOpenHashSet[] positionsByDistance = new IntOpenHashSet[7];
                for (int i = 0; i < positionsByDistance.length; ++i) {
                    positionsByDistance[i] = new IntOpenHashSet();
                }

                for (final LeavesSection section : sections) {
                    if (section == null || section.isSkippable()) {
                        continue;
                    }

                    for (int index = 0; index < 4096; ++index) {
                        final int block = section.getBlock(index);
                        if (section.isLog(block)) {
                            positionsByDistance[0].add(section.getSectionY() << 12 | index);
                        } else if (section.isLeaf(block)) {
                            int x = getX(index);
                            int z = getZ(index);
                            newSides |= getSideMask(x == 0, x == 15, z == 0, z == 15);
                        }
                    }
                }

                // this is basically supposed to recalculate the distances, because a higher cap was added
                for (int distance = 1; distance < 7; ++distance) {
                    final IntOpenHashSet positionsLess = positionsByDistance[distance - 1];
                    final IntOpenHashSet positionsEqual = positionsByDistance[distance];

                    for (final IntIterator iterator = positionsLess.iterator(); iterator.hasNext();) {
                        final int position = iterator.nextInt();
                        final int fromX = getX(position);
                        final int fromY = getY(position);
                        final int fromZ = getZ(position);

                        for (final int[] direction : DIRECTIONS) {
                            final int toX = fromX + direction[0];
                            final int toY = fromY + direction[1];
                            final int toZ = fromZ + direction[2];

                            if (!(toX >= 0 && toX <= 15 && toZ >= 0 && toZ <= 15 && toY >= 0 && toY <= 255)) {
                                continue;
                            }

                            final LeavesSection toSection = sections[toY >> 4];
                            if (toSection == null || toSection.isSkippable()) {
                                continue;
                            }

                            final int sectionLocalIndex = getIndex(toX, toY & 15, toZ);
                            final int toBlock = toSection.getBlock(sectionLocalIndex);

                            if (toSection.isLeaf(toBlock)) {
                                final int newDistance = toSection.getDistance(toBlock);
                                if (newDistance > distance) {
                                    toSection.setDistance(sectionLocalIndex, toBlock, distance);
                                    positionsEqual.add(getIndex(toX, toY, toZ));
                                }
                            }
                        }
                    }
                }

                // done updating blocks, now just update the blockstates and palette
                for (int i = 0, len = sectionsNBT.size(); i < len; ++i) {
                    final MapType<String> sectionNBT = sectionsNBT.getMap(i);
                    final int y = sectionNBT.getInt("Y");
                    final LeavesSection section = sections[y];

                    section.writeInto(sectionNBT);
                }

                // if sides changed during process, update it now
                if (newSides != 0) {
                    MapType<String> upgradeData = level.getMap("UpgradeData");
                    if (upgradeData == null) {
                        level.setMap("UpgradeData", upgradeData = Types.NBT.createEmptyMap());
                    }

                    upgradeData.setByte("Sides", (byte)(upgradeData.getByte("Sides") | newSides));
                }

                return null;
            }
        });
    }

    public static int getIndex(final int x, final int y, final int z) {
        return y << 8 | z << 4 | x;
    }

    public static int getX(final int index) {
        return index & 15;
    }

    public static int getY(final int index) {
        return index >> 8 & 255;
    }

    public static int getZ(final int index) {
        return index >> 4 & 15;
    }

    public static int getSideMask(final boolean noLeft, final boolean noRight, final boolean noBack, final boolean noForward) {
        final int ret;

        if (noBack) {
            if (noRight) {
                ret = 2;
            } else if (noLeft) {
                ret = 128;
            } else {
                ret = 1;
            }
        } else if (noForward) {
            if (noLeft) {
                ret = 32;
            } else if (noRight) {
                ret = 8;
            } else {
                ret = 16;
            }
        } else if (noRight) {
            ret = 4;
        } else if (noLeft) {
            ret = 64;
        } else {
            ret = 0;
        }

        return ret;
    }

    public abstract static class Section {
        protected final ListType palette;
        protected final int sectionY;
        protected PackedBitStorage storage;

        public Section(final MapType<String> section) {
            this.palette = section.getList("Palette", ObjectType.MAP);
            this.sectionY = section.getInt("Y");
            this.readStorage(section);
        }

        protected void readStorage(final MapType<String> section) {
            if (this.initSkippable()) {
                this.storage = null;
            } else {
                final long[] states = section.getLongs("BlockStates");
                final int bits = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
                this.storage = new PackedBitStorage(bits, 4096, states);
            }
        }

        public void writeInto(final MapType<String> section) {
            if (this.isSkippable()) {
                return;
            }

            section.setList("Palette", this.palette);
            section.setLongs("BlockStates", this.storage.getRaw());
        }

        public boolean isSkippable() {
            return this.storage == null;
        }

        public int getBlock(final int index) {
            return this.storage.get(index);
        }

        protected int getStateId(final String name, final boolean persistent, final int distance) {
            return LEAVES_TO_ID.getInt(name) << 5 | (persistent ? 16 : 0) | distance;
        }

        protected int getSectionY() {
            return this.sectionY;
        }

        protected abstract boolean initSkippable();
    }

    public static final class LeavesSection extends Section {
        private IntOpenHashSet leaveIds;
        private IntOpenHashSet logIds;
        private Int2IntOpenHashMap stateToIdMap;

        public LeavesSection(final MapType<String> section) {
            super(section);
        }

        @Override
        protected boolean initSkippable() {
            this.leaveIds = new IntOpenHashSet();
            this.logIds = new IntOpenHashSet();
            this.stateToIdMap = new Int2IntOpenHashMap();
            this.stateToIdMap.defaultReturnValue(-1);

            for(int i = 0; i < this.palette.size(); ++i) {
                final MapType<String> blockState = this.palette.getMap(i);
                final String name = blockState.getString("Name", "");
                if (LEAVES_TO_ID.containsKey(name)) {
                    final MapType<String> properties = blockState.getMap("Properties");
                    final boolean notDecayable = properties != null && "false".equals(properties.getString("decayable"));

                    this.leaveIds.add(i);
                    this.stateToIdMap.put(this.getStateId(name, notDecayable, 7), i);
                    this.palette.setMap(i, this.makeNewLeafTag(name, notDecayable, 7));
                }

                if (LOGS.contains(name)) {
                    this.logIds.add(i);
                }
            }

            return this.leaveIds.isEmpty() && this.logIds.isEmpty();
        }

        private MapType<String> makeNewLeafTag(final String name, final boolean notDecayable, final int distance) {
            final MapType<String> properties = Types.NBT.createEmptyMap();
            final MapType<String> ret = Types.NBT.createEmptyMap();

            ret.setString("Name", name);
            ret.setMap("Properties", properties);

            properties.setString("persistent", Boolean.toString(notDecayable));
            properties.setString("distance", Integer.toString(distance));

            return ret;
        }

        public boolean isLog(final int id) {
            return this.logIds.contains(id);
        }

        public boolean isLeaf(final int id) {
            return this.leaveIds.contains(id);
        }

        // only call for logs or leaves, will throw otherwise!
        private int getDistance(final int id) {
            if (this.isLog(id)) {
                return 0;
            }

            return Integer.parseInt(this.palette.getMap(id).getMap("Properties").getString("distance"));
        }

        private void setDistance(final int index, final int id, final int distance) {
            final MapType<String> state = this.palette.getMap(id);
            final String name = state.getString("Name");
            final boolean persistent = "true".equals(state.getMap("Properties").getString("persistent"));
            final int newState = this.getStateId(name, persistent, distance);
            int newStateId;
            if ((newStateId = this.stateToIdMap.get(newState)) == -1) {
                newStateId = this.palette.size();
                this.leaveIds.add(newStateId);
                this.stateToIdMap.put(newState, newStateId);
                this.palette.addMap(this.makeNewLeafTag(name, persistent, distance));
            }

            if (1 << this.storage.getBits() <= newStateId) {
                // need to widen storage
                final PackedBitStorage newStorage = new PackedBitStorage(this.storage.getBits() + 1, 4096);

                for(int i = 0; i < 4096; ++i) {
                    newStorage.set(i, this.storage.get(i));
                }

                this.storage = newStorage;
            }

            this.storage.set(index, newStateId);
        }
    }
}
