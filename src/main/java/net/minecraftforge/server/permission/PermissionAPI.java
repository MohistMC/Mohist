/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.server.permission;

import com.mohistmc.util.i18n.i18n;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.exceptions.UnregisteredPermissionException;
import net.minecraftforge.server.permission.handler.DefaultPermissionHandler;
import net.minecraftforge.server.permission.handler.IPermissionHandler;
import net.minecraftforge.server.permission.handler.IPermissionHandlerFactory;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public final class PermissionAPI
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static IPermissionHandler activeHandler = null;

    public static Collection<PermissionNode<?>> getRegisteredNodes()
    {
        return activeHandler == null ? Collections.emptySet() : activeHandler.getRegisteredNodes();
    }

    private PermissionAPI()
    {
    }

    /**
     * @return the Identifier of the currently active permission handler
     */
    @Nullable
    public static ResourceLocation getActivePermissionHandler()
    {
        return activeHandler == null ? null : activeHandler.getIdentifier();
    }

    /**
     * <p>Queries a player's permission for a given node and contexts</p>
     * <p><strong>Warning:</strong> PermissionNodes <strong>must</strong> be registered using the
     * {@link PermissionGatherEvent.Nodes} event before querying.</p>
     *
     * @param player  player for which you want to check permissions
     * @param node    the PermissionNode for which you want to query
     * @param context optional array of PermissionDynamicContext, single entries will be ignored if they weren't
     *                registered to the node
     * @param <T>     type of the queried PermissionNode
     * @return a value of type {@code <T>}, that the combination of Player and PermissionNode map to, defaults to the
     * PermissionNodes default handler.
     * @throws UnregisteredPermissionException when the PermissionNode wasn't registered properly
     */
    public static <T> T getPermission(ServerPlayer player, PermissionNode<T> node, PermissionDynamicContext<?>... context)
    {
        if (!activeHandler.getRegisteredNodes().contains(node)) throw new UnregisteredPermissionException(node);
        return activeHandler.getPermission(player, node, context);
    }

    /**
     * See {@link PermissionAPI#getPermission(ServerPlayer, PermissionNode, PermissionDynamicContext[])}
     *
     * @param player  offline player for which you want to check permissions
     * @param node    the PermissionNode for which you want to query
     * @param context optional array of PermissionDynamicContext, single entries will be ignored if they weren't
     *                registered to the node
     * @param <T>     type of the queried PermissionNode
     * @return a value of type {@code <T>}, that the combination of Player and PermissionNode map to, defaults to the
     * PermissionNodes default handler.
     * @throws UnregisteredPermissionException when the PermissionNode wasn't registered properly
     */
    public static <T> T getOfflinePermission(UUID player, PermissionNode<T> node, PermissionDynamicContext<?>... context)
    {
        if (!activeHandler.getRegisteredNodes().contains(node)) throw new UnregisteredPermissionException(node);
        return activeHandler.getOfflinePermission(player, node, context);
    }


    /**
     * <p>Helper method for internal use only!</p>
     * <p>Initializes the active permission handler based on the users config.</p>
     */
    public static void initializePermissionAPI()
    {
        Class<?> callerClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        if (callerClass != ServerLifecycleHooks.class)
        {
            LOGGER.warn(i18n.get("permissionapi.1"), callerClass.getName());
            return;
        }

        PermissionAPI.activeHandler = null;

        PermissionGatherEvent.Handler handlerEvent = new PermissionGatherEvent.Handler();
        MinecraftForge.EVENT_BUS.post(handlerEvent);
        Map<ResourceLocation, IPermissionHandlerFactory> availableHandlers = handlerEvent.getAvailablePermissionHandlerFactories();

        try
        {
            ResourceLocation selectedPermissionHandler = new ResourceLocation(ForgeConfig.SERVER.permissionHandler.get());
            if (!availableHandlers.containsKey(selectedPermissionHandler))
            {
                LOGGER.error(i18n.get("permissionapi.2", selectedPermissionHandler, DefaultPermissionHandler.IDENTIFIER));
                selectedPermissionHandler = DefaultPermissionHandler.IDENTIFIER;
            }

            IPermissionHandlerFactory factory = availableHandlers.get(selectedPermissionHandler);

            PermissionGatherEvent.Nodes nodesEvent = new PermissionGatherEvent.Nodes();
            MinecraftForge.EVENT_BUS.post(nodesEvent);

            PermissionAPI.activeHandler = factory.create(nodesEvent.getNodes());

            if(!selectedPermissionHandler.equals(activeHandler.getIdentifier()))
                LOGGER.warn(i18n.get("permissionapi.3", activeHandler.getIdentifier(), selectedPermissionHandler));

            LOGGER.info(i18n.get("permissionapi.4", PermissionAPI.activeHandler.getIdentifier()));
        }
        catch (ResourceLocationException e)
        {
            LOGGER.error(i18n.get("permissionapi.5"), e);
        }
    }
}
