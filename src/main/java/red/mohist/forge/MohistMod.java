package red.mohist.forge;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class MohistMod extends DummyModContainer {

    public MohistMod(ModMetadata metadata) {
        super(metadata);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }

    @Override
    public Disableable canBeDisabled()
    {
        return Disableable.YES;
    }
}
