package com.mohistmc;

public class MohistMC {
    public static final String NAME = "Mohist";

    public static String getVersion() {
        return (MohistMC.class.getPackage().getImplementationVersion() != null) ? MohistMC.class.getPackage().getImplementationVersion() : "unknown";
    }
}