/*
 * Minecraft Forge - Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.server.permission.exceptions;

import java.util.Locale;
import net.minecraftforge.server.permission.nodes.PermissionNode;

public class UnregisteredPermissionException extends RuntimeException
{
    private PermissionNode node;

    public UnregisteredPermissionException(PermissionNode node)
    {
        super(String.format(Locale.ENGLISH, "Tried to query PermissionNode '%s' although it has not been Registered", node.getNodeName()));
        this.node = node;
    }

    public PermissionNode getNode()
    {
        return node;
    }
}
