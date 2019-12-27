package red.mohist.launcher;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

/**
 * @author Mgazul
 * @date 2019/12/21 9:01
 */
public class Launcher implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration("mixins.mohist.api.json");
    }
}
