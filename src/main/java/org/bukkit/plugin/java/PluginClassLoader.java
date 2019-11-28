package org.bukkit.plugin.java;

import cc.uraniummc.UraniumMapping;
import cc.uraniummc.UraniumRemapper;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.RemapperProcessor;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.md_5.specialsource.transformer.MavenShade;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.cauldron.CauldronUtils;
import static org.apache.commons.codec.Charsets.UTF_8;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.objectweb.asm.Type;

// Cauldron end
//import org.bouncycastle.util.io.Streams;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final ConcurrentMap<String, Class<?>> classes = new ConcurrentHashMap<String, Class<?>>(); // Cauldron - Threadsafe classloadin
    private final PluginDescriptionFile description;
    public PluginDescriptionFile getDescription(){
        return description;
    }
    private final File dataFolder;
    private final File file;
    JavaPlugin plugin; // Cauldron - remove final
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;
    // Cauldron start
    private JarRemapper remapper;     // class remapper for this plugin, or null
    private RemapperProcessor remapperProcessor; // secondary; for inheritance & remapping reflection
    private boolean debug;            // classloader debugging
    private int remapFlags = -1;

    private static ConcurrentMap<Integer,UraniumMapping> jarMappings = new ConcurrentHashMap<Integer, UraniumMapping>();
    private static final int F_GLOBAL_INHERIT   = 1 << 1;
    private static final int F_REMAP_OBCPRE     = 1 << 2;
    private static final int F_REMAP_NMS152     = 1 << 3;
    private static final int F_REMAP_NMS164     = 1 << 4;
    private static final int F_REMAP_NMS172     = 1 << 5;
    private static final int F_REMAP_NMS179     = 1 << 6;
    private static final int F_REMAP_NMS1710    = 1 << 7;
    private static final int F_REMAP_OBC152     = 1 << 8;
    private static final int F_REMAP_OBC164     = 1 << 9;
    private static final int F_REMAP_OBC172     = 1 << 10;
    private static final int F_REMAP_OBC179     = 1 << 11;
    private static final int F_REMAP_OBC1710    = 1 << 12;
    private static final int F_REMAP_GUAVA17    = 1 << 13;
    private static final int F_REMAP_GUAVA21    = 1 << 14;
    private static final int F_REMAP_REFLECTION = 1 << 15;
    private static final int F_REMAP_FUTURE = 1 << 16;
    private static final int F_REMAP_SQLITE321 = 1 << 17;
    private static final int F_REMAP_NMSPRE_MASK= 0xffff0000;  // "unversioned" NMS plugin version
    // This trick bypasses Maven Shade's package rewriting when using String literals [same trick in jline]
    private static final String org_bukkit_craftbukkit = new String(new char[] {'o','r','g','/','b','u','k','k','i','t','/','c','r','a','f','t','b','u','k','k','i','t'});
    // Cauldron end
    // Uranium start
    public final UraniumPluginClassLoader umcl=new UraniumPluginClassLoader();
    public final class UraniumPluginClassLoader{
        public UraniumPluginClassLoader() {
        }
        public boolean checkVer(String pTargetVer){
            char sign=pTargetVer.charAt(0);
            String ver=description.getVersion()!=null?description.getVersion():"";
            double ven;
            try{
                ven=Double.valueOf(ver);
            }catch (NumberFormatException e){
                ven=0;
            }
            String cver_info=pTargetVer.substring(1);
            double cven;
            try{
                cven=Double.valueOf(cver_info);
            }catch (NumberFormatException e){
                cven=0;
            }
            boolean equal=false;
            switch (sign){
                case '>':
                case '<':
                    if(pTargetVer.charAt(1)=='=')equal=true;
                    cver_info=cver_info.substring(1);
                    break;
            }
            switch (sign){
                case '=':
                    return ver.equals(cver_info);
                case '-':
                    return ver.startsWith(cver_info);
                case '+':
                    return ver.endsWith(cver_info);
                case '>':
                    return equal?ven>=cven:ven>cven;
                case '<':
                    return equal?ven<=cven:ven<cven;
                case '*':
                    return Pattern.compile(cver_info).matcher(ver).find();
                default:
                    return ver.equals(pTargetVer);
            }
        }
        public String remapClass(String pClass){
            String tPacket="net.minecraft.util.";
            if(pClass.startsWith(tPacket)&&pClass.indexOf('.',tPacket.length())!=-1){
                return pClass.substring(tPacket.length());
            }else
            if(pClass.startsWith("net.minecraft.server")) {
                JarMapping jarMapping = getJarMapping(remapFlags); // grab from SpecialSource
                String remappedClass = jarMapping.classes.get(pClass.replaceAll("\\.", "/")); // get remapped pkgmcp class name
                return remappedClass;
            }
            return pClass;
        }
        public String remapMethod(Class<?> pClass,String pName, Class<?>[]parameterTypes,boolean pDeclared){
            UraniumMapping jarMapping = (UraniumMapping) getJarMapping(remapFlags); // grab from SpecialSource
            StringBuilder keyF=new StringBuilder(pName+" (");
            if(parameterTypes!=null&&parameterTypes.length>0){
                for(Class<?> type : parameterTypes){
                    keyF.append(Type.getType(type).getDescriptor());
                }
            }
            keyF.append(")");
            return jarMapping.remapMethod(jarMapping.mRclasses.get(pClass.getName().replace('.', '/')),pName,keyF.toString(),pDeclared);
        }

        public String remapField(Class<?> pClass,String pName,boolean pDeclared){
            UraniumMapping jarMapping=(UraniumMapping)getJarMapping(remapFlags); // grab from SpecialSource
            return jarMapping.remapField(jarMapping.mRclasses.get(pClass.getName().replace('.', '/')),pName,pDeclared);
        }
    }
    // Uranium end
    PluginClassLoader(final JavaPluginLoader loader, final ClassLoader parent, final PluginDescriptionFile description, final File dataFolder, final File file) throws InvalidPluginException, MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;

        // Cauldron start

        String pluginName = this.description.getName();

        // configure default remapper settings
        boolean useCustomClassLoader = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.custom-class-loader", true);
        debug = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.debug", false);
        boolean remapNMS1710 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-nms-v1_7_R4", true);
        boolean remapNMS179 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-nms-v1_7_R3", true);
        boolean remapNMS172 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-nms-v1_7_R1", true);
        boolean remapNMS164 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-nms-v1_6_R3", true);
        boolean remapNMS152 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-nms-v1_5_R3", true);
        String remapNMSPre = MinecraftServer.getServer().cauldronConfig.getString("plugin-settings.default.remap-nms-pre", "false");
        boolean remapOBC1710 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-obc-v1_7_R4", true);
        boolean remapOBC179 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-obc-v1_7_R3", true);
        boolean remapOBC172 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-obc-v1_7_R1", true);
        boolean remapOBC164 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-obc-v1_6_R3", true);
        boolean remapOBC152 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-obc-v1_5_R3", true);
        boolean remapOBCPre = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-obc-pre", false);
        boolean globalInherit = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.global-inheritance", true);
        boolean pluginInherit = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.plugin-inheritance", true);
        boolean reflectFields = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-reflect-field", true);
        boolean reflectClass = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-reflect-class", true);
        boolean allowFuture = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings.default.remap-allow-future", false);
        //boolean remapToSqlite321 = MinecraftServer.uraniumConfig.enableSQLite321.getValue();
        // plugin-specific overrides
        useCustomClassLoader = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".custom-class-loader", useCustomClassLoader, false);
        debug = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".debug", debug, false);
        remapNMS1710 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-nms-v1_7_R4", remapNMS1710, false);
        remapNMS179 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-nms-v1_7_R3", remapNMS179, false);
        remapNMS172 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-nms-v1_7_R1", remapNMS172, false);
        remapNMS164 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-nms-v1_6_R3", remapNMS164, false);
        remapNMS152 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-nms-v1_5_R3", remapNMS152, false);
        remapNMSPre = MinecraftServer.getServer().cauldronConfig.getString("plugin-settings."+pluginName+".remap-nms-pre", remapNMSPre, false);
        remapOBC1710 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-obc-v1_7_R4", remapOBC1710, false);
        remapOBC179 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-obc-v1_7_R3", remapOBC179, false);
        remapOBC172 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-obc-v1_7_R1", remapOBC172, false);
        remapOBC164 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-obc-v1_6_R3", remapOBC164, false);
        remapOBC152 = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-obc-v1_5_R3", remapOBC152, false);
        remapOBCPre = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-obc-pre", remapOBCPre, false);
        globalInherit = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".global-inheritance", globalInherit, false);
        pluginInherit = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".plugin-inheritance", pluginInherit, false);
        reflectFields = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-reflect-field", reflectFields, false);
        reflectClass = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-reflect-class", reflectClass, false);
        allowFuture = MinecraftServer.getServer().cauldronConfig.getBoolean("plugin-settings."+pluginName+".remap-allow-future", allowFuture, false);
        //remapToSqlite321 = MinecraftServer.uraniumConfig.getBoolean("plugin-settings."+pluginName+".remap-to-sqlite321",remapToSqlite321,false);
        // Uranium start
        boolean remapGuava17= MinecraftServer.uraniumConfig.enableGuava17.getValue();
        boolean remapGuava21= MinecraftServer.uraniumConfig.enableGuava21.getValue();
        String remap_guava17_ver=MinecraftServer.uraniumConfig.getString("plugin-settings."+pluginName+".remap-guava17-ver",null);
        String remap_guava21_ver=MinecraftServer.uraniumConfig.getString("plugin-settings."+pluginName+".remap-guava21-ver",null);
        remapGuava17 = MinecraftServer.uraniumConfig.getBoolean("plugin-settings." + pluginName + ".remap-guava17", remapGuava17, false);
        if(remap_guava17_ver!=null&&!remapGuava17) {
            remapGuava17 = umcl.checkVer(remap_guava17_ver);
        }
        remapGuava21 = MinecraftServer.uraniumConfig.getBoolean("plugin-settings." + pluginName + ".remap-guava21", remapGuava21, false);
        if(remap_guava21_ver!=null&&!remapGuava21) {
            remapGuava21 = umcl.checkVer(remap_guava21_ver);
        }
        boolean remapReflection=MinecraftServer.uraniumConfig.remapReflection.getValue();
        remapReflection=MinecraftServer.uraniumConfig.getBoolean("plugin-settings."+pluginName+".remap-Reflection",remapReflection,false);
        // Uranium end
        if (debug) {
            System.out.println("PluginClassLoader debugging enabled for "+pluginName);
        }

        if (!useCustomClassLoader) {
            remapper = null;
            return;
        }

        int flags = 0;
        if (remapNMS1710) flags |= F_REMAP_NMS1710;
        if (remapNMS179) flags |= F_REMAP_NMS179;
        if (remapNMS172) flags |= F_REMAP_NMS172;
        if (remapNMS164) flags |= F_REMAP_NMS164;
        if (remapNMS152) flags |= F_REMAP_NMS152;
        if (!remapNMSPre.equals("false")) {
            if      (remapNMSPre.equals("1.7.10")) flags |= 0x17100000;
            else if (remapNMSPre.equals("1.7.9")) flags |= 0x01790000;
            else if (remapNMSPre.equals("1.7.2")) flags |= 0x01720000;
            else if (remapNMSPre.equals("1.6.4")) flags |= 0x01640000;
            else if (remapNMSPre.equals("1.5.2")) flags |= 0x01520000;
            else {
                System.out.println("Unsupported nms-remap-pre version '"+remapNMSPre+"', disabling");
            }
        }
        if (remapOBC1710) flags |= F_REMAP_OBC1710;
        if (remapOBC179) flags |= F_REMAP_OBC179;
        if (remapOBC172) flags |= F_REMAP_OBC172;
        if (remapOBC164) flags |= F_REMAP_OBC164;
        if (remapOBC152) flags |= F_REMAP_OBC152;
        if (remapOBCPre) flags |= F_REMAP_OBCPRE;
        if (globalInherit) flags |= F_GLOBAL_INHERIT;
        //Uranium start
        if (remapGuava17) flags |= F_REMAP_GUAVA17;
        if (remapReflection) flags |=F_REMAP_REFLECTION;
        if (allowFuture) flags |=F_REMAP_FUTURE;
        //if (remapToSqlite321) flags |= F_REMAP_SQLITE321;
        if (remapGuava21) flags |= F_REMAP_GUAVA21;
        //Uranium end
        remapFlags = flags; // used in findClass0
        JarMapping jarMapping = getJarMapping(flags);

        // Load inheritance map
        if ((flags & F_GLOBAL_INHERIT) != 0) {
            if (debug) {
                System.out.println("Enabling global inheritance remapping");
                //ClassLoaderProvider.verbose = debug; // TODO: changed in https://github.com/md-5/SpecialSource/commit/132584eda4f0860c9d14f4c142e684a027a128b8#L3L48
            }
            jarMapping.setInheritanceMap(loader.getGlobalInheritanceMap());
            jarMapping.setFallbackInheritanceProvider(new ClassLoaderProvider(this));
        }

        remapper = new cc.uraniummc.UraniumRemapper((UraniumMapping) jarMapping,this,remapReflection);
        ((UraniumRemapper)remapper).setDebug(true);
        if (pluginInherit || reflectFields || reflectClass) {
            remapperProcessor = new RemapperProcessor(
                    pluginInherit ? loader.getGlobalInheritanceMap() : null,
                    (reflectFields || reflectClass) ? jarMapping : null);

            remapperProcessor.setRemapReflectField(reflectFields);
            remapperProcessor.setRemapReflectClass(reflectClass);
            remapperProcessor.debug = debug;
        } else {
            remapperProcessor = null;
        }
        // Cauldron end
        //Uranium start

        if(debug) {
            try {
                UraniumMapping um=(UraniumMapping)jarMapping;
                File fp=new File("remap-maps");
                if(!fp.exists())fp.mkdirs();
                File fm= new File(fp,pluginName+".tmp.methods");
                File ff= new File(fp,pluginName+".tmp.fields");
                File fc=new File(fp,pluginName+".tmp.classes");
                File frm= new File(fp,pluginName+".tmp.rmethods");
                File frc=new File(fp,pluginName+".tmp.rclasses");
                Gson g = new GsonBuilder().setPrettyPrinting().create();
                Files.write(g.toJson(jarMapping.methods),fm, UTF_8);
                Files.write(g.toJson(jarMapping.fields),ff, UTF_8);
                Files.write(g.toJson(jarMapping.classes),fc,UTF_8);
                Files.write(g.toJson(um.mMethodsNR),frm,UTF_8);
                Files.write(g.toJson(um.mRclasses),frc,UTF_8);
            }catch (Exception e){}
        }
        //Uranium end
        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex);
            }

            Class<? extends JavaPlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(JavaPlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + description.getMain() + "' does not extend JavaPlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {  // Cauldron - public access for plugins to support CB NMS -> MCP class remap
        return findClass(name, true);
    }

    // Cauldron start
    /**
     * Get the "native" obfuscation version, from our Maven shading version.
     */
    public static String getNativeVersion() {
        // see https://github.com/mbax/VanishNoPacket/blob/master/src/main/java/org/kitteh/vanish/compat/NMSManager.java
        if (CauldronUtils.deobfuscatedEnvironment()) return "v1_7_R4"; // support plugins in deobf environment
        final String packageName = org.bukkit.craftbukkit.CraftServer.class.getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.')  + 1);
    }

    /**
     * Load NMS mappings from CraftBukkit mc-dev to repackaged srgnames for FML runtime deobf
     *
     * @param jarMapping An existing JarMappings instance to load into
     * @param obfVersion CraftBukkit version with internal obfuscation counter identifier
     *                   >=1.4.7 this is the major version + R#. v1_4_R1=1.4.7, v1_5_R1=1.5, v1_5_R2=1.5.1..
     *                   For older Futureversions (including pre-safeguard) it is the full Minecraft version number
     * @throws IOException
     */
    private void loadNmsMappings(JarMapping jarMapping, String obfVersion) throws IOException {
        Map<String, String> relocations = new HashMap<String, String>();
        // mc-dev jar to CB, apply version shading (aka plugin safeguard)
        relocations.put("net.minecraft.server", "net.minecraft.server." + obfVersion);

        // support for running 1.7.10 plugins in Cauldron dev
        if (CauldronUtils.deobfuscatedEnvironment() && obfVersion.equals("v1_7_R4"))
        {
            jarMapping.loadMappings(
                    new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("mappings/"+obfVersion+"/cb2pkgmcp.srg"))),
                    new MavenShade(relocations),
                    null, false);
            //Uranium start
            jarMapping.loadMappings(
                    new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("uranium_data/mappings/new.srg"))),
                    new MavenShade(relocations),
                    null, false);
            //Uranium end
            jarMapping.loadMappings(
                    new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("mappings/"+obfVersion+"/obf2pkgmcp.srg"))),
                    null, // no version relocation for obf
                    null, false);
            // resolve naming conflict in FML/CB
            jarMapping.methods.put("net/minecraft/server/"+obfVersion+"/PlayerConnection/getPlayer ()Lorg/bukkit/craftbukkit/entity/CraftPlayer;", "getPlayerB");
        }
        else
        {
            jarMapping.loadMappings(
                    new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("mappings/"+obfVersion+"/cb2numpkg.srg"))),
                    new MavenShade(relocations),
                    null, false);
            //Uranium start
            jarMapping.loadMappings(
                    new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("uranium_data/mappings/new.srg"))),
                    new MavenShade(relocations),
                    null, false);
            //Uranium end
            if (obfVersion.equals("v1_7_R4")) {
                jarMapping.loadMappings(
                        new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("mappings/"+obfVersion+"/obf2numpkg.srg"))),
                        null, // no version relocation for obf
                        null, false);
            }

            // resolve naming conflict in FML/CB
            jarMapping.methods.put("net/minecraft/server/"+obfVersion+"/PlayerConnection/getPlayer ()Lorg/bukkit/craftbukkit/"+getNativeVersion()+"/entity/CraftPlayer;", "getPlayerB");
        }
        // remap bouncycastle to Forge's included copy, not the vanilla obfuscated copy (not in Cauldron), see #133
        //jarMapping.packages.put("net/minecraft/"+obfVersion+"/org/bouncycastle", "org/bouncycastle"); No longer needed
    }
    // Added future version of NMS remap, limited support some new plugins.
    private void loadFuture(JarMapping jarMapping, String[] obfVersion) throws IOException{
        Map<String, String> relocations = new HashMap<String, String>();
        // mc-dev jar to CB, apply version shading (aka plugin safeguard)
        for(String obf:obfVersion) {
            relocations.put("net.minecraft.server", "net.minecraft.server." + obf);
        }
        jarMapping.loadMappings(
                new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("uranium_data/mappings/new.srg"))),
                new MavenShade(relocations),
                null, false);
        /*
        jarMapping.loadMappings(
                new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("mappings/v1_7_R4/cb2numpkg.srg"))),
                new MavenShade(relocations),
                null, false);
        */
        jarMapping.loadMappings(
                new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("mappings/v1_7_R4/obf2numpkg.srg"))),
                null, // no version relocation for obf
                null, false);
    }
    private JarMapping getJarMapping(int flags) {
        UraniumMapping jarMapping = jarMappings.get(flags);
        if (jarMapping != null) {
            if (debug) {
                System.out.println("Mapping reused for "+Integer.toHexString(flags));
            }
            return jarMapping;
        }

        jarMapping = new UraniumMapping();
        try {

            // Guava 10 is part of the Bukkit API, so plugins can use it, but FML includes Guava 15
            // To resolve this conflict, remap plugin usages to Guava 10 in a separate package
            // Most plugins should keep this enabled, unless they want a newer Guava
            // Uranium using guava17 when config enable
            if((flags & F_REMAP_GUAVA17)==0) {
                if((flags & F_REMAP_GUAVA21)!=0){
                    jarMapping.packages.put("com/google/common", "guava21/com/google/common");
                }else{
                    jarMapping.packages.put("com/google/common", "guava10/com/google/common");
                }
            }else{
                jarMapping.packages.put("com/google/common", "guava17/com/google/common");
            }
            //Uranium start
            //Remap SQLite for some new plugins.
            /*
            if((flags & F_REMAP_SQLITE321)!=0) {
                jarMapping.packages.put("org/sqlite", "sqlite321/org/sqlite");
            }
            */
            //Uranium end
            jarMapping.packages.put(org_bukkit_craftbukkit + "/libs/com/google/gson", "com/google/gson"); // Handle Gson being in a "normal" place
            // Bukkit moves these packages to nms while we keep them in root so we must relocate them for plugins that rely on them
            jarMapping.packages.put("net/minecraft/util/io", "io");
            jarMapping.packages.put("net/minecraft/util/com", "com");
            jarMapping.packages.put("net/minecraft/util/gnu", "gnu");
            jarMapping.packages.put("net/minecraft/util/org", "org");
            if ((flags & F_REMAP_NMS1710) != 0) {
                loadNmsMappings(jarMapping, "v1_7_R4");
            }

            if ((flags & F_REMAP_NMS179) != 0) {
                loadNmsMappings(jarMapping, "v1_7_R3");
            }

            if ((flags & F_REMAP_NMS172) != 0) {
                loadNmsMappings(jarMapping, "v1_7_R1");
            }

            if ((flags & F_REMAP_NMS164) != 0) {
                loadNmsMappings(jarMapping, "v1_6_R3");
            }

            if ((flags & F_REMAP_NMS152) != 0) {
                loadNmsMappings(jarMapping, "v1_5_R3");
            }

            if ((flags & F_REMAP_OBC1710) != 0) {
                if (CauldronUtils.deobfuscatedEnvironment())
                    jarMapping.packages.put(org_bukkit_craftbukkit+"/v1_7_R4", org_bukkit_craftbukkit);
                else jarMapping.packages.put(org_bukkit_craftbukkit+"/v1_7_R4", org_bukkit_craftbukkit+"/"+getNativeVersion());
            }

            if ((flags & F_REMAP_OBC179) != 0) {
                if (CauldronUtils.deobfuscatedEnvironment())
                    jarMapping.packages.put(org_bukkit_craftbukkit+"/v1_7_R3", org_bukkit_craftbukkit);
                else jarMapping.packages.put(org_bukkit_craftbukkit+"/v1_7_R3", org_bukkit_craftbukkit+"/"+getNativeVersion());
            }

            if ((flags & F_REMAP_OBC172) != 0) {
                if (CauldronUtils.deobfuscatedEnvironment())
                    jarMapping.packages.put(org_bukkit_craftbukkit+"/v1_7_R1", org_bukkit_craftbukkit+"/"+getNativeVersion());
                else jarMapping.packages.put(org_bukkit_craftbukkit+"/v1_7_R1", org_bukkit_craftbukkit+"/"+getNativeVersion());
            }

            if ((flags & F_REMAP_OBC164) != 0) {
                jarMapping.packages.put(org_bukkit_craftbukkit+"/v1_6_R3", org_bukkit_craftbukkit+"/"+getNativeVersion());
            }

            if ((flags & F_REMAP_OBC152) != 0) {
                jarMapping.packages.put(org_bukkit_craftbukkit+"/v1_5_R3", org_bukkit_craftbukkit+"/"+getNativeVersion());
            }

            if ((flags & F_REMAP_OBCPRE) != 0) {
                // enabling unversioned obc not currently compatible with versioned obc plugins (overmapped) -
                // admins should enable remap-obc-pre on a per-plugin basis, as needed
                // then map unversioned to current version
                jarMapping.packages.put(org_bukkit_craftbukkit+"/libs/org/objectweb/asm", "org/objectweb/asm"); // ?
                jarMapping.packages.put(org_bukkit_craftbukkit, org_bukkit_craftbukkit+"/"+getNativeVersion());
            }

            if ((flags & F_REMAP_NMSPRE_MASK) != 0) {
                String obfVersion;
                switch (flags & F_REMAP_NMSPRE_MASK)
                {
                    case 0x17100000: obfVersion = "v1_7_R4"; break;
                    case 0x01790000: obfVersion = "v1_7_R3"; break;
                    case 0x01720000: obfVersion = "v1_7_R1"; break;
                    case 0x01640000: obfVersion = "v1_6_R3"; break;
                    case 0x01510000: obfVersion = "v1_5_R2"; break;
                    default: throw new IllegalArgumentException("Invalid unversioned mapping flags: "+Integer.toHexString(flags & F_REMAP_NMSPRE_MASK)+" in "+Integer.toHexString(flags));
                }

                jarMapping.loadMappings(
                        new BufferedReader(new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("mappings/" + obfVersion + "/cb2numpkg.srg"))),
                        null, // no version relocation!
                        null, false);
            }

            if((flags & F_REMAP_FUTURE) !=0){
                InputStream is=loader.getClass().getClassLoader().getResourceAsStream("Futureversions");
                String all=IOUtils.toString(is);
                String[] futureVer=all.split("\n");
                loadFuture(jarMapping,futureVer);
            }

            System.out.println("Mapping loaded "+jarMapping.packages.size()+" packages, "+jarMapping.classes.size()+" classes, "+jarMapping.fields.size()+" fields, "+jarMapping.methods.size()+" methods, flags "+Integer.toHexString(flags));

            if(jarMapping.mMethodsNR.isEmpty()&&!jarMapping.methods.isEmpty()){
                jarMapping.loadMethodsNR();
            }
            if(jarMapping.mRclasses.isEmpty()&&!jarMapping.classes.isEmpty()){
                jarMapping.loadRClasses();
            }
            JarMapping currentJarMapping = jarMappings.putIfAbsent(flags, jarMapping);
            return currentJarMapping == null ? jarMapping : currentJarMapping;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        // Cauldron start - remap any calls for classes with packaged nms version
        if (name.startsWith("net.minecraft."))
        {
            JarMapping jarMapping = this.getJarMapping(remapFlags); // grab from SpecialSource
            String remappedClass = jarMapping.classes.get(name.replaceAll("\\.", "/")); // get remapped pkgmcp class name
            Class<?> clazz = ((net.minecraft.launchwrapper.LaunchClassLoader)MinecraftServer.getServer().getClass().getClassLoader()).findClass(remappedClass);
            return clazz;
        }
        if (name.startsWith("org.bukkit.")) {
            if (debug) {
                System.out.println("Unexpected plugin findClass on OBC/NMS: name="+name+", checkGlobal="+checkGlobal+"; returning not found");
            }
            throw new ClassNotFoundException(name);
        }
        // custom loader, if enabled, threadsafety
        synchronized (name.intern()) {
            Class<?> result = classes.get(name);

            if (result == null) {
                if (checkGlobal) {
                    result = loader.getClassByName(name); // Don't warn on deprecation, but maintain overridability
                }

                if (result == null) {
                    if (remapper == null) {
                        result = super.findClass(name);
                    } else {
                        result = remappedFindClass(name);
                    }

                    if (result != null) {
                        loader.setClass(name, result);
                    }
                }
                if (result != null) {
                    Class<?> old = classes.putIfAbsent(name, result);
                    if (old != null && old != result) {
                        System.err.println("Defined class " + name + " twice as different classes, " + result + " and " + old);
                        result = old;
                    }
                }
            }

            return result;
        }
        // Cauldron end
    }
    private Class<?> remappedFindClass(String name) throws ClassNotFoundException {
        Class<?> result = null;

        try {
            // Load the resource to the name
            String path = name.replace('.', '/').concat(".class");
            URL url = this.findResource(path);
            if (url != null) {
                InputStream stream = url.openStream();
                if (stream != null) {
                    byte[] bytecode = null;

                    // Reflection remap and inheritance extract
                    if (remapperProcessor != null) {
                        // add to inheritance map
                        bytecode = remapperProcessor.process(stream);
                        if (bytecode == null) stream = url.openStream();
                    }

                    /*if (bytecode == null) {
                        bytecode = Streams.readAll(stream);
                    }*/

                    // Remap the classes
                    byte[] remappedBytecode = remapper.remapClassFile(bytecode, RuntimeRepo.getInstance());

                    if (debug) {
                        File file = new File("remapped-plugin-classes/"+name+".class");
                        file.getParentFile().mkdirs();
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(remappedBytecode);
                            fileOutputStream.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    // Define (create) the class using the modified byte code
                    // The top-child class loader is used for this to prevent access violations
                    // Set the codesource to the jar, not within the jar, for compatibility with
                    // plugins that do new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()))
                    // instead of using getResourceAsStream - see https://github.com/MinecraftPortCentral/Cauldron-Plus/issues/75
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection(); // parses only
                    URL jarURL = jarURLConnection.getJarFileURL();
                    CodeSource codeSource = new CodeSource(jarURL, new CodeSigner[0]);

                    result = this.defineClass(name, remappedBytecode, 0, remappedBytecode.length, codeSource);
                    if (result != null) {
                        // Resolve it - sets the class loader of the class
                        this.resolveClass(result);
                    }
                }
            }
        } catch (Throwable t) {
            if (debug) {
                System.out.println("remappedFindClass("+name+") exception: "+t);
                t.printStackTrace();
            }
            throw new ClassNotFoundException("Failed to remap class "+name, t);
        }

        return result;
    }
    // Cauldron end

    Set<String> getClasses() {
        return classes.keySet();
    }

    synchronized void initialize(JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = javaPlugin;

        javaPlugin.init(loader, loader.server, description, dataFolder, file, this);
    }
}