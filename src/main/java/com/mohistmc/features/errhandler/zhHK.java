package com.mohistmc.features.errhandler;

public class zhHK implements i18n_ErrHandler{
  public String J2SE_RAMErr(){
    return "這些內存對於Mohist還是太小了。\nJ2SE分配到的內存：<J2SEMem>\n目前剩餘內存：<RemainderMem>\n目前已用內存：<MemOfUsing>試試在服務器啓動參數的後面在附加上-Xmx？";
  }
}
