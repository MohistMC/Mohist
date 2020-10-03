package com.mohistmc;

public class Mohist {
    public static final String NAME = "Mohist";

    public static String getVersion() {
        return (Mohist.class.getPackage().getImplementationVersion() != null) ? Mohist.class.getPackage().getImplementationVersion() : "unknown";
    }
}