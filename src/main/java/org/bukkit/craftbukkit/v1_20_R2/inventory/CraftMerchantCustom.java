package org.bukkit.craftbukkit.v1_20_R2.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;

public class CraftMerchantCustom extends CraftMerchant {

    public CraftMerchantCustom(String title) {
        super(new MinecraftMerchant(title));
        getMerchant().craftMerchant = this;
    }

    @Override
    public String toString() {
        return "CraftMerchantCustom";
    }

    @Override
    public MinecraftMerchant getMerchant() {
        return (MinecraftMerchant) super.getMerchant();
    }

    public static class MinecraftMerchant implements Merchant {

        private final Component title;
        private final MerchantOffers trades = new MerchantOffers();
        private net.minecraft.world.entity.player.Player tradingPlayer;
        protected CraftMerchant craftMerchant;

        public MinecraftMerchant(String title) {
            Preconditions.checkArgument(title != null, "Title cannot be null");
            this.title = CraftChatMessage.fromString(title)[0];
        }

        @Override
        public CraftMerchant getCraftMerchant() {
            return craftMerchant;
        }

        @Override
        public void setTradingPlayer(net.minecraft.world.entity.player.Player entityhuman) {
            this.tradingPlayer = entityhuman;
        }

        @Override
        public net.minecraft.world.entity.player.Player getTradingPlayer() {
            return this.tradingPlayer;
        }

        @Override
        public MerchantOffers getOffers() {
            return this.trades;
        }

        // Paper start
        @Override
        public void processTrade(MerchantOffer merchantRecipe, @javax.annotation.Nullable io.papermc.paper.event.player.PlayerPurchaseEvent event) { // The MerchantRecipe passed in here is the one set by the PlayerPurchaseEvent
            /** Based on {@link net.minecraft.world.entity.npc.AbstractVillager#processTrade(MerchantOffer, io.papermc.paper.event.player.PlayerPurchaseEvent)} */
            if (getTradingPlayer() instanceof net.minecraft.server.level.ServerPlayer) {
                if (event == null || event.willIncreaseTradeUses()) {
                    merchantRecipe.increaseUses();
                }
                if (event == null || event.isRewardingExp()) {
                    this.tradingPlayer.level().addFreshEntity(new net.minecraft.world.entity.ExperienceOrb(this.tradingPlayer.level(), this.tradingPlayer.getX(), this.tradingPlayer.getY(), this.tradingPlayer.getZ(), merchantRecipe.getXp(), org.bukkit.entity.ExperienceOrb.SpawnReason.VILLAGER_TRADE, this.tradingPlayer, null));
                }
            }
            this.notifyTrade(merchantRecipe);
        }
        // Paper end

        @Override
        public void notifyTrade(MerchantOffer merchantrecipe) {
            // increase recipe's uses
            merchantrecipe.increaseUses();
        }

        @Override
        public void notifyTradeUpdated(ItemStack itemstack) {

        }

        public Component getScoreboardDisplayName() {
            return title;
        }

        @Override
        public int getVillagerXp() {
            return 0; // xp
        }

        @Override
        public void overrideXp(int i) {
        }

        @Override
        public boolean showProgressBar() {
            return false; // is-regular-villager flag (hides some gui elements: xp bar, name suffix)
        }

        @Override
        public SoundEvent getNotifyTradeSound() {
            return SoundEvents.VILLAGER_YES;
        }

        @Override
        public void overrideOffers(net.minecraft.world.item.trading.MerchantOffers merchantrecipelist) {
        }

        @Override
        public boolean isClientSide() {
            return false;
        }
    }
}
