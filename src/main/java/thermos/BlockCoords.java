package thermos;

public class BlockCoords {
    public final int x, y, z;
    public final long key;
    private final int hash;

    public BlockCoords(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        key = ((long) y << 56) | (((long) z & 0xFFFFFFF) << 28) | (x & 0xFFFFFFF);
        hash = (int) (key ^ (key >>> 32));
    }

    public BlockCoords(BlockCoords coords) {
        this.x = coords.x;
        this.y = coords.y;
        this.z = coords.z;
        this.key = coords.key;
        this.hash = coords.hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof BlockCoords))
            return false;
        BlockCoords coords = (BlockCoords) obj;
        return x == coords.x && y == coords.y && z == coords.z;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
