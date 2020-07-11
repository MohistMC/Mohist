package org.bukkit.plugin;

import com.destroystokyo.paper.event.executor.MethodHandleEventExecutor;
import com.destroystokyo.paper.event.executor.StaticMethodHandleEventExecutor;
import com.destroystokyo.paper.event.executor.asm.ASMEventExecutorGenerator;
import com.destroystokyo.paper.event.executor.asm.ClassDefiner;
import com.google.common.base.Preconditions;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Interface which defines the class for event call backs to plugins
 */
public interface EventExecutor {
    // Paper start
    ConcurrentMap<Method, Class<? extends EventExecutor>> eventExecutorMap = new ConcurrentHashMap<Method, Class<? extends EventExecutor>>() {
        @Override
        public Class<? extends EventExecutor> computeIfAbsent(Method key, Function<? super Method, ? extends Class<? extends EventExecutor>> mappingFunction) {
            Class<? extends EventExecutor> executorClass = get(key);
            if (executorClass != null)
                return executorClass;

            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (key) {
                executorClass = get(key);
                if (executorClass != null)
                    return executorClass;

                return super.computeIfAbsent(key, mappingFunction);
            }
        }
    };

    static EventExecutor create(Method m, Class<? extends Event> eventClass) {
        Preconditions.checkNotNull(m, "Null method");
        Preconditions.checkArgument(m.getParameterCount() != 0, "Incorrect number of arguments %s", m.getParameterCount());
        Preconditions.checkArgument(m.getParameterTypes()[0] == eventClass, "First parameter %s doesn't match event class %s", m.getParameterTypes()[0], eventClass);
        ClassDefiner definer = ClassDefiner.getInstance();
        if (Modifier.isStatic(m.getModifiers())) {
            return new StaticMethodHandleEventExecutor(eventClass, m);
        } else if (definer.isBypassAccessChecks() || Modifier.isPublic(m.getDeclaringClass().getModifiers()) && Modifier.isPublic(m.getModifiers())) {
            // get the existing generated EventExecutor class for the Method or generate one
            Class<? extends EventExecutor> executorClass = eventExecutorMap.computeIfAbsent(m, (__) -> {
                String name = ASMEventExecutorGenerator.generateName();
                byte[] classData = ASMEventExecutorGenerator.generateEventExecutor(m, name);
                return definer.defineClass(m.getDeclaringClass().getClassLoader(), name, classData).asSubclass(EventExecutor.class);
            });

            try {
                EventExecutor asmExecutor = executorClass.newInstance();
                // Define a wrapper to conform to bukkit stupidity (passing in events that don't match and wrapper exception)
                return (listener, event) -> {
                    if (!eventClass.isInstance(event)) return;
                    try {
                        asmExecutor.execute(listener, event);
                    } catch (Exception e) {
                        throw new EventException(e);
                    }
                };
            } catch (InstantiationException | IllegalAccessException e) {
                throw new AssertionError("Unable to initialize generated event executor", e);
            }
        } else {
            return new MethodHandleEventExecutor(eventClass, m);
        }
    }

    void execute(Listener listener, Event event) throws EventException;
    // Paper end
}
