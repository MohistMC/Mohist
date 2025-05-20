package com.mohistmc.features.errhandler;

public interface i18n {
  default String J2SE_RAMErr(){
    return "这些内存对于Mohist还是太小了。";
  }
}
