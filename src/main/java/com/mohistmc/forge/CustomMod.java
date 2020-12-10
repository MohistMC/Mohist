package com.mohistmc.forge;

import io.netty.util.internal.ConcurrentSet;
import java.io.File;
import java.io.InputStream;
import java.util.Set;

public interface CustomMod {

    static Set<InputStream> is = new ConcurrentSet<>();

    File jarFile();
}
