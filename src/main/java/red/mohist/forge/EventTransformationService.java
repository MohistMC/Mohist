package red.mohist.forge;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *This class is make BukkitHookForge event add.
 *
 * @author 1798643961
 */

public class EventTransformationService implements ITransformationService {

    public String name() {
        return "EventTransformationService";
    }
    public void initialize(IEnvironment environment) {}

    @Override
    public void beginScanning(IEnvironment environment) {

    }

    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {}

    public List<ITransformer> transformers() {
        return Arrays.asList(new EventAddTransformer());
    }
}