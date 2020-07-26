package red.mohist.forge;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileLocator;
import net.minecraftforge.forgespi.locating.IModFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

public abstract class MohistLocator extends AbstractJarFileLocator {

    private final IModFile mohist;

    public MohistLocator() {
        this.mohist = loadJars();
        this.modJars.put(mohist, createFileSystem(mohist));
    }

    protected abstract IModFile loadJars();

    @Override
    public List<IModFile> scanMods() {
        return ImmutableList.of(mohist);
    }

    @Override
    public String name() {
        return "mohist";
    }

    @Override
    public void initArguments(Map<String, ?> arguments) {
    }

}
