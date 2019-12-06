package cc.uraniummc;

import net.minecraft.block.Block;
import net.minecraft.world.NextTickListEntry;

public class BlockUpdateEntry extends BlockCoords implements Comparable<BlockUpdateEntry> {
    public final int priority;
    public final long time;
    public final Block block;
    public final long id;
    private NextTickListEntry mcEntry;

    public BlockUpdateEntry(int x, int y, int z, int priority, long time, Block block, long id) {
        super(x, y, z);
        this.priority = priority;
        this.time = time;
        this.block = block;
        this.id = id;
    }

    public NextTickListEntry asMCEntry() {
        if (mcEntry == null) {
            mcEntry = new NextTickListEntry(x, y, z, block);
            mcEntry.setPriority(priority);
            mcEntry.setScheduledTime(time);
        }
        return mcEntry;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash * 31 + priority;
        hash = hash * 31 + (int) (time | (time >>> 32));
        hash = hash * 31 + Block.getIdFromBlock(block);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof BlockUpdateEntry))
            return false;
        BlockUpdateEntry entry = (BlockUpdateEntry) obj;
        if (priority != entry.priority)
            return false;
        if (time != entry.time)
            return false;
        if (block != entry.block)
            return false;
        return true;
    }

    @Override
    public int compareTo(BlockUpdateEntry o) {
        if (o == this) return 0;
        if (time < o.time)
            return -1;
        if (time > o.time)
            return 1;
        int diff = priority - o.priority;
        if (diff != 0)
            return diff;
        if (id < o.id) return -1;
        if (id > o.id) return 1;
        return 0; // Normally never should happens
    }
}
