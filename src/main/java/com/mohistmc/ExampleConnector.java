package com.mohistmc;

import org.spongepowered.asm.mixin.connect.IMixinConnector;

/**
 * @author Mgazul by MohistMC
 * @date 2023/4/21 16:19:04
 */
public class ExampleConnector implements IMixinConnector {

    @Override
    public void connect() {
        System.out.println("Mohist mixin test");
    }
}
