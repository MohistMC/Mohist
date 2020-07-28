package red.mohist.mixin.server;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Executor;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.event.server.ServerLoadEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.command.DataCommandStorage;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.WorldSaveHandler;
import red.mohist.extra.server.ExtraMinecraftServer;

@Mixin(value=MinecraftServer.class)
public abstract class MixinMinecraftServer implements ExtraMinecraftServer {

    @Shadow
    @Final
    public RegistryTracker.Modifiable dimensionTracker;

    @Shadow
    @Final
    public WorldSaveHandler field_24371;

    @Shadow
    private Map<RegistryKey<net.minecraft.world.World>, ServerWorld> worlds;

    @Shadow
    public ServerResourceManager serverResourceManager;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    public Executor workerExecutor;

    @Shadow
    public void initScoreboard(PersistentStateManager arg0) {}

    @Shadow
    public DataCommandStorage dataCommandStorage;

    public java.util.Queue<Runnable> processQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>();

    @Overwrite
    public String getServerModName() {
        return "Fabric,Bukkit";
    }

    @Override
    public Map<RegistryKey<net.minecraft.world.World>, ServerWorld> getWorldMap() {
        return worlds;
    }

    @Override
    public void convertWorld(String name) {
        //getServer().upgradeWorld(name);
    }

    @Override
    public WorldGenerationProgressListenerFactory getWorldGenerationProgressListenerFactory() {
        return CraftServer.server.worldGenerationProgressListenerFactory;
    }

    @Override
    public Queue<Runnable> getProcessQueue() {
        return processQueue;
    }

    @Override
    public CommandManager setCommandManager(CommandManager commandManager) {
        return (commandManager);
    }

    public MinecraftServer getServer() {
        return (MinecraftServer) (Object) this;
    }

    @Inject(at = @At("TAIL"), method = "loadWorld")
    public void afterWorldLoad(CallbackInfo ci) {
        CraftServer bukkit = ((CraftServer)Bukkit.getServer());

        bukkit.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD);
        bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
    }

}