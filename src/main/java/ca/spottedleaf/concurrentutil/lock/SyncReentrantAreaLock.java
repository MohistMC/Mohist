package ca.spottedleaf.concurrentutil.lock;

import ca.spottedleaf.concurrentutil.collection.MultiThreadedQueue;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.util.concurrent.locks.LockSupport;

// not concurrent, unlike ReentrantAreaLock
// no incorrect lock usage detection (acquiring intersecting areas)
// this class is nothing more than a performance reference for ReentrantAreaLock
public final class SyncReentrantAreaLock {

    private final int coordinateShift;

    // aggressive load factor to reduce contention
    private final Long2ReferenceOpenHashMap<Node> nodes = new Long2ReferenceOpenHashMap<>(128, 0.2f);

    public SyncReentrantAreaLock(final int coordinateShift) {
        this.coordinateShift = coordinateShift;
    }

    private static long key(final int x, final int z) {
        return ((long)z << 32) | (x & 0xFFFFFFFFL);
    }

    public Node lock(final int x, final int z) {
        final Thread currThread = Thread.currentThread();
        final int shift = this.coordinateShift;
        final int sectionX = x >> shift;
        final int sectionZ = z >> shift;

        final LongArrayList areaAffected = new LongArrayList();

        final Node ret = new Node(this, areaAffected, currThread);

        final long coordinate = key(sectionX, sectionZ);

        for (long failures = 0L;;) {
            final Node park;

            synchronized (this) {
                // try to fast acquire area
                final Node prev = this.nodes.putIfAbsent(coordinate, ret);

                if (prev == null) {
                    areaAffected.add(coordinate);
                    return ret;
                } else if (prev.thread != currThread) {
                    park = prev;
                } else {
                    // only one node we would want to acquire, and it's owned by this thread already
                    return ret;
                }
            }

            ++failures;

            if (failures > 128L && park.add(currThread)) {
                LockSupport.park();
            } else {
                // high contention, spin wait
                if (failures < 128L) {
                    for (long i = 0; i < failures; ++i) {
                        Thread.onSpinWait();
                    }
                    failures = failures << 1;
                } else if (failures < 1_200L) {
                    LockSupport.parkNanos(1_000L);
                    failures = failures + 1L;
                } else { // scale 0.1ms (100us) per failure
                    Thread.yield();
                    LockSupport.parkNanos(100_000L * failures);
                    failures = failures + 1L;
                }
            }
        }
    }

    public Node lock(final int centerX, final int centerZ, final int radius) {
        return this.lock(centerX - radius, centerZ - radius, centerX + radius, centerZ + radius);
    }

    public Node lock(final int fromX, final int fromZ, final int toX, final int toZ) {
        if (fromX > toX || fromZ > toZ) {
            throw new IllegalArgumentException();
        }

        final Thread currThread = Thread.currentThread();
        final int shift = this.coordinateShift;
        final int fromSectionX = fromX >> shift;
        final int fromSectionZ = fromZ >> shift;
        final int toSectionX = toX >> shift;
        final int toSectionZ = toZ >> shift;

        final LongArrayList areaAffected = new LongArrayList();

        final Node ret = new Node(this, areaAffected, currThread);

        for (long failures = 0L;;) {
            Node park = null;
            boolean addedToArea = false;

            synchronized (this) {
                // try to fast acquire area
                for (int currZ = fromSectionZ; currZ <= toSectionZ; ++currZ) {
                    for (int currX = fromSectionX; currX <= toSectionX; ++currX) {
                        final long coordinate = key(currX, currZ);

                        final Node prev = this.nodes.putIfAbsent(coordinate, ret);

                        if (prev == null) {
                            addedToArea = true;
                            areaAffected.add(coordinate);
                            continue;
                        }

                        if (prev.thread != currThread) {
                            park = prev;
                            break;
                        }
                    }
                }

                if (park == null) {
                    return ret;
                }

                // failed, undo logic
                if (!areaAffected.isEmpty()) {
                    for (int i = 0, len = areaAffected.size(); i < len; ++i) {
                        final long key = areaAffected.getLong(i);

                        if (!this.nodes.remove(key, ret)) {
                            throw new IllegalStateException();
                        }
                    }
                }
            }

            if (addedToArea) {
                areaAffected.clear();
                // since we inserted, we need to drain waiters
                Thread unpark;
                while ((unpark = ret.pollOrBlockAdds()) != null) {
                    LockSupport.unpark(unpark);
                }
            }

            ++failures;

            if (failures > 128L && park.add(currThread)) {
                LockSupport.park();
            } else {
                // high contention, spin wait
                if (failures < 128L) {
                    for (long i = 0; i < failures; ++i) {
                        Thread.onSpinWait();
                    }
                    failures = failures << 1;
                } else if (failures < 1_200L) {
                    LockSupport.parkNanos(1_000L);
                    failures = failures + 1L;
                } else { // scale 0.1ms (100us) per failure
                    Thread.yield();
                    LockSupport.parkNanos(100_000L * failures);
                    failures = failures + 1L;
                }
            }

            if (addedToArea) {
                // try again, so we need to allow adds so that other threads can properly block on us
                ret.allowAdds();
            }
        }
    }

    public void unlock(final Node node) {
        if (node.lock != this) {
            throw new IllegalStateException("Unlock target lock mismatch");
        }

        final LongArrayList areaAffected = node.areaAffected;

        if (areaAffected.isEmpty()) {
            // here we are not in the node map, and so do not need to remove from the node map or unblock any waiters
            return;
        }

        // remove from node map; allowing other threads to lock
        synchronized (this) {
            for (int i = 0, len = areaAffected.size(); i < len; ++i) {
                final long coordinate = areaAffected.getLong(i);
                if (!this.nodes.remove(coordinate, node)) {
                    throw new IllegalStateException();
                }
            }
        }

        Thread unpark;
        while ((unpark = node.pollOrBlockAdds()) != null) {
            LockSupport.unpark(unpark);
        }
    }

    public static final class Node extends MultiThreadedQueue<Thread> {

        private final SyncReentrantAreaLock lock;
        private final LongArrayList areaAffected;
        private final Thread thread;

        private Node(final SyncReentrantAreaLock lock, final LongArrayList areaAffected, final Thread thread) {
            this.lock = lock;
            this.areaAffected = areaAffected;
            this.thread = thread;
        }
    }
}
