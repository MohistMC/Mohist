package cc.uraniummc;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.WorldServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;

public class ReverseClonner {
    public static EntityPlayerMP clone(EntityPlayerMP player, boolean wasDeath) {
        if(MinecraftServer.uraniumConfig.enableForgeRespawnClone.getValue()) {
            EntityPlayerMP shadowCopy = new EntityPlayerMP(player.mcServer, (WorldServer) player.worldObj,
                    player.getGameProfile(), new ItemInWorldManager(player.worldObj));
            shadowCopy.bukkitEntity = player.bukkitEntity;
            shadowCopy.playerNetServerHandler = player.playerNetServerHandler;
            shadowCopy.playerNetServerHandler.playerEntity = player;
            shadowCopy.setEntityId(player.getEntityId());
            shadowCopy.timeOffset=player.timeOffset;
            CraftPlayer craftPlayer=(CraftPlayer) player.bukkitEntity;
            /*
            if (wasDeath && !player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
                shadowCopy.inventory.clearInventory(null, -1);
                shadowCopy.inventoryContainer = new ContainerPlayer(shadowCopy.inventory, !player.worldObj.isRemote, player);
            }
            */
            if(craftPlayer!=null){
                shadowCopy.bukkitEntity=craftPlayer;
                craftPlayer.setHandle(shadowCopy);
            }
            shadowCopy.clonePlayer(player, !wasDeath);
            if(!wasDeath){
                shadowCopy.activePotionsMap.clear();
                shadowCopy.activePotionsMap.putAll(player.activePotionsMap);
                /*
                shadowCopy.experienceLevel=player.experienceLevel;
                shadowCopy.experienceTotal=player.experienceTotal;
                shadowCopy.experience=player.experience;
                shadowCopy.setHealth(player.getHealth());
                shadowCopy.setFoodStats(player.getFoodStats());
                shadowCopy.setScore(player.getScore());
                shadowCopy.setTeleportDirection(player.getTeleportDirection());
                shadowCopy.setExtendedProperties(player.getExtendedProperties());
                for(IExtendedEntityProperties prop:shadowCopy.getExtendedProperties().values()){
                    prop.init(shadowCopy,shadowCopy.worldObj);
                    shadowCopy.activePotionsMap.clear();
                    shadowCopy.activePotionsMap.putAll(player.activePotionsMap);
                    shadowCopy.setScore(player.getScore());
                }
                shadowCopy.experienceTotal=player.experienceTotal;
                */
            }

            if(craftPlayer != null){
                craftPlayer.setEnderChest(new CraftInventory(player.getInventoryEnderChest()));
                craftPlayer.setInventory(new CraftInventoryPlayer(player.inventory));
            }
            return shadowCopy;
        }
        return player;
    }
}
