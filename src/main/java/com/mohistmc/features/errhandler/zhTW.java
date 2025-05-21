package com.mohistmc.features.errhandler;

public class zhTW implements i18n_ErrHandler{
    @Override
    public String J2SE_RAMErr(){
      return "這些記憶體對於Mohist還是太小了。\nJ2SE分配到的記憶體：<J2SEMem>\n目前剩餘記憶體：<RemainderMem>\n目前已用記憶體：<MemOfUsing>試試在伺服器啟動引數的後面在附加上-Xmx？";
    }
}
