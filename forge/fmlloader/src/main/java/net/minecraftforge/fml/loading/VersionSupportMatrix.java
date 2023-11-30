/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading;

import net.minecraftforge.forgespi.language.MavenVersionAdapter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;

public class VersionSupportMatrix {
    private static final HashMap<String, List<ArtifactVersion>> overrideVersions = new HashMap<>();
    static {
        if (FMLLoader.versionInfo().mcVersion().equals("1.20.1")) {
            add("mod.forge",              "47.1.79");
        }
    }
    private static void add(String key, String value) {
        overrideVersions.computeIfAbsent(key, k -> new ArrayList<>()).add(new DefaultArtifactVersion(value));
    }
    public static <T> boolean testVersionSupportMatrix(VersionRange declaredRange, String lookupId, String type, BiPredicate<String, VersionRange> standardLookup) {
        if (standardLookup.test(lookupId, declaredRange)) return true;
        List<ArtifactVersion> custom = overrideVersions.get(type +"." +lookupId);
        return custom == null ? false  : custom.stream().anyMatch(declaredRange::containsVersion);
    }
}
