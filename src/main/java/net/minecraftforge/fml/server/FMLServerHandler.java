/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
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

package net.minecraftforge.fml.server;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PendingCommand;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.IOUtils;
import red.mohist.util.i18n.Message;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Handles primary communication from hooked code into the system
 * <p>
 * The FML entry point is {@link #beginServerLoading(MinecraftServer)} called from
 * {@link net.minecraft.server.dedicated.DedicatedServer}
 * <p>
 * Obfuscated code should focus on this class and other members of the "server"
 * (or "client") code
 * <p>
 * The actual mod loading is handled at arms length by {@link Loader}
 * <p>
 * It is expected that a similar class will exist for each target environment:
 * Bukkit and Client side.
 * <p>
 * It should not be directly modified.
 *
 * @author cpw
 */
public class FMLServerHandler implements IFMLSidedHandler {
    /**
     * The singleton
     */
    private static final FMLServerHandler INSTANCE = new FMLServerHandler();
    private final List<String> injectedModContainers;
    /**
     * A reference to the server itself
     */
    private MinecraftServer server;

    private FMLServerHandler() {
        injectedModContainers = FMLCommonHandler.instance().beginLoading(this);
    }

    /**
     * @return the instance
     */
    public static FMLServerHandler instance() {
        return INSTANCE;
    }

    /**
     * Called to start the whole game off from
     * {@link MinecraftServer#startServer}
     *
     * @param minecraftServer server
     */
    @Override
    public void beginServerLoading(MinecraftServer minecraftServer) {
        server = minecraftServer;
        Loader.instance().loadMods(injectedModContainers);
        Loader.instance().preinitializeMods();
    }

    /**
     * Called a bit later on during server initialization to finish loading mods
     */
    @Override
    public void finishServerLoading() {
        Loader.instance().initializeMods();
    }

    @Override
    public void haltGame(String message, Throwable exception) {
        throw new RuntimeException(message, exception);
    }

    @Override
    public File getSavesDirectory() {
        return ((SaveFormatOld) server.getActiveAnvilConverter()).savesDirectory;
    }

    /**
     * Get the server instance
     */
    @Override
    public MinecraftServer getServer() {
        return server;
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.IFMLSidedHandler#getAdditionalBrandingInformation()
     */
    @Override
    public List<String> getAdditionalBrandingInformation() {
        return ImmutableList.of();
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.IFMLSidedHandler#getSide()
     */
    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    @Override
    public void showGuiScreen(Object clientGuiElement) {

    }

    @Override
    public void queryUser(StartupQuery query) throws InterruptedException {
        if (query.getResult() == null) {
            FMLLog.log.warn(query.getText());
            query.finish();
        } else {
            String text = query.getText() +
                    "\n\n" + Message.getString("forge.fmlserverhandler.1") +
                    "\n" + Message.getString("forge.fmlserverhandler.2");

            FMLLog.log.warn(text);

            if (!query.isSynchronous()) return; // no-op until mc does commands in another thread (if ever)

            boolean done = false;

            while (!done && server.isServerRunning()) {
                if (Thread.interrupted()) throw new InterruptedException();

                DedicatedServer dedServer = (DedicatedServer) server;

                // rudimentary command processing, check for fml confirm/cancel and stop commands
                synchronized (dedServer.pendingCommandList) {
                    for (Iterator<PendingCommand> it = dedServer.pendingCommandList.iterator(); it.hasNext(); ) {
                        String cmd = it.next().command.trim().toLowerCase();
                        cmd = cmd.charAt(0) == '/' ? cmd.substring(1) : cmd; // strip the forward slash to make it optional
                        if (cmd.equals("fml confirm")) {
                            FMLLog.log.info(Message.getString("forge.fmlserverhandler.3"));
                            query.setResult(true);
                            done = true;
                            it.remove();
                        } else if (cmd.equals("fml cancel")) {
                            FMLLog.log.info(Message.getString("forge.fmlserverhandler.4"));
                            query.setResult(false);
                            done = true;
                            it.remove();
                        } else if (cmd.equals("stop")) {
                            StartupQuery.abort();
                        }
                    }
                }

                Thread.sleep(10L);
            }

            query.finish();
        }
    }

    @Override
    public boolean isDisplayCloseRequested() {
        return false;
    }

    @Override
    public boolean shouldServerShouldBeKilledQuietly() {
        return false;
    }

    @Override
    public void addModAsResource(ModContainer container) {
        String langFile = "assets/" + container.getModId().toLowerCase() + "/lang/" + Message.getLocale().toLowerCase() + ".lang";
        String langFile2 = "assets/" + container.getModId().toLowerCase() + "/lang/" + Message.getLocale() + ".lang";
        String langFile3 = "assets/" + container.getModId().toLowerCase() + "/lang/en_us.lang";
        String langFile4 = "assets/" + container.getModId().toLowerCase() + "/lang/en_US.lang";
        File source = container.getSource();
        InputStream stream = null;
        ZipFile zip = null;
        try {
            if (source.isDirectory() && FMLLaunchHandler.isDeobfuscatedEnvironment()) {
                File f = new File(source.toURI().resolve(langFile).getPath());
                if (!f.exists())
                    f = new File(source.toURI().resolve(langFile2).getPath());
                if (!f.exists())
                    f = new File(source.toURI().resolve(langFile3).getPath());
                if (!f.exists())
                    f = new File(source.toURI().resolve(langFile4).getPath());
                if (!f.exists())
                    throw new FileNotFoundException(source.toURI().resolve(langFile).getPath());
                stream = new FileInputStream(f);
            } else if (source.exists()) //Fake sources.. Yay coremods -.-
            {
                zip = new ZipFile(source);
                ZipEntry entry = zip.getEntry(langFile);
                if (entry == null) entry = zip.getEntry(langFile2);
                if (entry == null) entry = zip.getEntry(langFile3);
                if (entry == null) entry = zip.getEntry(langFile4);
                if (entry == null) throw new FileNotFoundException(langFile);
                stream = zip.getInputStream(entry);
            }
            if (stream != null)
                LanguageMap.inject(stream);
        } catch (FileNotFoundException e) {
            FMLLog.log.debug("Missing English translation for {}: {}", container.getModId(), e.getMessage());
        } catch (IOException e) {
            // hush
        } catch (Exception e) {
            FMLLog.log.error(e);
        } finally {
            IOUtils.closeQuietly(stream);
            IOUtils.closeQuietly(zip);
        }
    }

    @Override
    public String getCurrentLanguage() {
        return Message.getLocale();
    }

    @Override
    public void serverStopped() {
        // NOOP
    }

    @Override
    public NetworkManager getClientToServerNetworkManager() {
        throw new RuntimeException("Missing");
    }

    @Override
    public INetHandler getClientPlayHandler() {
        return null;
    }

    @Override
    public void fireNetRegistrationEvent(EventBus bus, NetworkManager manager, Set<String> channelSet, String channel, Side side) {
        bus.post(new FMLNetworkEvent.CustomPacketRegistrationEvent<NetHandlerPlayServer>(manager, channelSet, channel, side, NetHandlerPlayServer.class));
    }

    @Override
    public boolean shouldAllowPlayerLogins() {
        return DedicatedServer.allowPlayerLogins;
    }

    @Override
    public void allowLogins() {
        DedicatedServer.allowPlayerLogins = true;
    }

    @Override
    public IThreadListener getWorldThread(INetHandler net) {
        // Always the server on the dedicated server, eventually add Per-World if Mojang adds world stuff.
        return getServer();
    }

    @Override
    public void processWindowMessages() {
        // NOOP
    }

    @Override
    public String stripSpecialChars(String message) {
        return message;
    }

    @Override
    public void reloadRenderers() {
        // NOOP
    }

    @Override
    public void fireSidedRegistryEvents() {
        // NOOP
    }

    @Override
    public CompoundDataFixer getDataFixer() {
        return (CompoundDataFixer) this.server.getDataFixer();
    }

    @Override
    public boolean isDisplayVSyncForced() {
        return false;
    }
}