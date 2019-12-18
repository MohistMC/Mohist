package net.minecraft.launchwrapper;

import java.io.File;
import java.util.List;

public class AlphaVanillaTweaker implements ITweaker {
    private List<String> args;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args = args;
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.registerTransformer("net.minecraft.launchwrapper.injector.AlphaVanillaTweakInjector");
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.launchwrapper.injector.AlphaVanillaTweakInjector";
    }

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[args.size()]);
    }
}
