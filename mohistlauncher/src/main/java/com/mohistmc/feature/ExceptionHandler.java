package com.mohistmc.feature;

import java.net.BindException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Map<Class<? extends Throwable>, Consumer<Throwable>> exceptionHandlers = new HashMap<>();

    public ExceptionHandler() {
        exceptionHandlers.put(OutOfMemoryError.class, this::handleOutOfMemoryError);
        exceptionHandlers.put(ClassNotFoundException.class, this::handleClassNotFoundException);
        exceptionHandlers.put(NoClassDefFoundError.class, this::handleNoClassDefFoundError);
        exceptionHandlers.put(BindException.class, this::handleBindException);
        exceptionHandlers.put(NullPointerException.class, this::handleNullPointerException);
        exceptionHandlers.put(SQLException.class, this::handleSQLException);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Consumer<Throwable> handler = exceptionHandlers.get(e.getClass());
        if (handler != null) {
            handler.accept(e);
        } else {
            System.out.println("未处理的异常: " + e.getClass().getName() + " - " + e.getMessage());
            printJarOrClassInfo(e);
            e.printStackTrace();
        }
    }

    private void printJarOrClassInfo(Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (!isInternalJavaClass(element.getClassName())) {
                System.out.println("问题发生在: " + element.getClassName() + " (文件: " + element.getFileName() + " 行: " + element.getLineNumber() + ")");
                try {
                    Class<?> clazz = Class.forName(element.getClassName());
                    String location = clazz.getProtectionDomain().getCodeSource().getLocation().toString();
                    System.out.println("相关的Jar/类路径: " + location);
                } catch (ClassNotFoundException ex) {
                    System.out.println("无法找到相关的类: " + element.getClassName());
                }
                break;
            }
        }
    }
    private boolean isInternalJavaClass(String className) {
        return className.startsWith("java.") || className.startsWith("javax.") ||
                className.startsWith("org.w3c.dom.") || className.startsWith("org.xml.") ||
                className.startsWith("com.sun.") || className.startsWith("sun.") ||
                className.startsWith("javafx.");
    }


    private void handleOutOfMemoryError(Throwable e) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;

        System.out.println("你给Minecraft服务器分配的内存不够。");
        System.out.println("当前已用内存: " + usedMemory + " MB");
        System.out.println("当前总分配内存: " + totalMemory + " MB");
        System.out.println("JVM最大可用内存: " + maxMemory + " MB");
        printJarOrClassInfo(e);
    }

    private void handleClassNotFoundException(Throwable e) {
        System.out.println("未找到对应的Java类: " + e.getMessage());
        System.out.println("请尝试查找对应的类是否是客户端独有，例如包含\"GUI\"等字样，如果确信是Mod或插件问题，请联系对应开发者。");
        printJarOrClassInfo(e);
    }

    private void handleNoClassDefFoundError(Throwable e) {
        System.out.println("运行时未找到类: " + e.getMessage());
        System.out.println("这可能是因为类在编译时存在，但在运行时由于依赖问题、类路径不完整等原因导致找不到。");
        printJarOrClassInfo(e);
    }

    private void handleBindException(Throwable e) {
        BindException be = (BindException) e;
        System.out.println("无法将服务器绑定到: " + e.getMessage());
        System.out.println("这可能是因为端口已经被占用，或者没有足够的权限绑定到指定的端口。");
        System.out.println("请检查是否有其他程序占用了相同的端口，或者尝试使用不同的端口启动服务器。");
    }

    private void handleNullPointerException(Throwable e) {
        System.out.println("你遇到了一个NullPointerException，这个错误可能与未正确初始化的对象有关。");
        System.out.println("以下是完整的异常信息:");
        e.printStackTrace();
        printJarOrClassInfo(e);
    }

    private void handleSQLException(Throwable e) {
        SQLException sqlException = (SQLException) e;
        System.out.println("看起来与数据库连接时出现了问题。");
        System.out.println("SQL状态码：" + sqlException.getErrorCode());
        System.out.println("SQL返回：" + sqlException.getMessage());
        sqlException.printStackTrace();
        printJarOrClassInfo(e);
    }
}
