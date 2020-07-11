package org.bukkit.craftbukkit.v1_12_R1.advancement;

import com.google.common.collect.Lists;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.PlayerAdvancements;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class CraftAdvancementProgress implements AdvancementProgress {

    private final CraftAdvancement advancement;
    private final PlayerAdvancements playerData;
    private final net.minecraft.advancements.AdvancementProgress handle;

    public CraftAdvancementProgress(CraftAdvancement advancement, PlayerAdvancements player, net.minecraft.advancements.AdvancementProgress handle) {
        this.advancement = advancement;
        this.playerData = player;
        this.handle = handle;
    }

    @Override
    public Advancement getAdvancement() {
        return advancement;
    }

    @Override
    public boolean isDone() {
        return handle.isDone();
    }

    @Override
    public boolean awardCriteria(String criteria) {
        return playerData.grantCriterion(advancement.getHandle(), criteria);
    }

    @Override
    public boolean revokeCriteria(String criteria) {
        return playerData.revokeCriterion(advancement.getHandle(), criteria);
    }

    @Override
    public Date getDateAwarded(String criteria) {
        CriterionProgress criterion = handle.getCriterionProgress(criteria);
        return (criterion == null) ? null : criterion.getObtained();
    }

    @Override
    public Collection<String> getRemainingCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(handle.getRemaningCriteria()));
    }

    @Override
    public Collection<String> getAwardedCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(handle.getCompletedCriteria()));
    }
}
