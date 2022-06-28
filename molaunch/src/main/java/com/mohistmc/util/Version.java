package com.mohistmc.util;


import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum Version {
    FORGE("41.0.61"), MCP("20220607.102129"), MOHIST("dev"), MINECRAFT("1.19");

    public final String value;
}
