package com.mohistmc.util;

import java.io.IOException;
import java.net.BindException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mohistmc.MohistMC;
import com.mohistmc.util.i18n.i18n;
/**
 * @author deepsleep-v3, Mgazul
 * This is a ExceptionHandler, i18n resource created by deepsleep-v3, The rest were created by Mgazul
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Logger logger = Logger.getLogger(MohistMC.class.getName());
    private Map<Class<? extends Throwable>, Consumer<Throwable>> exceptionHandlers = new HashMap<>();
    public ExceptionHandler(){
        exceptionHandlers.put(OutOfMemoryError.class, this::handleOutOfMemoryError);
        exceptionHandlers.put(IOException.class, this::handleIOException);
        exceptionHandlers.put(ClassNotFoundException.class, this::handleClassNotFoundException);
        exceptionHandlers.put(NoClassDefFoundError.class, this::handleNoClassDefFoundError);
        exceptionHandlers.put(BindException.class, this::handleBindException);
        exceptionHandlers.put(NullPointerException.class, this::handleNullPtrException);
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Consumer<Throwable> handler = exceptionHandlers.get(e.getClass());
        if (handler != null) {
            handler.accept(e);
        } else {
            System.out.println("Uncaught Exception: " + e.getClass().getName() + " - " + e.getMessage());
            printJarOrClassInfo(e);
            logger.log(Level.SEVERE, String.format("%s: %s", e.getClass().getName(), e.getMessage()));
        }
    }

    private void printJarOrClassInfo(Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (!isInternalJavaClass(element.getClassName())) {
                System.out.println(i18n.get("errhandler.classinfo.problemin")
                        .replace("<ProblemIn>", element.getClassName())
                        .replace("<File>", element.getFileName())
                        .replace("<Line>", String.valueOf(element.getLineNumber())));
                try {
                    Class<?> clazz = Class.forName(element.getClassName());
                    String location = clazz.getProtectionDomain().getCodeSource().getLocation().toString();
                    System.out.println(i18n.get("errhandler.classinfo.location").replace("<Location>", location));
                } catch (ClassNotFoundException ex) {
                    System.out.println(i18n.get("errhandler.classinfo.classnotfound"));
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

    private void handleOutOfMemoryError(Throwable ex){
        long J2SEMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long RemainderMem = Runtime.getRuntime().freeMemory();
        long UsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String msg = i18n.get("errhandler.j2se.ram").replace("<J2SEMem>", J2SEMem + " MB").replace("<RemainderMem>", RemainderMem + " MB").replace("<MemOfUsing>", UsedMem + " MB");
        System.out.println(msg);
    }
    private void handleIOException(Throwable ex){
        System.out.println(i18n.get("errhandler.j2se.IOException"));
        printJarOrClassInfo(ex);
    }

    private void handleClassNotFoundException(Throwable ex) {
        System.out.println(i18n.get("errhandler.j2se.ClassNotFound").replace("<MoreMessage>", ex.getMessage()));
        printJarOrClassInfo(ex);
    }

    private void handleNoClassDefFoundError(Throwable ex){
        System.out.println(i18n.get("errhandler.j2se.NotClassDef").replace("<MoreMessage>", ex.getMessage()));
    }

    private void handleBindException(Throwable ex) {
        System.out.println(i18n.get("errhandler.j2se.network.BindException").replace("<MoreMessage>", ex.getMessage()));
    }

    private void handleNullPtrException(Throwable ex){
        System.out.println(i18n.get("errhandler.j2se.NullPtrException"));
        System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
    }

    private void handleSQLException(Throwable ex){
        SQLException sqlException = (SQLException) ex;
        System.out.println(i18n.get("errhandler.sql.SQLException")
                .replace("<ReturnCode>", String.valueOf(sqlException.getErrorCode()))
                .replace("<ReturnValue>", sqlException.getMessage()));
        sqlException.printStackTrace();
        printJarOrClassInfo(ex);
    }
}