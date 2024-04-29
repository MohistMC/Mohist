package com.mohistmc.mohist.bukkit.entity;

import net.minecraft.world.entity.animal.AbstractGolem;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftGolem;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/13 3:26:33
 */
public class MohistModsGolem extends CraftGolem {

    public MohistModsGolem(CraftServer server, AbstractGolem entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "MohistModsGolem{" + getType() + '}';
    }
}
