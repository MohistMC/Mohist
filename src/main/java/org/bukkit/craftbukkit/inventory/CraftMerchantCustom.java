package org.bukkit.craftbukkit.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.Trader;
import net.minecraft.village.TraderOfferList;
import net.minecraft.world.World;
import org.apache.commons.lang.Validate;

import javax.annotation.Nullable;

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

    public static class MinecraftMerchant implements Trader {

        private final Text title;
        private final TraderOfferList trades = new TraderOfferList();
        private PlayerEntity tradingPlayer;
        private World tradingWorld;
        protected CraftMerchant craftMerchant;

        public MinecraftMerchant(String title) {
            Validate.notNull(title, "Title cannot be null");
            this.title = new LiteralText(title);
        }

        @Override
        public CraftMerchant getCraftMerchant() {
            return craftMerchant;
        }

        @Override
        public void setCurrentCustomer(PlayerEntity entityhuman) {
            this.tradingPlayer = entityhuman;
            if (entityhuman != null) {
                this.tradingWorld = entityhuman.world;
            }
        }

        @Override
        public PlayerEntity getCurrentCustomer() {
            return this.tradingPlayer;
        }

        @Override
        public TraderOfferList getOffers() {
            return this.trades;
        }

        @Override
        public void setOffersFromServer(@Nullable TraderOfferList traderOfferList) {

        }

        @Override
        public void trade(TradeOffer merchantrecipe) {
            // increase recipe's uses
            merchantrecipe.use();
        }

        @Override
        public void onSellingItem(ItemStack itemstack) {
        }

        public Text getScoreboardDisplayName() {
            return title;
        }

        @Override
        public World getTraderWorld() {
            return this.tradingWorld;
        }

        @Override
        public int getExperience() {
            return 0; // xp
        }

        @Override
        public void setExperienceFromServer(int i) {
        }

        @Override
        public boolean isLevelledTrader() {
            return false; // is-regular-villager flag (hides some gui elements: xp bar, name suffix)
        }

        @Override
        public SoundEvent getYesSound() {
            return SoundEvents.ENTITY_VILLAGER_YES;
        }
    }
}
