package org.bukkit.craftbukkit.inventory;

import javax.annotation.Nullable;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import org.apache.commons.lang.Validate;

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

    public static class MinecraftMerchant implements IMerchant {

        private final ITextComponent title;
        private final MerchantOffers trades = new MerchantOffers();
        private PlayerEntity tradingPlayer;
        private World tradingWorld;
        protected CraftMerchant craftMerchant;

        public MinecraftMerchant(String title) {
            Validate.notNull(title, "Title cannot be null");
            this.title = new StringTextComponent(title);
        }

        @Override
        public CraftMerchant getCraftMerchant() {
            return craftMerchant;
        }

        @Nullable
        @Override
        public PlayerEntity getCustomer() {
            return this.tradingPlayer;
        }

        @Override
        public void setCustomer(@Nullable PlayerEntity player) {
            this.tradingPlayer = player;
            if (player != null) {
                this.tradingWorld = player.world;
            }
        }


        @Override
        public MerchantOffers getOffers() {
            return this.trades;
        }

        @Override
        public void setClientSideOffers(@Nullable MerchantOffers p_213703_1_) {

        }

        @Override
        public void onTrade(MerchantOffer p_213704_1_) {
            p_213704_1_.increaseUses();
        }

        @Override
        public void verifySellingItem(ItemStack stack) {

        }

        public ITextComponent getScoreboardDisplayName() {
            return title;
        }

        @Override
        public World getWorld() {
            return this.tradingWorld;
        }

        @Override
        public int getXp() {
            return 0;
        }

        @Override
        public void setXP(int p_213702_1_) {

        }

        @Override
        public SoundEvent getYesSound() {
            return SoundEvents.ENTITY_VILLAGER_YES;
        }

        @Override
        public boolean func_213705_dZ() {
            return false; // is-regular-villager flag (hides some gui elements: xp bar, name suffix)
        }
    }
}
