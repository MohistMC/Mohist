package red.mohist.forge;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.mohist.bukkit.BukkitPermissionsHandler;

@Mod("mohist")
public class MohistMod {

    public static final Logger LOGGER = LogManager.getLogger("Mohist");

    public MohistMod() {
        LOGGER.info("mod loaded...");
        PermissionAPI.setPermissionHandler(new BukkitPermissionsHandler());
        MohistMod.LOGGER.info("Registered Forge API Permission Handler");
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }
}
