package com.mohistmc.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/31 13:52:08
 */
public class MavenVersionAdapterFix {

    private static final Logger LOGGER = LogManager.getLogger();
    private MavenVersionAdapterFix() {}

    public static VersionRange createFromVersionSpec(final String spec) {
        try {
            return VersionRange.createFromVersionSpec(spec.replace(",47.1.3", ",47.1.99"));
        } catch (InvalidVersionSpecificationException e) {
            LOGGER.fatal("Failed to parse version spec {}", spec, e);
            throw new RuntimeException("Failed to parse spec", e);
        }
    }
}
