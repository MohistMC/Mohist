package red.mohist.gradle.dev;

import net.minecraftforge.gradle.common.Constants;

public class DevConstants {
    static final String INSTALLER_URL = "http://files.minecraftforge.net/maven/net/minecraftforge/installer/{INSTALLER_VERSION}/installer-{INSTALLER_VERSION}-shrunk.jar";
    static final String LAUNCH4J_URL = "http://files.minecraftforge.net/launch4j/launch4j-3.0.0-" + Constants.OPERATING_SYSTEM + "-" + Constants.SYSTEM_ARCH + ".zip";
    static final String DEOBF_DATA = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_srg/{MC_VERSION}/deobfuscation_data-{MC_VERSION}.lzma";
    // other generated stuff
    static final String INSTALLER_BASE = "{BUILD_DIR}/tmp/installer_base.{INSTALLER_VERSION}.jar";
    static final String INSTALL_PROFILE = "{BUILD_DIR}/tmp/install_profile.json";
    static final String REOBF_TMP = "{BUILD_DIR}/tmp/recomp_obfed.jar";
    static final String MCP_2_SRG_SRG = "{BUILD_DIR}/tmp/mcp2srg.srg";
    static final String MCP_2_NOTCH_SRG = "{BUILD_DIR}/tmp/mcp2notch.srg";
    static final String SRG_2_MCP_SRG = "{BUILD_DIR}/tmp/srg2mcp.srg";
    static final String NOTCH_2_MCP_SRG = "{BUILD_DIR}/tmp/notch2mcp.srg";
    static final String NOTCH_2_SRG_SRG = "{BUILD_DIR}/tmp/notch2srg.srg";
    static final String SRG_EXC = "{BUILD_DIR}/tmp/srg.exc";
    static final String MCP_EXC = "{BUILD_DIR}/tmp/mcp.exc";
    static final String JAVADOC_TMP = "{BUILD_DIR}/tmp/javadoc";
    static final String BINPATCH_TMP = "{BUILD_DIR}/tmp/bin_patches.jar";
    static final String LAUNCH4J_DIR = "{BUILD_DIR}/launch4j_exec";
    static final String VERSION_JSON = "{BUILD_DIR}/tmp/version.json";
    static final String USERDEV_RANGEMAP = "{BUILD_DIR}/tmp/user_dev_range.txt";
    static final String EXC_MODIFIERS_DIRTY = "{BUILD_DIR}/tmp/exc_modifiers_dirty.txt";
    static final String EXC_MODIFIERS_CLEAN = "{BUILD_DIR}/tmp/exc_modifiers_clean.txt";
    // mappings
    static final String METHODS_CSV = "{MCP_DATA_DIR}/methods.csv";
    static final String FIELDS_CSV = "{MCP_DATA_DIR}/fields.csv";
    static final String PARAMS_CSV = "{MCP_DATA_DIR}/params.csv";
    static final String PACK_CSV = "{FML_CONF_DIR}/packages.csv";
    static final String JOINED_SRG = "{FML_CONF_DIR}/joined.srg";
    static final String JOINED_EXC = "{FML_CONF_DIR}/joined.exc";
    static final String ASTYLE_CFG = "{FML_CONF_DIR}/astyle.cfg";
    static final String EXC_JSON = "{FML_CONF_DIR}/exceptor.json";
    static final String MCP_PATCH = "{FML_CONF_DIR}/patches/minecraft_ff.patch";
    static final String MCP_PATCH_DIR = "{FML_CONF_DIR}/patches/minecraft_ff";
    static final String MERGE_CFG = "{FML_DIR}/mcp_merge.cfg";
    // jars.
    static final String JAR_SRG_FML = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_srg/{MC_VERSION}/minecraft_srg_fml-{MC_VERSION}.jar";
    static final String JAR_SRG_FORGE = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_srg/{MC_VERSION}/minecraft_srg_forge-{MC_VERSION}.jar";
    static final String JAR_SRG_CDN = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_srg/{MC_VERSION}/minecraft_srg_cauldron-{MC_VERSION}.jar";
    static final String JAR_SRG_EDU = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_srg/{MC_VERSION}/minecraft_srg_edu-{MC_VERSION}.jar";
    static final String ZIP_DECOMP_FML = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_decomp/{MC_VERSION}/minecraft_decomp_fml-{MC_VERSION}.zip";
    static final String ZIP_DECOMP_FORGE = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_decomp/{MC_VERSION}/minecraft_decomp_forge-{MC_VERSION}.zip";
    static final String ZIP_DECOMP_CDN = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_decomp/{MC_VERSION}/minecraft_decomp_cauldron-{MC_VERSION}.zip";
    static final String ZIP_DECOMP_EDU = "{CACHE_DIR}/minecraft/net/minecraft/minecraft_decomp/{MC_VERSION}/minecraft_decomp_edu-{MC_VERSION}.zip";
    static final String LAUNCH4J = "{CACHE_DIR}/minecraft/launch4j.zip";
    // fml intermediate jars
    static final String ZIP_PATCHED_FML = "{BUILD_DIR}/fmlTmp/minecraft_patched.zip";
    // forge intermediate jars
    static final String ZIP_FMLED_FORGE = "{BUILD_DIR}/forgeTmp/minecraft_fmlpatched.zip";
    static final String ZIP_PATCHED_FORGE = "{BUILD_DIR}/forgeTmp/minecraft_patches.zip";
    static final String ZIP_RENAMED_FORGE = "{BUILD_DIR}/forgeTmp/minecraft_renamed.zip";
    //Cauldron  intermediate jars
    static final String ZIP_FORGED_CDN = "{BUILD_DIR}/cauldronTmp/minecraft_fmlpatched.zip";
    static final String ZIP_PATCHED_CDN = "{BUILD_DIR}/cauldronTmp/minecraft_patched.zip";
    static final String ZIP_RENAMED_CDN = "{BUILD_DIR}/cauldronTmp/minecraft_renamed.zip";
    //MC EDU  intermediate jars
    static final String ZIP_FORGED_EDU = "{BUILD_DIR}/eduTmp/minecraft_fmlpatched.zip";
    static final String ZIP_PATCHED_EDU = "{BUILD_DIR}/eduTmp/minecraft_patched.zip";
    static final String ZIP_RENAMED_EDU = "{BUILD_DIR}/eduTmp/minecraft_renamed.zip";
    // other stuff
    static final String CHANGELOG = "{BUILD_DIR}/distributions/{PROJECT}-{MC_VERSION_SAFE}-{VERSION}-changelog.txt";
    static final String USERDEV_SRG_SRC = "{BUILD_DIR}/tmp/user_dev_srg_src.zip";
    // necesary for patch generation
    static final String PATCH_CLEAN = "{BUILD_DIR}/tmp/clean-path-base.zip";
    static final String PATCH_DIRTY = "{BUILD_DIR}/tmp/dirty-patch-base.zip";
    static final String REMAPPED_CLEAN = "{BUILD_DIR}/tmp/clean.jar";
    static final String REMAPPED_DIRTY = "{BUILD_DIR}/tmp/dirty.jar";
    // jsons
    static final String JSON_DEV = "{FML_DIR}/jsons/{MC_VERSION}-dev.json";
    static final String JSON_REL = "{FML_DIR}/jsons/{MC_VERSION}-rel.json";
    static final String JSON_BASE = "{FML_DIR}/jsons/{MC_VERSION}.json";
    static final String EXTRA_JSON_DEV = "jsons/{MC_VERSION}-dev.json";
    static final String EXTRA_JSON_REL = "jsons/{MC_VERSION}-rel.json";
    static final String EXTRA_JSON_BASE = "jsons/{MC_VERSION}.json";
    // eclipse folders      More stuff only for the Dev plugins
    static final String WORKSPACE_ZIP = "eclipse-workspace-dev.zip";
    static final String WORKSPACE = "eclipse";
    static final String ECLIPSE_CLEAN = WORKSPACE + "/Clean";
    static final String ECLIPSE_CLEAN_SRC = ECLIPSE_CLEAN + "/src/main/java";
    static final String ECLIPSE_CLEAN_START = ECLIPSE_CLEAN + "/src/main/start";
    static final String ECLIPSE_CLEAN_RES = ECLIPSE_CLEAN + "/src/main/resources";
    static final String ECLIPSE_FML = WORKSPACE + "/FML";
    static final String ECLIPSE_FML_SRC = ECLIPSE_FML + "/src/main/java";
    static final String ECLIPSE_FML_START = ECLIPSE_FML + "/src/main/start";
    static final String ECLIPSE_FML_RES = ECLIPSE_FML + "/src/main/resources";
    static final String ECLIPSE_FORGE = WORKSPACE + "/Forge";
    static final String ECLIPSE_FORGE_SRC = ECLIPSE_FORGE + "/src/main/java";
    static final String ECLIPSE_FORGE_START = ECLIPSE_FORGE + "/src/main/start";
    static final String ECLIPSE_FORGE_RES = ECLIPSE_FORGE + "/src/main/resources";
    static final String ECLIPSE_CDN = WORKSPACE + "/cauldron";
    static final String ECLIPSE_CDN_SRC = ECLIPSE_CDN + "/src/main/java";
    static final String ECLIPSE_CDN_RES = ECLIPSE_CDN + "/src/main/resources";
    static final String ECLIPSE_EDU = WORKSPACE + "/McEdu";
    static final String ECLIPSE_EDU_SRC = ECLIPSE_EDU + "/src/main/java";
    static final String ECLIPSE_EDU_RES = ECLIPSE_EDU + "/src/main/resources";
    static final String ECLIPSE_RUN = WORKSPACE + "/run";
    static final String ECLIPSE_NATIVES = ECLIPSE_RUN + "/bin/natives";
    static final String ECLIPSE_ASSETS = ECLIPSE_RUN + "/assets";
    // FML stuff only...
    static final String FML_PATCH_DIR = "{FML_DIR}/patches/minecraft";
    static final String FML_SOURCES = "{FML_DIR}/src/main/java";
    static final String FML_RESOURCES = "{FML_DIR}/src/main/resources";
    static final String FML_TEST_SOURCES = "{FML_DIR}/src/test/java";
    static final String FML_TEST_RES = "{FML_DIR}/src/test/resources";
    static final String FML_VERSIONF = "src/main/resources/fmlversion.properties";
    static final String FML_LICENSE = "{FML_DIR}/LICENSE-fml.txt";
    static final String FML_CREDITS = "{FML_DIR}/CREDITS-fml.txt";
    static final String FML_LOGO = "{FML_DIR}/jsons/big_logo.png";
    // Forge stuff only
    static final String FORGE_PATCH_DIR = "{FORGE_DIR}/patches/minecraft";
    static final String FORGE_SOURCES = "{FORGE_DIR}/src/main/java";
    static final String FORGE_RESOURCES = "{FORGE_DIR}/src/main/resources";
    static final String FORGE_TEST_SOURCES = "{FORGE_DIR}/src/test/java";
    static final String FORGE_TEST_RES = "{FORGE_DIR}/src/test/resources";
    static final String FORGE_LICENSE = "{FORGE_DIR}/MinecraftForge-License.txt";
    static final String FORGE_CREDITS = "{FORGE_DIR}/MinecraftForge-Credits.txt";
    static final String PAULSCODE_LISCENCE1 = "{FORGE_DIR}/Paulscode IBXM Library License.txt";
    static final String PAULSCODE_LISCENCE2 = "{FORGE_DIR}/Paulscode SoundSystem CodecIBXM License.txt";
    static final String FORGE_LOGO = FORGE_RESOURCES + "/forge_logo.png";
    static final String FORGE_VERSION_JAVA = FORGE_SOURCES + "/net/minecraftforge/common/ForgeVersion.java";
    // Extra stuff only, for the current project
    static final String EXTRA_PATCH_DIR = "patches";
    static final String EXTRA_SOURCES = "src/main/java";
    static final String EXTRA_RESOURCES = "src/main/resources";
    static final String EXTRA_TEST_SOURCES = "src/test/java";
    static final String EXTRA_TEST_RES = "src/test/resources";
    // USED ONLY FOR Cauldron.. BUT ITS BUKKIT STUFF
    static final String BUKKIT_SOURCES = "{BUKKIT_DIR}/src/main/java";
    static final String EXTRACTED_RES = "{BUILD_DIR}/extractedResources";
    // CrowdIn Stuff
    static final String CROWDIN_ZIP = "{BUILD_DIR}/crowdin-localizations.zip";
    static final String CROWDIN_FORGEID = "minecraft-forge";

    private DevConstants() {

    }

}
