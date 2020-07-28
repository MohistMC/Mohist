package red.mohist.reampper;

import java.util.LinkedList;

/**
 *
 * @author pyz
 * @date 2019/7/4 1:30 AM
 */
public class ClassLoaderContext {
    private static final ThreadLocal<LinkedList<ClassLoader>> THREAD_LOCAL = new ThreadLocal<>();

    public static void put(ClassLoader classLoader) {
        LinkedList<ClassLoader> stack = THREAD_LOCAL.get();
        if (stack == null) {
            stack = new LinkedList<>();
            THREAD_LOCAL.set(stack);
        }
        stack.push(classLoader);
    }

    public static ClassLoader remove(){
        LinkedList<ClassLoader> stack = THREAD_LOCAL.get();
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.remove();
    }

    public static ClassLoader pop() {
        LinkedList<ClassLoader> stack = THREAD_LOCAL.get();
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.pop();
    }

    public static ClassLoader peek() {
        LinkedList<ClassLoader> stack = THREAD_LOCAL.get();
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }
}
