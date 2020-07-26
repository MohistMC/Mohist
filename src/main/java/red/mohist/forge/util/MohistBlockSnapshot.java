package red.mohist.forge.util;

import net.minecraft.block.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;

public class MohistBlockSnapshot extends CraftBlock {

    private final BlockState blockState;

    public MohistBlockSnapshot(BlockSnapshot blockSnapshot, boolean current) {
        super(blockSnapshot.getWorld(), blockSnapshot.getPos());
        this.blockState = current ? blockSnapshot.getCurrentBlock() : blockSnapshot.getReplacedBlock();
    }

    @Override
    public BlockState getNMS() {
        return blockState;
    }

    public static MohistBlockSnapshot fromBlockSnapshot(BlockSnapshot blockSnapshot, boolean current) {
        return new MohistBlockSnapshot(blockSnapshot, current);
    }
}
