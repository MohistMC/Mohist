package red.mohist.extra.entity;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.text.Text;

public interface ExtraBeaconBlockEntity {

    StatusEffect getPrimary();

    StatusEffect getSecondary();

    Text getCustomName();

    ContainerLock getLock();
}
