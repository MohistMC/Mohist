package red.mohist.extra.network;

import org.bukkit.Location;

public interface ExtraPlayNetWorkHandler {

    public void chat(String message, boolean notDeprecated);

    public void teleport(Location location);

    public boolean isDisconnected();

}