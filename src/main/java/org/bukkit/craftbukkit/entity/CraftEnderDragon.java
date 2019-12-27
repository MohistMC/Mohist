package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Set;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.boss.CraftBossBar;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {

    private BossBar bossBar;

    public CraftEnderDragon(CraftServer server, EnderDragonEntity entity) {
        super(server, entity);

        if (entity.getEnderDragonBattle() != null) {
            this.bossBar = new CraftBossBar(entity.getEnderDragonBattle().bossBattle);
        }
    }

    @Override
    public Set<ComplexEntityPart> getParts() {
        Builder<ComplexEntityPart> builder = ImmutableSet.builder();

        for (EnderDragonPartEntity part : getHandle().children) {
            builder.add((ComplexEntityPart) part.getBukkitEntity());
        }

        return builder.build();
    }

    @Override
    public EnderDragonEntity getHandle() {
        return (EnderDragonEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderDragon";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public Phase getPhase() {
        return Phase.values()[getHandle().getDataWatcher().get(EnderDragonEntity.PHASE)];
    }

    @Override
    public void setPhase(Phase phase) {
        getHandle().getDragonControllerManager().setControllerPhase(getMinecraftPhase(phase));
    }

    public static Phase getBukkitPhase(PhaseType phase) {
        return Phase.values()[phase.b()];
    }

    public static PhaseType getMinecraftPhase(Phase phase) {
        return PhaseType.getById(phase.ordinal());
    }

    @Override
    public BossBar getBossBar() {
        return bossBar;
    }
}
