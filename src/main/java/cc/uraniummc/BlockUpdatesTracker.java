package cc.uraniummc;

import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.block.Block;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public final class BlockUpdatesTracker implements Iterable<BlockUpdateEntry> {
    private long lastEntryId = Long.MIN_VALUE;
    private final WorldServer world;
    private final NavigableSet<BlockUpdateEntry> sortedTree = new TreeSet<BlockUpdateEntry>();
    private final TLongObjectMap<BlockUpdateEntry> trackerMap = new TLongObjectHashMap<BlockUpdateEntry>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public BlockUpdatesTracker(WorldServer world) {
        this.world = world;
    }

    public BlockUpdateEntry allocateEntry(int x, int y, int z, int priority, long time, Block block) {
        return allocateEntry(x, y, z, priority, time, block, true);
    }

    public BlockUpdateEntry allocateEntry(int x, int y, int z, int priority, long time, Block block,
            boolean allowChunkTracking) {
        lock.writeLock().lock();
        try {
            Chunk chunk = allowChunkTracking ? world.getChunkIfLoaded(x >> 4, z >> 4) : null;
            BlockUpdateEntry entry = new BlockUpdateEntry(x, y, z, priority, time, block, lastEntryId++);
            BlockUpdateEntry oldEntry = trackerMap.remove(entry.key);
            if (oldEntry != null) {
                sortedTree.remove(oldEntry);
                if (chunk != null)
                    chunk.blockUpdates.remove(oldEntry);
            }
            trackerMap.put(entry.key, entry);
            sortedTree.add(entry);
            if (chunk != null)
                chunk.blockUpdates.add(entry);
            return entry;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public BlockUpdateEntry removeEntry(int x, int y, int z) {
        return removeEntry(x, y, z, true);
    }

    public BlockUpdateEntry removeEntry(int x, int y, int z, boolean allowChunkTracking) {
        lock.writeLock().lock();
        try {
            Chunk chunk = allowChunkTracking ? world.getChunkIfLoaded(x >> 4, z >> 4) : null;
            BlockUpdateEntry entry = trackerMap.remove(new BlockCoords(x, y, z).key);
            if (entry != null) {
                sortedTree.remove(entry);
                if (chunk != null)
                    chunk.blockUpdates.remove(entry);
            }
            return entry;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public BlockUpdateEntry removeEntry(BlockUpdateEntry entry, boolean allowChunkTracking) {
        lock.writeLock().lock();
        try {
            Chunk chunk = allowChunkTracking ? world.getChunkIfLoaded(entry.x >> 4, entry.z >> 4) : null;
            trackerMap.remove(entry.key);
            if (entry != null) {
                sortedTree.remove(entry);
                if (chunk != null)
                    chunk.blockUpdates.remove(entry);
            }
            return entry;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public BlockUpdateEntry getEntry(int x, int y, int z) {
        lock.readLock().lock();
        try {
            return trackerMap.get(new BlockCoords(x, y, z).key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public BlockUpdateEntry peek() {
        lock.readLock().lock();
        try {
            return sortedTree.first();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return trackerMap.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return trackerMap.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Iterator<BlockUpdateEntry> iterator() {
        return new Iterator<BlockUpdateEntry>() {
            private final Iterator<BlockUpdateEntry> iterator = sortedTree.iterator();;
            private BlockUpdateEntry next;

            @Override
            public boolean hasNext() {
                next = null;
                return iterator.hasNext();
            }

            @Override
            public BlockUpdateEntry next() {
                return next = iterator.next();
            }

            @Override
            public void remove() {
                if (next == null)
                    next();
                if (next != null)
                    removeEntry(next, true);
            }
        };
    }

    private final VanillaWrapper wrapper = new VanillaWrapper();
    public final HashSet<NextTickListEntry> hashSet = new VanillaHashSetWrapper();
    public final TreeSet<NextTickListEntry> treeSet = new VanillaTreeSetWrapper();

    private final class VanillaWrapper {
        private Set<NextTickListEntry> add = new HashSet<NextTickListEntry>();
        private Set<NextTickListEntry> remove = new HashSet<NextTickListEntry>();

        public boolean add(NextTickListEntry e) {
            if (add.remove(e)) {
                allocateEntry(e.xCoord, e.yCoord, e.zCoord, e.priority, e.scheduledTime, e.func_151351_a());
                return true;
            }
            return add.add(e);
        }

        public boolean remove(NextTickListEntry e) {
            if (remove.remove(e)) {
                removeEntry(e.xCoord, e.yCoord, e.zCoord);
                return true;
            }
            return remove.add(e);
        }

        public boolean contains(NextTickListEntry e, boolean allowInterscan) {
            if (allowInterscan) {
                if (add.contains(e))
                    return true;
                if (remove.contains(e))
                    return false;
            }
            return getEntry(e.xCoord, e.yCoord, e.zCoord) != null;
        }
    }

    private final class VanillaHashSetWrapper extends HashSet<NextTickListEntry> {
        @Override
        public Iterator<NextTickListEntry> iterator() {
            return new Iterator<NextTickListEntry>() {
                private final TLongObjectIterator<BlockUpdateEntry> iterator = trackerMap.iterator();;

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public NextTickListEntry next() {
                    iterator.advance();
                    return iterator.value().asMCEntry();
                }

                @Override
                public void remove() {
                    removeEntry(iterator.value(), true);
                }
            };
        }

        @Override
        public boolean add(NextTickListEntry e) {
            return wrapper.add(e);
        }

        @Override
        public boolean remove(Object o) {
            return wrapper.remove((NextTickListEntry) o);
        }

        @Override
        public boolean contains(Object o) {
            return wrapper.contains((NextTickListEntry) o, true);
        }

        @Override
        public boolean isEmpty() {
            return BlockUpdatesTracker.this.sortedTree.isEmpty();
        }

        @Override
        public int size() {
            return BlockUpdatesTracker.this.sortedTree.size();
        }
    }

    private final class VanillaTreeSetWrapper extends TreeSet<NextTickListEntry> {
        @Override
        public Iterator<NextTickListEntry> iterator() {
            return new Iterator<NextTickListEntry>() {
                private final Iterator<BlockUpdateEntry> iterator = sortedTree.iterator();;
                private BlockUpdateEntry next;

                @Override
                public boolean hasNext() {
                    next = null;
                    return iterator.hasNext();
                }

                @Override
                public NextTickListEntry next() {
                    return (next = iterator.next()).asMCEntry();
                }

                @Override
                public void remove() {
                    if (next == null)
                        next();
                    if (next != null)
                        removeEntry(next, true);
                }
            };
        }

        @Override
        public boolean add(NextTickListEntry e) {
            return wrapper.add(e);
        }

        @Override
        public boolean remove(Object o) {
            return wrapper.remove((NextTickListEntry) o);
        }

        @Override
        public boolean contains(Object o) {
            return wrapper.contains((NextTickListEntry) o, false);
        }

        @Override
        public boolean isEmpty() {
            return BlockUpdatesTracker.this.sortedTree.isEmpty();
        }

        @Override
        public int size() {
            return BlockUpdatesTracker.this.sortedTree.size();
        }
    }
}
