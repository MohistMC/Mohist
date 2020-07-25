package io.izzel.arclight.common.mixin.core.world.chunk;

import io.izzel.arclight.common.bridge.world.WorldBridge;
import io.izzel.arclight.common.bridge.world.chunk.ChunkBridge;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import org.bukkit.Bukkit;
import org.bukkit.event.world.ChunkLoadEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Chunk.class)
public abstract class ChunkMixin implements ChunkBridge {

    // @formatter:off
    @Shadow @Nullable public abstract BlockState setBlockState(BlockPos pos, BlockState state, boolean isMoving);
    @Shadow @Final public World world;
    @Shadow @Final private ChunkPos pos;
    @Shadow private volatile boolean dirty;
    @Shadow private boolean hasEntities;
    @Shadow private long lastSaveTime;
    // @formatter:on

    public org.bukkit.Chunk bukkitChunk;
    public boolean mustNotSave;
    public boolean needsDecoration;
    private transient boolean arclight$doPlace;

    public org.bukkit.Chunk getBukkitChunk() {
        return bukkitChunk;
    }

    @Override
    public org.bukkit.Chunk bridge$getBukkitChunk() {
        return bukkitChunk;
    }

    @Override
    public void bridge$setBukkitChunk(org.bukkit.Chunk chunk) {
        this.bukkitChunk = chunk;
    }

    public BlockState setType(BlockPos pos, BlockState state, boolean isMoving, boolean doPlace) {
        return this.bridge$setType(pos, state, isMoving, doPlace);
    }

    @Override
    public BlockState bridge$setType(BlockPos pos, BlockState state, boolean isMoving, boolean doPlace) {
        this.arclight$doPlace = doPlace;
        try {
            return this.setBlockState(pos, state, isMoving);
        } finally {
            this.arclight$doPlace = true;
        }
    }

    @Override
    public boolean bridge$isMustNotSave() {
        return this.mustNotSave;
    }

    @Override
    public void bridge$setMustNotSave(boolean mustNotSave) {
        this.mustNotSave = mustNotSave;
    }

    @Override
    public boolean bridge$isNeedsDecoration() {
        return this.needsDecoration;
    }

    @Override
    public void bridge$loadCallback() {
        loadCallback();
    }

    @Override
    public void bridge$unloadCallback() {
        unloadCallback();
    }

    public void loadCallback() {
        org.bukkit.Server server = Bukkit.getServer();
        if (server != null) {
            /*
             * If it's a new world, the first few chunks are generated inside
             * the World constructor. We can't reliably alter that, so we have
             * no way of creating a CraftWorld/CraftServer at that point.
             */
            server.getPluginManager().callEvent(new ChunkLoadEvent(this.bukkitChunk, this.needsDecoration));

            if (this.needsDecoration) {
                this.needsDecoration = false;
                java.util.Random random = new java.util.Random();
                random.setSeed(world.getSeed());
                long xRand = random.nextLong() / 2L * 2L + 1L;
                long zRand = random.nextLong() / 2L * 2L + 1L;
                random.setSeed((long) this.pos.x * xRand + (long) this.pos.z * zRand ^ world.getSeed());

                org.bukkit.World world = ((WorldBridge) this.world).bridge$getWorld();
                if (world != null) {
                    ((WorldBridge) this.world).bridge$setPopulating(true);
                    try {
                        for (org.bukkit.generator.BlockPopulator populator : world.getPopulators()) {
                            populator.populate(world, random, bukkitChunk);
                        }
                    } finally {
                        ((WorldBridge) this.world).bridge$setPopulating(false);
                    }
                }
                server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkPopulateEvent(bukkitChunk));
            }
        }
    }

    public void unloadCallback() {
        org.bukkit.Server server = Bukkit.getServer();
        org.bukkit.event.world.ChunkUnloadEvent unloadEvent = new org.bukkit.event.world.ChunkUnloadEvent(this.bukkitChunk, this.isModified());
        server.getPluginManager().callEvent(unloadEvent);
        // note: saving can be prevented, but not forced if no saving is actually required
        this.mustNotSave = !unloadEvent.isSaveChunk();
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/ChunkPrimer;)V",
        at = @At("RETURN"))
    public void arclight$setNeedsDecoration(World worldIn, ChunkPrimer p_i49947_2_, CallbackInfo ci) {
        this.needsDecoration = true;
    }

    @Redirect(method = "setBlockState", at = @At(value = "FIELD", ordinal = 1, target = "Lnet/minecraft/world/World;isRemote:Z"))
    public boolean arclight$redirectIsRemote(World world) {
        return world.isRemote && this.arclight$doPlace;
    }

    /**
     * @author IzzelAliz
     * @reason
     */
    @Overwrite
    public boolean isModified() {
        return !this.mustNotSave && (this.dirty || this.hasEntities && this.world.getGameTime() != this.lastSaveTime);
    }
}
