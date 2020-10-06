package org.bukkit.craftbukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

public class CraftLootTable implements org.bukkit.loot.LootTable {

    private final LootTable handle;
    private final NamespacedKey key;

    public CraftLootTable(NamespacedKey key, LootTable handle) {
        this.handle = handle;
        this.key = key;
    }

    public LootTable getHandle() {
        return handle;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        net.minecraft.loot.LootContext nmsContext = convertContext(context);
        List<net.minecraft.item.ItemStack> nmsItems = handle.generate(nmsContext);
        Collection<ItemStack> bukkit = new ArrayList<>(nmsItems.size());

        for (net.minecraft.item.ItemStack item : nmsItems) {
            if (item.isEmpty()) {
                continue;
            }
            bukkit.add(CraftItemStack.asBukkitCopy(item));
        }

        return bukkit;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        net.minecraft.loot.LootContext nmsContext = convertContext(context);
        CraftInventory craftInventory = (CraftInventory) inventory;
        IInventory handle = craftInventory.getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        getHandle().fillInventory(handle, nmsContext);
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    private net.minecraft.loot.LootContext convertContext(LootContext context) {
        Location loc = context.getLocation();
        ServerWorld handle = ((CraftWorld) loc.getWorld()).getHandle();

        net.minecraft.loot.LootContext.Builder builder = new net.minecraft.loot.LootContext.Builder(handle);
        builder.withParameter(LootParameters.field_237457_g_, new Vector3d(loc.getX(), loc.getY(), loc.getZ()));
        if (getHandle() != LootTable.EMPTY_LOOT_TABLE) {
            // builder.luck(context.getLuck());

            if (context.getLootedEntity() != null) {
                Entity nmsLootedEntity = ((CraftEntity) context.getLootedEntity()).getHandle();
                builder.withParameter(LootParameters.THIS_ENTITY, nmsLootedEntity);
                builder.withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.GENERIC);
                builder.withParameter(LootParameters.POSITION, new BlockPos(nmsLootedEntity));
            }

            if (context.getKiller() != null) {
                PlayerEntity nmsKiller = ((CraftHumanEntity) context.getKiller()).getHandle();
                builder.withParameter(LootParameters.KILLER_ENTITY, nmsKiller);
                // If there is a player killer, damage source should reflect that in case loot tables use that information
                builder.withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.causePlayerDamage(nmsKiller));
                builder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, nmsKiller); // SPIGOT-5603 - Set minecraft:killed_by_player
            }

            // SPIGOT-5603 - Use LootContext#lootingModifier
            if (context.getLootingModifier() != LootContext.DEFAULT_LOOT_MODIFIER) {
                builder.withParameter(LootParameters.LOOTING_MOD, context.getLootingModifier());
            }
        }

        // SPIGOT-5603 - Avoid IllegalArgumentException in LootTableInfo#build()
        LootParameterSet.Builder nmsBuilder = new LootParameterSet.Builder(); // PAIL rename Builder
        for (LootParameter<?> param : getHandle().getParameterSet().getAllParameters()) { // PAIL rename required
            nmsBuilder.required(param); // PAIL rename addRequired
        }
        for (LootParameter<?> param : getHandle().getParameterSet().getAllParameters()) { // PAIL rename optional
            if (!getHandle().getParameterSet().getAllParameters().contains(param)) { // PAIL rename required
                nmsBuilder.optional(param); // PAIL rename addOptional
            }
        }
        nmsBuilder.optional(LootParameters.LOOTING_MOD);

        return builder.build(nmsBuilder.build());
    }

    public static LootContext convertContext(net.minecraft.loot.LootContext info) {
        Vector3d position = info.get(LootParameters.field_237457_g_);
        Location location = new Location(info.getWorld().getCBWorld(), position.getX(), position.getY(), position.getZ());
        LootContext.Builder contextBuilder = new LootContext.Builder(location);

        if (info.has(LootParameters.KILLER_ENTITY)) {
            CraftEntity killer = info.get(LootParameters.KILLER_ENTITY).getBukkitEntity();
            if (killer instanceof CraftHumanEntity) {
                contextBuilder.killer((CraftHumanEntity) killer);
            }
        }

        if (info.has(LootParameters.THIS_ENTITY)) {
            contextBuilder.lootedEntity(info.get(LootParameters.THIS_ENTITY).getBukkitEntity());
        }

        if (info.has(LootParameters.LOOTING_MOD)) {
            contextBuilder.lootingModifier(info.get(LootParameters.LOOTING_MOD));
        }

        contextBuilder.luck(info.getLuck());
        return contextBuilder.build();
    }

    @Override
    public String toString() {
        return getKey().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof org.bukkit.loot.LootTable)) {
            return false;
        }

        org.bukkit.loot.LootTable table = (org.bukkit.loot.LootTable) obj;
        return table.getKey().equals(this.getKey());
    }
}
