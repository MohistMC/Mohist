/*
 * Minecraft Forge
 * Copyright (c) 2016.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.eventbus.api;

import net.minecraftforge.eventbus.ListenerList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EventListenerHelper
{
    private final static Map<Class<?>, ListenerList> listeners = new IdentityHashMap<>();
    private static ReadWriteLock lock = new ReentrantReadWriteLock(true);
    /**
     * Returns a {@link ListenerList} object that contains all listeners
     * that are registered to this event class.
     *
     * This supports abstract classes that cannot be instantiated.
     *
     * Note: this is much slower than the instance method {@link Event#getListenerList()}.
     * For performance when emitting events, always call that method instead.
     */
    public static ListenerList getListenerList(Class<?> eventClass)
    {
        return getListenerListInternal(eventClass, false);
    }

    static ListenerList getListenerListInternal(Class<?> eventClass, boolean fromInstanceCall)
    {
        final Lock readLock = lock.readLock();
        // to read the listener list, let's take the read lock
        readLock.lock();
        ListenerList listenerList = listeners.get(eventClass);
        readLock.unlock();
        // if there's no entry, we'll end up here
        if (listenerList == null) {
            // Let's pre-compute our new listener list value. This will possibly call parents' listener list
            // evaluations. as such, we need to make sure we don't hold a lock when we do this, otherwise
            // we could conflict with the class init global lock that is implicitly present
            listenerList = computeListenerList(eventClass, fromInstanceCall);
            // having computed a listener list, we'll grab the write lock.
            // We'll also take the read lock, so we're very clear we have _both_ locks here.
            final Lock writeLock = lock.writeLock();
            writeLock.lock();
            readLock.lock();
            // insert our computed value if no existing value is present
            listeners.putIfAbsent(eventClass, listenerList);
            // get whatever value got stored in the list
            listenerList = listeners.get(eventClass);
            // and unlock, and we're done
            readLock.unlock();
            writeLock.unlock();
        }
        return listenerList;
    }

    private static ListenerList computeListenerList(Class<?> eventClass, boolean fromInstanceCall)
    {
        if (eventClass == Event.class)
        {
            return new ListenerList();
        }

        if (fromInstanceCall || Modifier.isAbstract(eventClass.getModifiers()))
        {
            Class<?> superclass = eventClass.getSuperclass();
            ListenerList parentList = getListenerList(superclass);
            return new ListenerList(parentList);
        }

        try
        {
            Constructor<?> ctr = eventClass.getConstructor();
            ctr.setAccessible(true);
            Event event = (Event) ctr.newInstance();
            return event.getListenerList();
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            throw new RuntimeException("Error computing listener list for " + eventClass.getName(), e);
        }
    }

    private static void clearAll() {
        listeners.clear();
        lock = new ReentrantReadWriteLock(true);
    }
}
