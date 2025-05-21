package com.mohistmc.features.errhandler;

import com.mohistmc.util.i18n.i18n;

public class ExceptionHandler {
    public void RAMError(){
        long J2SEMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long RemainderMem = Runtime.getRuntime().freeMemory();
        long UsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        String msg = i18n.get("errhandler.j2se.ram").replace("<J2SEMem>", J2SEMem + " MB").replace("<RemainderMem>", RemainderMem + " MB").replace("<MemOfUsing>", UsedMem + " MB");
        System.out.println(msg);
    }
    public void IO(){
        System.out.println(i18n.get("errhandler.j2se.IOException"));
    }
}