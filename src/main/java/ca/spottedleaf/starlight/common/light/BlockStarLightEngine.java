package ca.spottedleaf.starlight.common.light;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class BlockStarLightEngine extends StarLightEngine {

    public BlockStarLightEngine(final Level world) {
        super(false, world);
    }

    @Override
    protected boolean[] getEmptinessMap(final ChunkAccess chunk) {
        return chunk.getBlockEmptinessMap();
    }

    @Override
    protected void setEmptinessMap(final ChunkAccess chunk, final boolean[] to) {
        chunk.setBlockEmptinessMap(to);
    }

    @Override
    protected SWMRNibbleArray[] getNibblesOnChunk(final ChunkAccess chunk) {
        return chunk.getBlockNibbles();
    }

    @Override
    protected void setNibbles(final ChunkAccess chunk, final SWMRNibbleArray[] to) {
        chunk.setBlockNibbles(to);
    }

    @Override
    protected boolean canUseChunk(final ChunkAccess chunk) {
        return chunk.getStatus().isOrAfter(ChunkStatus.LIGHT) && (this.isClientSide || chunk.isLightCorrect());
    }

    @Override
    protected void setNibbleNull(final int chunkX, final int chunkY, final int chunkZ) {
        final SWMRNibbleArray nibble = this.getNibbleFromCache(chunkX, chunkY, chunkZ);
        if (nibble != null) {
            // de-initialisation is not as straightforward as with sky data, since deinit of block light is typically
            // because a block was removed - which can decrease light. with sky data, block breaking can only result
            // in increases, and thus the existing sky block check will actually correctly propagate light through
            // a null section. so in order to propagate decreases correctly, we can do a couple of things: not remove
            // the data section, or do edge checks on ALL axis (x, y, z). however I do not want edge checks running
            // for clients at all, as they are expensive. so we don't remove the section, but to maintain the appearence
            // of vanilla data management we "hide" them.
            nibble.setHidden();
        }
    }

    @Override
    protected void initNibble(final int chunkX, final int chunkY, final int chunkZ, final boolean extrude, final boolean initRemovedNibbles) {
        if (chunkY < this.minLightSection || chunkY > this.maxLightSection || this.getChunkInCache(chunkX, chunkZ) == null) {
            return;
        }

        final SWMRNibbleArray nibble = this.getNibbleFromCache(chunkX, chunkY, chunkZ);
        if (nibble == null) {
            if (!initRemovedNibbles) {
                throw new IllegalStateException();
            } else {
                this.setNibbleInCache(chunkX, chunkY, chunkZ, new SWMRNibbleArray());
            }
        } else {
            nibble.setNonNull();
        }
    }

    @Override
    protected final void checkBlock(final LightChunkGetter lightAccess, final int worldX, final int worldY, final int worldZ) {
        // blocks can change opacity
        // blocks can change emitted light
        // blocks can change direction of propagation

        final int encodeOffset = this.coordinateOffset;
        final int emittedMask = this.emittedLightMask;

        final int currentLevel = this.getLightLevel(worldX, worldY, worldZ);
        final BlockState blockState = this.getBlockState(worldX, worldY, worldZ);
        this.checkBlockPos.set(worldX, worldY, worldZ);
        final int emittedLevel = blockState.getLightEmission(lightAccess.getLevel(), this.checkBlockPos) & emittedMask; // Forge

        this.setLightLevel(worldX, worldY, worldZ, emittedLevel);
        // this accounts for change in emitted light that would cause an increase
        if (emittedLevel != 0) {
            this.appendToIncreaseQueue(
                    ((worldX + (worldZ << 6) + (worldY << (6 + 6)) + encodeOffset) & ((1L << (6 + 6 + 16)) - 1))
                            | (emittedLevel & 0xFL) << (6 + 6 + 16)
                            | (((long)ALL_DIRECTIONS_BITSET) << (6 + 6 + 16 + 4))
                            | (blockState.isConditionallyFullOpaque() ? FLAG_HAS_SIDED_TRANSPARENT_BLOCKS : 0)
            );
        }
        // this also accounts for a change in emitted light that would cause a decrease
        // this also accounts for the change of direction of propagation (i.e old block was full transparent, new block is full opaque or vice versa)
        // as it checks all neighbours (even if current level is 0)
        this.appendToDecreaseQueue(
                ((worldX + (worldZ << 6) + (worldY << (6 + 6)) + encodeOffset) & ((1L << (6 + 6 + 16)) - 1))
                        | (currentLevel & 0xFL) << (6 + 6 + 16)
                        | (((long)ALL_DIRECTIONS_BITSET) << (6 + 6 + 16 + 4))
                        // always keep sided transparent false here, new block might be conditionally transparent which would
                        // prevent us from decreasing sources in the directions where the new block is opaque
                        // if it turns out we were wrong to de-propagate the source, the re-propagate logic WILL always
                        // catch that and fix it.
        );
        // re-propagating neighbours (done by the decrease queue) will also account for opacity changes in this block
    }

    protected final BlockPos.MutableBlockPos recalcCenterPos = new BlockPos.MutableBlockPos();
    protected final BlockPos.MutableBlockPos recalcNeighbourPos = new BlockPos.MutableBlockPos();

    @Override
    protected int calculateLightValue(final LightChunkGetter lightAccess, final int worldX, final int worldY, final int worldZ,
                                      final int expect) {
        final BlockState centerState = this.getBlockState(worldX, worldY, worldZ);
        this.recalcCenterPos.set(worldX, worldY, worldZ); // Forge
        int level = centerState.getLightEmission(lightAccess.getLevel(), this.recalcCenterPos) & 0xF;

        if (level >= (15 - 1) || level > expect) {
            return level;
        }

        final int sectionOffset = this.chunkSectionIndexOffset;
        final BlockState conditionallyOpaqueState;
        int opacity = centerState.getOpacityIfCached();

        if (opacity == -1) {
            // Forge
            opacity = centerState.getLightBlock(lightAccess.getLevel(), this.recalcCenterPos);
            if (centerState.isConditionallyFullOpaque()) {
                conditionallyOpaqueState = centerState;
            } else {
                conditionallyOpaqueState = null;
            }
        } else if (opacity >= 15) {
            return level;
        } else {
            conditionallyOpaqueState = null;
        }
        opacity = Math.max(1, opacity);

        for (final AxisDirection direction : AXIS_DIRECTIONS) {
            final int offX = worldX + direction.x;
            final int offY = worldY + direction.y;
            final int offZ = worldZ + direction.z;

            final int sectionIndex = (offX >> 4) + 5 * (offZ >> 4) + (5 * 5) * (offY >> 4) + sectionOffset;

            final int neighbourLevel = this.getLightLevel(sectionIndex, (offX & 15) | ((offZ & 15) << 4) | ((offY & 15) << 8));

            if ((neighbourLevel - 1) <= level) {
                // don't need to test transparency, we know it wont affect the result.
                continue;
            }

            final BlockState neighbourState = this.getBlockState(offX, offY, offZ);
            if (neighbourState.isConditionallyFullOpaque()) {
                // here the block can be conditionally opaque (i.e light cannot propagate from it), so we need to test that
                // we don't read the blockstate because most of the time this is false, so using the faster
                // known transparency lookup results in a net win
                this.recalcNeighbourPos.set(offX, offY, offZ);
                final VoxelShape neighbourFace = neighbourState.getFaceOcclusionShape(lightAccess.getLevel(), this.recalcNeighbourPos, direction.opposite.nms);
                final VoxelShape thisFace = conditionallyOpaqueState == null ? Shapes.empty() : conditionallyOpaqueState.getFaceOcclusionShape(lightAccess.getLevel(), this.recalcCenterPos, direction.nms);
                if (Shapes.faceShapeOccludes(thisFace, neighbourFace)) {
                    // not allowed to propagate
                    continue;
                }
            }

            // passed transparency,

            final int calculated = neighbourLevel - opacity;
            level = Math.max(calculated, level);
            if (level > expect) {
                return level;
            }
        }

        return level;
    }

    @Override
    protected void propagateBlockChanges(final LightChunkGetter lightAccess, final ChunkAccess atChunk, final Set<BlockPos> positions) {
        for (final BlockPos pos : positions) {
            this.checkBlock(lightAccess, pos.getX(), pos.getY(), pos.getZ());
        }

        this.performLightDecrease(lightAccess);
    }

    protected List<BlockPos> getSources(final LightChunkGetter lightAccess, final ChunkAccess chunk) {
        final List<BlockPos> sources = new ArrayList<>();

        final int offX = chunk.getPos().x << 4;
        final int offZ = chunk.getPos().z << 4;

        final LevelChunkSection[] sections = chunk.getSections();
        for (int sectionY = this.minSection; sectionY <= this.maxSection; ++sectionY) {
            final LevelChunkSection section = sections[sectionY - this.minSection];
            if (section == null || section.hasOnlyAir()) {
                // no sources in empty sections
                continue;
            }
            // Forge - need to check each state getLightEmission
            final PalettedContainer<BlockState> states = section.getStates();
            final int offY = sectionY << 4;

            for (int index = 0; index < (16 * 16 * 16); ++index) {
                final BlockState state = states.get(index);
                // Forge start - use correct getLightEmission
                this.mutablePos1.set(offX | (index & 15), offY | (index >>> 8), offZ | ((index >>> 4) & 15));
                if (state.getLightEmission(lightAccess.getLevel(), this.mutablePos1) <= 0) {
                    continue;
                }
                // Forge end - use correct getLightEmission

                // index = x | (z << 4) | (y << 8)
                sources.add(new BlockPos(offX | (index & 15), offY | (index >>> 8), offZ | ((index >>> 4) & 15)));
            }
        }

        return sources;
    }

    @Override
    public void lightChunk(final LightChunkGetter lightAccess, final ChunkAccess chunk, final boolean needsEdgeChecks) {
        // setup sources
        final int emittedMask = this.emittedLightMask;
        final List<BlockPos> positions = this.getSources(lightAccess, chunk);
        for (final BlockPos pos : positions) {
            final BlockState blockState = this.getBlockState(pos.getX(), pos.getY(), pos.getZ());
            final int emittedLight = blockState.getLightEmission(lightAccess.getLevel(), pos) & emittedMask; // Forge

            if (emittedLight <= this.getLightLevel(pos.getX(), pos.getY(), pos.getZ())) {
                // some other source is brighter
                continue;
            }

            this.appendToIncreaseQueue(
                    ((pos.getX() + (pos.getZ() << 6) + (pos.getY() << (6 + 6)) + this.coordinateOffset) & ((1L << (6 + 6 + 16)) - 1))
                            | (emittedLight & 0xFL) << (6 + 6 + 16)
                            | (((long) ALL_DIRECTIONS_BITSET) << (6 + 6 + 16 + 4))
                            | (blockState.isConditionallyFullOpaque() ? FLAG_HAS_SIDED_TRANSPARENT_BLOCKS : 0)
            );


            // propagation wont set this for us
            this.setLightLevel(pos.getX(), pos.getY(), pos.getZ(), emittedLight);
        }

        if (needsEdgeChecks) {
            // not required to propagate here, but this will reduce the hit of the edge checks
            this.performLightIncrease(lightAccess);

            // verify neighbour edges
            this.checkChunkEdges(lightAccess, chunk, this.minLightSection, this.maxLightSection);
        } else {
            this.propagateNeighbourLevels(lightAccess, chunk, this.minLightSection, this.maxLightSection);

            this.performLightIncrease(lightAccess);
        }
    }
}
