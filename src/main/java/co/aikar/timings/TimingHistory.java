/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import co.aikar.timings.TimingHistory.RegionData.RegionId;
import co.aikar.util.LoadingMap;
import co.aikar.util.MRUMapCache;
import com.google.common.base.Function;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.util.*;

import static co.aikar.timings.TimingsManager.FULL_SERVER_TICK;
import static co.aikar.timings.TimingsManager.MINUTE_REPORTS;
import static co.aikar.util.JSONUtil.*;

@SuppressWarnings({"deprecation", "SuppressionAnnotation", "Convert2Lambda", "Anonymous2MethodRef"})
public class TimingHistory {
    public static long lastMinuteTime;
    public static long timedTicks;
    public static long playerTicks;
    public static long entityTicks;
    public static long tileEntityTicks;
    public static long activatedEntityTicks;
    private static int worldIdPool = 1;
    static Map<String, Integer> worldMap = LoadingMap.newHashMap(new Function<String, Integer>() {
        @Override
        public Integer apply(String input) {
            return worldIdPool++;
        }
    });
    final Set<Material> tileEntityTypeSet = Sets.newHashSet();
    final Set<EntityType> entityTypeSet = Sets.newHashSet();
    private final long endTime;
    private final long startTime;
    private final long totalTicks;
    private final long totalTime; // Represents all time spent running the server this history
    private final MinuteReport[] minuteReports;
    private final TimingHistoryEntry[] entries;
    private final Map<Object, Object> worlds;

    TimingHistory() {
        this.endTime = System.currentTimeMillis() / 1000;
        this.startTime = TimingsManager.historyStart / 1000;
        if (timedTicks % 1200 != 0 || MINUTE_REPORTS.isEmpty()) {
            this.minuteReports = MINUTE_REPORTS.toArray(new MinuteReport[MINUTE_REPORTS.size() + 1]);
            this.minuteReports[this.minuteReports.length - 1] = new MinuteReport();
        } else {
            this.minuteReports = MINUTE_REPORTS.toArray(new MinuteReport[MINUTE_REPORTS.size()]);
        }
        long ticks = 0;
        for (MinuteReport mp : this.minuteReports) {
            ticks += mp.ticksRecord.timed;
        }
        this.totalTicks = ticks;
        this.totalTime = FULL_SERVER_TICK.record.getTotalTime();
        this.entries = new TimingHistoryEntry[TimingsManager.HANDLERS.size()];

        int i = 0;
        for (TimingHandler handler : TimingsManager.HANDLERS) {
            entries[i++] = new TimingHistoryEntry(handler);
        }

        // Information about all loaded chunks/entities
        //noinspection unchecked
        this.worlds = toObjectMapper(Bukkit.getWorlds(), new Function<World, JSONPair>() {
            @Override
            public JSONPair apply(World world) {
                Map<RegionId, RegionData> regions = LoadingMap.newHashMap(RegionData.LOADER);

                for (Chunk chunk : world.getLoadedChunks()) {
                    RegionData data = regions.get(new RegionId(chunk.getX(), chunk.getZ()));

                    for (Entity entity : chunk.getEntities()) {
                        if (entity == null) {
                            Bukkit.getLogger().warning("Null entity detected in chunk at position x: " + chunk.getX() + ", z: " + chunk.getZ());
                            continue;
                        }

                        data.entityCounts.get(entity.getType()).increment();
                    }

                    for (BlockState tileEntity : chunk.getTileEntities()) {
                        if (tileEntity == null) {
                            Bukkit.getLogger().warning("Null tileentity detected in chunk at position x: " + chunk.getX() + ", z: " + chunk.getZ());
                            continue;
                        }

                        data.tileEntityCounts.get(tileEntity.getBlock().getType()).increment();
                    }
                }
                return pair(
                        worldMap.get(world.getName()),
                        toArrayMapper(regions.values(), new Function<RegionData, Object>() {
                            @Override
                            public Object apply(RegionData input) {
                                return toArray(
                                        input.regionId.x,
                                        input.regionId.z,
                                        toObjectMapper(input.entityCounts.entrySet(),
                                                new Function<Map.Entry<EntityType, Counter>, JSONPair>() {
                                                    @Override
                                                    public JSONPair apply(Map.Entry<EntityType, Counter> entry) {
                                                        entityTypeSet.add(entry.getKey());
                                                        return pair(
                                                                String.valueOf(entry.getKey().getTypeId()),
                                                                entry.getValue().count()
                                                        );
                                                    }
                                                }
                                        ),
                                        toObjectMapper(input.tileEntityCounts.entrySet(),
                                                new Function<Map.Entry<Material, Counter>, JSONPair>() {
                                                    @Override
                                                    public JSONPair apply(Map.Entry<Material, Counter> entry) {
                                                        tileEntityTypeSet.add(entry.getKey());
                                                        return pair(
                                                                String.valueOf(entry.getKey().getId()),
                                                                entry.getValue().count()
                                                        );
                                                    }
                                                }
                                        )
                                );
                            }
                        })
                );
            }
        });
    }

    static void resetTicks(boolean fullReset) {
        if (fullReset) {
            // Non full is simply for 1 minute reports
            timedTicks = 0;
        }
        lastMinuteTime = System.nanoTime();
        playerTicks = 0;
        tileEntityTicks = 0;
        entityTicks = 0;
        activatedEntityTicks = 0;
    }

    Object export() {
        return createObject(
                pair("s", startTime),
                pair("e", endTime),
                pair("tk", totalTicks),
                pair("tm", totalTime),
                pair("w", worlds),
                pair("h", toArrayMapper(entries, new Function<TimingHistoryEntry, Object>() {
                    @Override
                    public Object apply(TimingHistoryEntry entry) {
                        TimingData record = entry.data;
                        if (!record.hasData()) {
                            return null;
                        }
                        return entry.export();
                    }
                })),
                pair("mp", toArrayMapper(minuteReports, new Function<MinuteReport, Object>() {
                    @Override
                    public Object apply(MinuteReport input) {
                        return input.export();
                    }
                }))
        );
    }

    static class RegionData {
        @SuppressWarnings("Guava")
        static Function<RegionId, RegionData> LOADER = new Function<RegionId, RegionData>() {
            @Override
            public RegionData apply(RegionId id) {
                return new RegionData(id);
            }
        };
        final RegionId regionId;
        @SuppressWarnings("unchecked")
        final Map<EntityType, Counter> entityCounts = MRUMapCache.of(LoadingMap.of(
                new EnumMap<EntityType, Counter>(EntityType.class), k -> new Counter()
        ));
        @SuppressWarnings("unchecked")
        final Map<Material, Counter> tileEntityCounts = MRUMapCache.of(LoadingMap.of(
                new EnumMap<Material, Counter>(Material.class), k -> new Counter()
        ));

        RegionData(RegionId id) {
            this.regionId = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RegionData that = (RegionData) o;

            return regionId.equals(that.regionId);

        }

        @Override
        public int hashCode() {
            return regionId.hashCode();
        }

        static class RegionId {
            final int x, z;
            final long regionId;

            RegionId(int x, int z) {
                this.x = x >> 5 << 5;
                this.z = z >> 5 << 5;
                this.regionId = ((long) (this.x) << 32) + (this.z >> 5 << 5) - Integer.MIN_VALUE;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                RegionId regionId1 = (RegionId) o;

                return regionId == regionId1.regionId;

            }

            @Override
            public int hashCode() {
                return (int) (regionId ^ (regionId >>> 32));
            }
        }
    }

    static class MinuteReport {
        final long time = System.currentTimeMillis() / 1000;

        final TicksRecord ticksRecord = new TicksRecord();
        final PingRecord pingRecord = new PingRecord();
        final TimingData fst = TimingsManager.FULL_SERVER_TICK.minuteData.clone();
        final double tps = 1E9 / (System.nanoTime() - lastMinuteTime) * ticksRecord.timed;
        final double usedMemory = TimingsManager.FULL_SERVER_TICK.avgUsedMemory;
        final double freeMemory = TimingsManager.FULL_SERVER_TICK.avgFreeMemory;
        final double loadAvg = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();

        List<Object> export() {
            return toArray(
                    time,
                    Math.round(tps * 100D) / 100D,
                    Math.round(pingRecord.avg * 100D) / 100D,
                    fst.export(),
                    toArray(ticksRecord.timed,
                            ticksRecord.player,
                            ticksRecord.entity,
                            ticksRecord.activatedEntity,
                            ticksRecord.tileEntity
                    ),
                    usedMemory,
                    freeMemory,
                    loadAvg
            );
        }
    }

    private static class TicksRecord {
        final long timed;
        final long player;
        final long entity;
        final long tileEntity;
        final long activatedEntity;

        TicksRecord() {
            timed = timedTicks - (TimingsManager.MINUTE_REPORTS.size() * 1200);
            player = playerTicks;
            entity = entityTicks;
            tileEntity = tileEntityTicks;
            activatedEntity = activatedEntityTicks;
        }

    }

    private static class PingRecord {
        final double avg;

        PingRecord() {
            final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            int totalPing = 0;
            for (Player player : onlinePlayers) {
                totalPing += player.spigot().getPing();
            }
            avg = onlinePlayers.isEmpty() ? 0 : totalPing / onlinePlayers.size();
        }
    }


    private static class Counter {
        private int count = 0;

        public int increment() {
            return ++count;
        }

        public int count() {
            return count;
        }
    }
}
