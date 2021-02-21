package com.mohistmc.inventory;

import com.mohistmc.api.ItemAPI;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;

public class ForgeCapMeta implements Cloneable {

    protected static CompoundNBT capNBT;
    public static net.minecraft.item.ItemStack nmsItemStack;

    public ForgeCapMeta(CompoundNBT capNBT) {
        ForgeCapMeta.capNBT = capNBT;
    }

    public static CompoundNBT getCap() {
        return nmsItemStack.serializeCaps();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ForgeCapMeta)) {
            return false;
        }

        return capNBT.equals(capNBT);
    }

    @Override
    public int hashCode() {
        return capNBT.hashCode();
    }

    @Override
    public ForgeCapMeta clone() {
        try {
            return (ForgeCapMeta) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public static void setCap(net.minecraft.item.ItemStack nmsItemStack, ItemStack bukkitItemStack) {
        if (nmsItemStack != null && nmsItemStack != null) {
            CompoundNBT capNBT = nmsItemStack.serializeNBT();
            if (capNBT != null && !capNBT.isEmpty()) {
                bukkitItemStack.setForgeCapMeta(new ForgeCapMeta(capNBT));
            }
        }
    }

    public String serializeNBT() {
        return ItemAPI.serializeNBT(capNBT);
    }

    public static ForgeCapMeta deserializeNBT(String serializedNBT) {
        if (serializedNBT != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.decodeBase64(serializedNBT));
            try {
                CompoundNBT capNBT = CompressedStreamTools.readCompressed(buf);
                return new ForgeCapMeta(capNBT);
            } catch (IOException ex) {
            }
        }
        return null;
    }
}