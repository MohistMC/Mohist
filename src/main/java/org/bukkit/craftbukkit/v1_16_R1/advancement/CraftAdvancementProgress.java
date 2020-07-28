package org.bukkit.craftbukkit.v1_16_R1.advancement;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionProgress;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.NotNull;

public class CraftAdvancementProgress implements Advancement {
    private final CraftAdvancement advancement;
    private final PlayerAdvancementTracker playerData;
    private final AdvancementProgress handle;

    public CraftAdvancementProgress(CraftAdvancement advancement, PlayerAdvancementTracker player, AdvancementProgress handle) {
        this.advancement = advancement;
        this.playerData = player;
        this.handle = handle;
    }

    public Advancement getAdvancement() {
        return this.advancement;
    }

    public boolean isDone() {
        return this.handle.isDone();
    }

    public boolean awardCriteria(String criteria) {
        return this.playerData.grantCriterion(this.advancement.getHandle(), criteria);
    }

    public boolean revokeCriteria(String criteria) {
        return this.playerData.revokeCriterion(this.advancement.getHandle(), criteria);
    }

    public Date getDateAwarded(String criteria) {
        CriterionProgress criterion = this.handle.getCriterionProgress(criteria);
        return (criterion == null) ? null : criterion.getObtainedDate();
    }


    public Collection<String> getRemainingCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(this.handle.getUnobtainedCriteria()));
    }

    @Override
    public @NotNull Collection<String> getCriteria() {
        return getCriteria();
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return getKey();
    }
}
