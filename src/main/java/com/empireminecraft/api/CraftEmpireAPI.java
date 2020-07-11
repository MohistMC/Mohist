package com.empireminecraft.api;

public final class CraftEmpireAPI extends API {

    public static final API instance = new CraftEmpireAPI();

    static {
        entity = new CraftEAPI_Entity();
        misc = new CraftEAPI_Misc();
    }

    private CraftEmpireAPI() {
    }
}
