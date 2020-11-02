package com.mohistmc.inventory.meta;

import com.mohistmc.api.ItemAPI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;

public class ForgeCapMeta implements Cloneable {

    protected NBTTagCompound capNBT;

    public ForgeCapMeta(NBTTagCompound capNBT) {
        this.capNBT = capNBT;
    }

    public NBTTagCompound getCap() {
        return capNBT.copy();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ForgeCapMeta)) {
            return false;
        }

        return this.capNBT.equals(((ForgeCapMeta) obj).capNBT);
    }

    @Override
    public int hashCode() {
        return this.capNBT.hashCode();
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
        if (nmsItemStack != null && nmsItemStack.capabilities != null) {
            NBTTagCompound capNBT = nmsItemStack.capabilities.serializeNBT();
            if (capNBT != null && !capNBT.hasNoTags()) {
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
                NBTTagCompound capNBT = CompressedStreamTools.readCompressed(buf);
                return new ForgeCapMeta(capNBT);
            } catch (IOException ex) {
            }
        }
        return null;
    }
}
