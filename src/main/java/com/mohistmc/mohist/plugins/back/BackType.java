package com.mohistmc.mohist.plugins.back;

public enum BackType {
    TELEPORT,DEATH;

    public boolean isTeleport() {
        return this == TELEPORT;
    }
}
