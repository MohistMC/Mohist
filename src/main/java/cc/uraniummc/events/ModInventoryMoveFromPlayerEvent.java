package cc.uraniummc.events;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

/**
 * Created by xjboss on 2017/7/3.
 */
public class ModInventoryMoveFromPlayerEvent extends InventoryMoveItemEvent {
    public ModInventoryMoveFromPlayerEvent(InventoryPlayer sourceInventory, net.minecraft.item.ItemStack itemStack, IInventory destinationInventory, boolean didSourceInitiate) {
        super(new CraftInventoryPlayer(sourceInventory), CraftItemStack.asCraftMirror(itemStack),new CraftInventory(destinationInventory) , didSourceInitiate);
    }
}
