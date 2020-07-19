package red.mohist.api.world;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {

    @Override
    public byte[] generate(World world, Random random, int x, int z) {
        return new byte[32678];
    }
}
