package ca.spottedleaf.starlight.common.util;

import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;
import ca.spottedleaf.starlight.common.light.StarLightEngine;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.slf4j.Logger;

public final class SaveUtil {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int STARLIGHT_LIGHT_VERSION = 9;

    public static int getLightVersion() {
        return STARLIGHT_LIGHT_VERSION;
    }

    private static final String BLOCKLIGHT_STATE_TAG = "starlight.blocklight_state";
    private static final String SKYLIGHT_STATE_TAG = "starlight.skylight_state";
    private static final String STARLIGHT_VERSION_TAG = "starlight.light_version";

    public static void saveLightHook(final Level world, final ChunkAccess chunk, final CompoundTag nbt) {
        try {
            saveLightHookReal(world, chunk, nbt);
        } catch (final Throwable ex) {
            // failing to inject is not fatal so we catch anything here. if it fails, it will have correctly set lit to false
            // for Vanilla to relight on load and it will not set our lit tag so we will relight on load
            if (ex instanceof ThreadDeath) {
                throw (ThreadDeath)ex;
            }
            LOGGER.warn("Failed to inject light data into save data for chunk " + chunk.getPos() + ", chunk light will be recalculated on its next load", ex);
        }
    }

    private static void saveLightHookReal(final Level world, final ChunkAccess chunk, final CompoundTag tag) {
        if (tag == null) {
            return;
        }

        final int minSection = WorldUtil.getMinLightSection(world);
        final int maxSection = WorldUtil.getMaxLightSection(world);

        SWMRNibbleArray[] blockNibbles = chunk.getBlockNibbles();
        SWMRNibbleArray[] skyNibbles = chunk.getSkyNibbles();

        boolean lit = chunk.isLightCorrect() || !(world instanceof ServerLevel);
        // diff start - store our tag for whether light data is init'd
        if (lit) {
            tag.putBoolean("isLightOn", false);
        }
        // diff end - store our tag for whether light data is init'd
        ChunkStatus status = ChunkStatus.byName(tag.getString("Status"));

        CompoundTag[] sections = new CompoundTag[maxSection - minSection + 1];

        ListTag sectionsStored = tag.getList("sections", 10);

        for (int i = 0; i < sectionsStored.size(); ++i) {
            CompoundTag sectionStored = sectionsStored.getCompound(i);
            int k = sectionStored.getByte("Y");

            // strip light data
            sectionStored.remove("BlockLight");
            sectionStored.remove("SkyLight");

            if (!sectionStored.isEmpty()) {
                sections[k - minSection] = sectionStored;
            }
        }

        if (lit && status.isOrAfter(ChunkStatus.LIGHT)) {
            for (int i = minSection; i <= maxSection; ++i) {
                SWMRNibbleArray.SaveState blockNibble = blockNibbles[i - minSection].getSaveState();
                SWMRNibbleArray.SaveState skyNibble = skyNibbles[i - minSection].getSaveState();
                if (blockNibble != null || skyNibble != null) {
                    CompoundTag section = sections[i - minSection];
                    if (section == null) {
                        section = new CompoundTag();
                        section.putByte("Y", (byte)i);
                        sections[i - minSection] = section;
                    }

                    // we store under the same key so mod programs editing nbt
                    // can still read the data, hopefully.
                    // however, for compatibility we store chunks as unlit so vanilla
                    // is forced to re-light them if it encounters our data. It's too much of a burden
                    // to try and maintain compatibility with a broken and inferior skylight management system.

                    if (blockNibble != null) {
                        if (blockNibble.data() != null) {
                            section.putByteArray("BlockLight", blockNibble.data());
                        }
                        section.putInt(BLOCKLIGHT_STATE_TAG, blockNibble.state());
                    }

                    if (skyNibble != null) {
                        if (skyNibble.data() != null) {
                            section.putByteArray("SkyLight", skyNibble.data());
                        }
                        section.putInt(SKYLIGHT_STATE_TAG, skyNibble.state());
                    }
                }
            }
        }

        // rewrite section list
        sectionsStored.clear();
        for (CompoundTag section : sections) {
            if (section != null) {
                sectionsStored.add(section);
            }
        }
        tag.put("sections", sectionsStored);
        if (lit) {
            tag.putInt(STARLIGHT_VERSION_TAG, STARLIGHT_LIGHT_VERSION); // only mark as fully lit after we have successfully injected our data
        }
    }

    public static void loadLightHook(final Level world, final ChunkPos pos, final CompoundTag tag, final ChunkAccess into) {
        try {
            loadLightHookReal(world, pos, tag, into);
        } catch (final Throwable ex) {
            // failing to inject is not fatal so we catch anything here. if it fails, then we simply relight. Not a problem, we get correct
            // lighting in both cases.
            if (ex instanceof ThreadDeath) {
                throw (ThreadDeath)ex;
            }
            LOGGER.warn("Failed to load light for chunk " + pos + ", light will be recalculated", ex);
        }
    }

    private static void loadLightHookReal(final Level world, final ChunkPos pos, final CompoundTag tag, final ChunkAccess into) {
        if (into == null) {
            return;
        }
        final int minSection = WorldUtil.getMinLightSection(world);
        final int maxSection = WorldUtil.getMaxLightSection(world);

        into.setLightCorrect(false); // mark as unlit in case we fail parsing

        SWMRNibbleArray[] blockNibbles = StarLightEngine.getFilledEmptyLight(world);
        SWMRNibbleArray[] skyNibbles = StarLightEngine.getFilledEmptyLight(world);


        // start copy from the original method
        boolean lit = tag.get("isLightOn") != null && tag.getInt(STARLIGHT_VERSION_TAG) == STARLIGHT_LIGHT_VERSION;
        boolean canReadSky = world.dimensionType().hasSkyLight();
        ChunkStatus status = ChunkStatus.byName(tag.getString("Status"));
        if (lit && status.isOrAfter(ChunkStatus.LIGHT)) { // diff - we add the status check here
            ListTag sections = tag.getList("sections", 10);

            for (int i = 0; i < sections.size(); ++i) {
                CompoundTag sectionData = sections.getCompound(i);
                int y = sectionData.getByte("Y");

                if (sectionData.contains("BlockLight", 7)) {
                    // this is where our diff is
                    blockNibbles[y - minSection] = new SWMRNibbleArray(sectionData.getByteArray("BlockLight").clone(), sectionData.getInt(BLOCKLIGHT_STATE_TAG)); // clone for data safety
                } else {
                    blockNibbles[y - minSection] = new SWMRNibbleArray(null, sectionData.getInt(BLOCKLIGHT_STATE_TAG));
                }

                if (canReadSky) {
                    if (sectionData.contains("SkyLight", 7)) {
                        // we store under the same key so mod programs editing nbt
                        // can still read the data, hopefully.
                        // however, for compatibility we store chunks as unlit so vanilla
                        // is forced to re-light them if it encounters our data. It's too much of a burden
                        // to try and maintain compatibility with a broken and inferior skylight management system.
                        skyNibbles[y - minSection] = new SWMRNibbleArray(sectionData.getByteArray("SkyLight").clone(), sectionData.getInt(SKYLIGHT_STATE_TAG)); // clone for data safety
                    } else {
                        skyNibbles[y - minSection] = new SWMRNibbleArray(null, sectionData.getInt(SKYLIGHT_STATE_TAG));
                    }
                }
            }
        }
        // end copy from vanilla

        into.setBlockNibbles(blockNibbles);
        into.setSkyNibbles(skyNibbles);
        into.setLightCorrect(lit); // now we set lit here, only after we've correctly parsed data
    }

    private SaveUtil() {}
}
