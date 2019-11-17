package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
// CraftBukkit - package-private -> public
public class TileEntityCommandBlockListener extends CommandBlockLogic
{
    final TileEntityCommandBlock field_145767_a;

    TileEntityCommandBlockListener(TileEntityCommandBlock p_i45441_1_)
    {
        this.field_145767_a = p_i45441_1_;
        sender = new org.bukkit.craftbukkit.command.CraftBlockCommandSender(this); // CraftBukkit - add sender
    }

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(this.field_145767_a.xCoord, this.field_145767_a.yCoord, this.field_145767_a.zCoord);
    }

    public World getEntityWorld()
    {
        return this.field_145767_a.getWorldObj();
    }

    public void func_145752_a(String p_145752_1_)
    {
        super.func_145752_a(p_145752_1_);
        this.field_145767_a.markDirty();
    }

    public void func_145756_e()
    {
        this.field_145767_a.getWorldObj().markBlockForUpdate(this.field_145767_a.xCoord, this.field_145767_a.yCoord, this.field_145767_a.zCoord);
    }

    @SideOnly(Side.CLIENT)
    public int func_145751_f()
    {
        return 0;
    }
    @SideOnly(Side.CLIENT)
    public void func_145757_a(ByteBuf p_145757_1_)
    {
        p_145757_1_.writeInt(field_145767_a.xCoord);
        p_145757_1_.writeInt(field_145767_a.yCoord);
        p_145757_1_.writeInt(field_145767_a.zCoord);
    }
}