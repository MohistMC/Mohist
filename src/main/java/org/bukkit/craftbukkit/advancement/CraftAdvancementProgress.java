package org.bukkit.craftbukkit.advancement;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionProgress;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;

public class CraftAdvancementProgress implements AdvancementProgress {

    private final CraftAdvancement advancement;
    private final PlayerAdvancementTracker playerData;
    private final net.minecraft.advancement.AdvancementProgress handle;

    public CraftAdvancementProgress(CraftAdvancement advancement, PlayerAdvancementTracker player, net.minecraft.advancement.AdvancementProgress handle) {
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
        return (criterion == null) ? null : criterion.getObtainedDate();
    }

    @Override
    public Collection<String> getRemainingCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(handle.getUnobtainedCriteria()));
    }

    @Override
    public Collection<String> getAwardedCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(handle.getObtainedCriteria()));
    }
}
