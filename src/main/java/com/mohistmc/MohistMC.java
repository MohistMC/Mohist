package com.mohistmc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MohistMC {
    public static final String NAME = "Mohist";
    public static Logger LOGGER =  LogManager.getLogger();

    public static String getVersion() {
        return (MohistMC.class.getPackage().getImplementationVersion() != null) ? MohistMC.class.getPackage().getImplementationVersion() : "unknown";
    }
}