package com.mohistmc.features.errhandler;

public interface i18n_ErrHandler {
  default String J2SE_RAMErr(){
    return "这些内存对于Mohist还是太小了。\nJ2SE分配到的内存：<J2SEMem>\n目前剩余内存：<RemainderMem>\n目前已用内存：<MemOfUsing>试试在服务器启动参数的后面在附加上-Xmx";
  }
}
