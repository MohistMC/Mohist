package cc.uraniummc;

import com.google.common.base.Charsets;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;

public class UraniumUUIDManager {
    public static UUID getOfflineUUID(String name){
        switch (MinecraftServer.uraniumConfig.uuidMode.getValue()){
            case 1:
                name=name.toLowerCase();
                break;
            case 2:
                name=name.toUpperCase();
                break;
        }
        return UUID.nameUUIDFromBytes( ( "OfflinePlayer:" + name ).getBytes( Charsets.UTF_8 ) );
    }
}
