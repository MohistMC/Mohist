package com.mohistmc.features.errhandler;

import java.io.IOException;

import com.mohistmc.util.i18n.i18n;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof OutOfMemoryError){
            RAMError();
        }else if(e instanceof IOException){
            IO();
        }
    }
    private void RAMError(){
        long J2SEMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long RemainderMem = Runtime.getRuntime().freeMemory();
        long UsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String msg = i18n.get("errhandler.j2se.ram").replace("<J2SEMem>", J2SEMem + " MB").replace("<RemainderMem>", RemainderMem + " MB").replace("<MemOfUsing>", UsedMem + " MB");
        System.out.println(msg);
    }
    private void IO(){
        System.out.println(i18n.get("errhandler.j2se.IOException"));
    }
}