<img src="https://i.loli.net/2020/02/28/vZRHJACadF7rgn5.png">

<div align="center">
  <h1>Mohist 1.12.2</h1>

### Minecraft Forge Hybrid server implementing the Paper/Spigot/Bukkit API(1.12.2/1.15.2), formerly known as Thermos/Kettle/Cauldron/MCPC+

[![](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.codemc.io%2Fjob%2FMohist-Community%2Fjob%2FMohist-1.12.2&style=for-the-badge)](https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2)
[![](https://img.shields.io/github/stars/Mohist-Community/Mohist.svg?label=Stars&style=for-the-badge&logo=github)](https://github.com/Mohist-Community/Mohist/stargazers)
[![](https://img.shields.io/github/license/Mohist-Community/Mohist?style=for-the-badge)](https://github.com/Mohist-Community/Mohist/blob/1.12.2/LICENSE)
[![](https://img.shields.io/badge/Forge-1.12.2--14.23.5.2854-brightgreen.svg?colorB=26303d&style=for-the-badge&logo=Conda-Forge)](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
[![](https://img.shields.io/badge/Paper-1.12.2-brightgreen.svg?colorB=DC3340&style=for-the-badge)](https://papermc.io/downloads#Paper-1.12)
[![](https://img.shields.io/badge/AdoptOpenJDK-8u252-brightgreen.svg?colorB=469C00&style=for-the-badge&logo=java)](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot)
[![](https://img.shields.io/badge/Gradle-5.6.4-brightgreen.svg?colorB=469C00&style=for-the-badge&logo=gradle)](https://docs.gradle.org/5.6.4/release-notes.html)
[![](https://img.shields.io/bstats/servers/6762?label=bStats&style=for-the-badge)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![](https://badges.crowdin.net/mohist/localized.svg)](https://crowdin.com/project/mohist)

<a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-zh.md">中文</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-fr.md">Français</a>

[![Mohist Stats](https://bstats.org/signatures/server-implementation/Mohist.svg)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![Mohist Stats](https://bstats.org/signatures/bukkit/Mohist.svg)](https://bstats.org/plugin/bukkit/Mohist/3939)
</div>

Notice
------
The original owner and devs stopped participating in Mohist development due to personal reasons.
Now, the Mohist development team is undergoing reorganization.  
After the new development team is stabilized, we will continue maintenance and development as usual.  
If you have corresponding type of server development experience or are familiar with BukkitAPI,  
And willing to participate in this project。  
You can send an email to 3293876954@qq.com to inform me.  
  
We urgently need new devs to inject new impetus into this project to make it more excellent and lasting.  
      
Getting Help
------
   [**Home**](https://mohist.red/)
   
   [**Discord**](https://discord.gg/ZgXjHGd)
   
   [**QQ**](https://jq.qq.com/?_wv=1027&k=5YIRYnH)  
   
Download
------

You can download the latest version from [**Jenkins**](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/).

Install
------
This software requires Java 8.

[Download Mohist](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/) and place the server jar in an empty directory.

Now launch it using the `java` command:

```bash
java -jar yourJar.jar
```

Building
------
* Checkout project
  * Clone Mohist:
  `git clone https://github.com/Mohist-Community/Mohist.git`
* Build Mohist
  * Build with Linux:
  `bash gradlew launch4j`
  * Build with Windows:
  `gradlew.bat launch4j`

All builds will be in `.\build\distributions\`

The Mohist server jar file is located at `.\build\distributions\Mohist-xxxxx-server.jar`

This is the jarfile that you should run.

Plugin development
------
* Building
   * Build with Linux:
   `bash gradlew reobfToSRG`
   * Build with Windows:
   `gradlew.bat reobfToSRG`

`.\build\mohist_srg.jar` Is the development frontend you need

Thanks to the following projects
------
* [**Bukkit**](https://hub.spigotmc.org/stash/scm/spigot/bukkit.git) - plugin support.
* [**Paper**](https://github.com/PaperMC/Paper.git) - performance optimizations.
* [**CraftBukkit**](https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git) - plugin support.
* [**Spigot**](https://hub.spigotmc.org/stash/scm/spigot/spigot.git) - plugin support.
* [**MinecraftForge**](https://github.com/MinecraftForge/MinecraftForge.git) - mod support.
* [**Atom**](https://gitlab.com/divinecode/atom/Atom.git) - Partial code source.
* [**Thermos**](https://github.com/CyberdyneCC/Thermos.git) - Partial code source.
* [**um_bukkit**](https://github.com/TechCatOther/um_bukkit.git) - Partial code source.
* [**EMC**](https://github.com/starlis/empirecraft) - performance optimizations.
* [**Purpur**](https://github.com/pl3xgaming/Purpur) - performance optimizations.
* [**Akarin**](https://github.com/Akarin-project/Akarin) - performance optimizations.
* [**Phosphor**](https://github.com/jellysquid3/phosphor-fabric) - LightEngine optimizations.
* [**Lithium**](https://github.com/jellysquid3/lithium-fabric) - performance optimizations.
* [**Yapfa**](https://github.com/tr7zw/YAPFA) - performance optimizations.
* [**Tuinity**](https://github.com/Spottedleaf/Tuinity) - performance optimizations.

A Special Thanks To:
-------------
<a href="https://serverjars.com/"><img src="https://serverjars.com/assets/img/logo_white.svg" width="200"></a>
<a href="https://ci.codemc.io/"><img src="https://i.loli.net/2020/03/11/YNicj3PLkU5BZJT.png" width="200"></a>

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit](http://www.yourkit.com/), makers of the outstanding java profiler, support open source projects of all kinds with their full featured [Java](https://www.yourkit.com/java/profiler/index.jsp) and [.NET](https://www.yourkit.com/.net/profiler/index.jsp) application profilers. We thank them for granting Mohist an OSS license so that we can make our software the best it can be.