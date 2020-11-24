package me.jellysquid.phosphor.core.collections;

import java.util.ArrayDeque;
import java.util.Deque;

//Implement own queue with pooled segments to reduce allocation costs and reduce idle memory footprint
public class PooledLongQueue {
    private static final int CACHED_QUEUE_SEGMENTS_COUNT = 1 << 12; // 4096
    private static final int QUEUE_SEGMENT_SIZE = 1 << 10; // 1024

    private final Pool pool;

    private Segment cur, last;

    private int size = 0;

    // Stores whether or not the queue is empty. Updates to this field will be seen by all threads immediately. Writes
    // to volatile fields are generally quite a bit more expensive, so we avoid repeatedly setting this flag to true.
    private volatile boolean empty;

    public PooledLongQueue(Pool pool) {
        this.pool = pool;
    }

    /**
     * Not thread-safe! If you must know whether or not the queue is empty, please use {@link PooledLongQueue#isEmpty()}.
     *
     * @return The number of encoded values present in this queue
     */
    public int size() {
        return this.size;
    }

    /**
     * Thread-safe method to check whether or not this queue has work to do. Significantly cheaper than acquiring a lock.
     * @return True if the queue is empty, otherwise false
     */
    public boolean isEmpty() {
        return this.empty;
    }

    /**
     * Not thread-safe! Adds an encoded long value into this queue.
     * @param val The encoded value to add
     */
    public void add(final long val) {
        if (this.cur == null) {
            this.empty = false;
            this.cur = this.last = this.pool.acquire();
        }

        if (this.last.index == QUEUE_SEGMENT_SIZE) {
            Segment ret = this.last.next = this.last.pool.acquire();
            ret.longArray[ret.index++] = val;

            this.last = ret;
        } else {
            this.last.longArray[this.last.index++] = val;
        }

        ++this.size;
    }

    /**
     * Not thread safe! Creates an iterator over the values in this queue. Values will be returned in a FIFO fashion.
     * @return The iterator
     */
    public LongQueueIterator iterator() {
        return new LongQueueIterator(this.cur);
    }

    private void clear() {
        Segment segment = this.cur;

        while (segment != null) {
            Segment next = segment.next;
            segment.release();
            segment = next;
        }

        this.size = 0;
        this.cur = null;
        this.last = null;
        this.empty = true;
    }

    public class LongQueueIterator {
        private Segment cur;
        private long[] curArray;

        private int index, capacity;

        private LongQueueIterator(Segment cur) {
            this.cur = cur;

            if (this.cur != null) {
                this.curArray = cur.longArray;
                this.capacity = cur.index;
            }
        }

        public boolean hasNext() {
            return this.cur != null;
        }

        public long next() {
            final long ret = this.curArray[this.index++];

            if (this.index == this.capacity) {
                this.index = 0;

                this.cur = this.cur.next;

                if (this.cur != null) {
                    this.curArray = this.cur.longArray;
                    this.capacity = this.cur.index;
                }
            }

            return ret;
        }

        public void finish() {
            PooledLongQueue.this.clear();
        }
    }

    public static class Pool {
        private final Deque<Segment> segmentPool = new ArrayDeque<>();

        private Segment acquire() {
            if (this.segmentPool.isEmpty()) {
                return new Segment(this);
            }

            return this.segmentPool.pop();
        }

        private void release(Segment segment) {
            if (this.segmentPool.size() < CACHED_QUEUE_SEGMENTS_COUNT) {
                this.segmentPool.push(segment);
            }
        }
    }

    private static class Segment {
        private final long[] longArray = new long[QUEUE_SEGMENT_SIZE];
        private int index = 0;
        private Segment next;
        private final Pool pool;

        private Segment(Pool pool) {
            this.pool = pool;
        }

        private void release() {
            this.index = 0;
            this.next = null;

            this.pool.release(this);
        }
    }

}