package ca.spottedleaf.concurrentutil.lock;

import ca.spottedleaf.concurrentutil.collection.MultiThreadedQueue;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

public final class ReentrantAreaLock {

    public final int coordinateShift;

    // aggressive load factor to reduce contention
    private final ConcurrentHashMap<Coordinate, Node> nodes = new ConcurrentHashMap<>(128, 0.2f);

    public ReentrantAreaLock(final int coordinateShift) {
        this.coordinateShift = coordinateShift;
    }

    public boolean isHeldByCurrentThread(final int x, final int z) {
        final Thread currThread = Thread.currentThread();
        final int shift = this.coordinateShift;
        final int sectionX = x >> shift;
        final int sectionZ = z >> shift;

        final Coordinate coordinate = new Coordinate(Coordinate.key(sectionX, sectionZ));
        final Node node = this.nodes.get(coordinate);

        return node != null && node.thread == currThread;
    }

    public boolean isHeldByCurrentThread(final int centerX, final int centerZ, final int radius) {
        return this.isHeldByCurrentThread(centerX - radius, centerZ - radius, centerX + radius, centerZ + radius);
    }

    public boolean isHeldByCurrentThread(final int fromX, final int fromZ, final int toX, final int toZ) {
        if (fromX > toX || fromZ > toZ) {
            throw new IllegalArgumentException();
        }

        final Thread currThread = Thread.currentThread();
        final int shift = this.coordinateShift;
        final int fromSectionX = fromX >> shift;
        final int fromSectionZ = fromZ >> shift;
        final int toSectionX = toX >> shift;
        final int toSectionZ = toZ >> shift;

        for (int currZ = fromSectionZ; currZ <= toSectionZ; ++currZ) {
            for (int currX = fromSectionX; currX <= toSectionX; ++currX) {
                final Coordinate coordinate = new Coordinate(Coordinate.key(currX, currZ));

                final Node node = this.nodes.get(coordinate);

                if (node == null || node.thread != currThread) {
                    return false;
                }
            }
        }

        return true;
    }

    public Node tryLock(final int x, final int z) {
        return this.tryLock(x, z, x, z);
    }

    public Node tryLock(final int centerX, final int centerZ, final int radius) {
        return this.tryLock(centerX - radius, centerZ - radius, centerX + radius, centerZ + radius);
    }

    public Node tryLock(final int fromX, final int fromZ, final int toX, final int toZ) {
        if (fromX > toX || fromZ > toZ) {
            throw new IllegalArgumentException();
        }

        final Thread currThread = Thread.currentThread();
        final int shift = this.coordinateShift;
        final int fromSectionX = fromX >> shift;
        final int fromSectionZ = fromZ >> shift;
        final int toSectionX = toX >> shift;
        final int toSectionZ = toZ >> shift;

        final List<Coordinate> areaAffected = new ArrayList<>();

        final Node ret = new Node(this, areaAffected, currThread);

        boolean failed = false;

        // try to fast acquire area
        for (int currZ = fromSectionZ; currZ <= toSectionZ; ++currZ) {
            for (int currX = fromSectionX; currX <= toSectionX; ++currX) {
                final Coordinate coordinate = new Coordinate(Coordinate.key(currX, currZ));

                final Node prev = this.nodes.putIfAbsent(coordinate, ret);

                if (prev == null) {
                    areaAffected.add(coordinate);
                    continue;
                }

                if (prev.thread != currThread) {
                    failed = true;
                    break;
                }
            }
        }

        if (!failed) {
            return ret;
        }

        // failed, undo logic
        if (!areaAffected.isEmpty()) {
            for (int i = 0, len = areaAffected.size(); i < len; ++i) {
                final Coordinate key = areaAffected.get(i);

                if (this.nodes.remove(key) != ret) {
                    throw new IllegalStateException();
                }
            }

            areaAffected.clear();

            // since we inserted, we need to drain waiters
            Thread unpark;
            while ((unpark = ret.pollOrBlockAdds()) != null) {
                LockSupport.unpark(unpark);
            }
        }

        return null;
    }

    public Node lock(final int x, final int z) {
        final Thread currThread = Thread.currentThread();
        final int shift = this.coordinateShift;
        final int sectionX = x >> shift;
        final int sectionZ = z >> shift;

        final List<Coordinate> areaAffected = new ArrayList<>(1);

        final Node ret = new Node(this, areaAffected, currThread);
        final Coordinate coordinate = new Coordinate(Coordinate.key(sectionX, sectionZ));

        for (long failures = 0L;;) {
            final Node park;

            // try to fast acquire area
            {
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

        if (((fromSectionX ^ toSectionX) | (fromSectionZ ^ toSectionZ)) == 0) {
            return this.lock(fromX, fromZ);
        }

        final List<Coordinate> areaAffected = new ArrayList<>();

        final Node ret = new Node(this, areaAffected, currThread);

        for (long failures = 0L;;) {
            Node park = null;
            boolean addedToArea = false;
            boolean alreadyOwned = false;
            boolean allOwned = true;

            // try to fast acquire area
            for (int currZ = fromSectionZ; currZ <= toSectionZ; ++currZ) {
                for (int currX = fromSectionX; currX <= toSectionX; ++currX) {
                    final Coordinate coordinate = new Coordinate(Coordinate.key(currX, currZ));

                    final Node prev = this.nodes.putIfAbsent(coordinate, ret);

                    if (prev == null) {
                        addedToArea = true;
                        allOwned = false;
                        areaAffected.add(coordinate);
                        continue;
                    }

                    if (prev.thread != currThread) {
                        park = prev;
                        alreadyOwned = true;
                        break;
                    }
                }
            }

            if (park == null) {
                if (alreadyOwned && !allOwned) {
                    throw new IllegalStateException("Improper lock usage: Should never acquire intersecting areas");
                }
                return ret;
            }

            // failed, undo logic
            if (addedToArea) {
                for (int i = 0, len = areaAffected.size(); i < len; ++i) {
                    final Coordinate key = areaAffected.get(i);

                    if (this.nodes.remove(key) != ret) {
                        throw new IllegalStateException();
                    }
                }

                areaAffected.clear();

                // since we inserted, we need to drain waiters
                Thread unpark;
                while ((unpark = ret.pollOrBlockAdds()) != null) {
                    LockSupport.unpark(unpark);
                }
            }

            ++failures;

            if (failures > 128L && park.add(currThread)) {
                LockSupport.park(park);
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

        final List<Coordinate> areaAffected = node.areaAffected;

        if (areaAffected.isEmpty()) {
            // here we are not in the node map, and so do not need to remove from the node map or unblock any waiters
            return;
        }

        // remove from node map; allowing other threads to lock
        for (int i = 0, len = areaAffected.size(); i < len; ++i) {
            final Coordinate coordinate = areaAffected.get(i);
            if (this.nodes.remove(coordinate) != node) {
                throw new IllegalStateException();
            }
        }

        Thread unpark;
        while ((unpark = node.pollOrBlockAdds()) != null) {
            LockSupport.unpark(unpark);
        }
    }

    public static final class Node extends MultiThreadedQueue<Thread> {

        private final ReentrantAreaLock lock;
        private final List<Coordinate> areaAffected;
        private final Thread thread;
        //private final Throwable WHO_CREATED_MY_ASS = new Throwable();

        private Node(final ReentrantAreaLock lock, final List<Coordinate> areaAffected, final Thread thread) {
            this.lock = lock;
            this.areaAffected = areaAffected;
            this.thread = thread;
        }

        @Override
        public String toString() {
            return "Node{" +
                "areaAffected=" + this.areaAffected +
                ", thread=" + this.thread +
                '}';
        }
    }

    private static final class Coordinate implements Comparable<Coordinate> {

        public final long key;

        public Coordinate(final long key) {
            this.key = key;
        }

        public Coordinate(final int x, final int z) {
            this.key = key(x, z);
        }

        public static long key(final int x, final int z) {
            return ((long)z << 32) | (x & 0xFFFFFFFFL);
        }

        public static int x(final long key) {
            return (int)key;
        }

        public static int z(final long key) {
            return (int)(key >>> 32);
        }

        @Override
        public int hashCode() {
            return (int)HashCommon.mix(this.key);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Coordinate other)) {
                return false;
            }

            return this.key == other.key;
        }

        // This class is intended for HashMap/ConcurrentHashMap usage, which do treeify bin nodes if the chain
        // is too large. So we should implement compareTo to help.
        @Override
        public int compareTo(final Coordinate other) {
            return Long.compare(this.key, other.key);
        }

        @Override
        public String toString() {
            return "[" + x(this.key) + "," + z(this.key) + "]";
        }
    }
}
