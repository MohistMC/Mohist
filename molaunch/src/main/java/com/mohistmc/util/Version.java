package com.mohistmc.util;


import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum Version {
    FORGE("41.0.45"), MCP("20220607.102129"), MOHIST("1.19-dev");

    public final String value;
}
