package net.minecraftforge.cauldron.apiimpl;

import net.minecraftforge.cauldron.api.Cauldron;
import net.minecraftforge.cauldron.api.CauldronApi;
import net.minecraftforge.cauldron.api.inventory.BukkitOreDictionary;
import net.minecraftforge.cauldron.apiimpl.inventory.OreDictionaryInterface;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class CauldronPluginInterface implements CauldronApi {
    private BukkitOreDictionary oreDictionary = new OreDictionaryInterface();

    public void install() {
        Cauldron.setInterface(this);
        Bukkit.getServicesManager().register(CauldronApi.class, this, null, ServicePriority.Highest);
    }

    @Override
    public BukkitOreDictionary getOreDictionary() {
        return oreDictionary;
    }
}
