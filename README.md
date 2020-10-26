<img src="https://i.loli.net/2020/09/06/lQscneqbV8Hptxz.png">

<div align="center">
  <h1>Mohist 1.16.3</h1>

### Minecraft Forge Hybrid server implementing the Paper/Spigot/Bukkit API(1.12.2/1.16), formerly known as Thermos/Kettle/Cauldron/MCPC+

[![](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.codemc.io%2Fjob%2FMohist-Community%2Fjob%2Fdebug&style=for-the-badge)](https://ci.codemc.io/job/Mohist-Community/job/debug)
[![](https://img.shields.io/github/stars/Mohist-Community/Mohist.svg?label=Stars&style=for-the-badge&logo=github)](https://github.com/Mohist-Community/Mohist/stargazers)
[![](https://img.shields.io/github/license/Mohist-Community/Mohist?style=for-the-badge)](https://github.com/Mohist-Community/Mohist/blob/1.16.3/LICENSE)
[![](https://img.shields.io/badge/Forge-1.16.3-brightgreen.svg?colorB=26303d&style=for-the-badge&logo=Conda-Forge)](http://files.minecraftforge.net/)
[![](https://img.shields.io/badge/Paper-1.16.3-brightgreen.svg?colorB=DC3340&style=for-the-badge)](https://papermc.io/downloads#Paper-1.16.3)
[![](https://img.shields.io/badge/AdoptOpenJDK-8u252-brightgreen.svg?colorB=469C00&style=for-the-badge&logo=java)](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot)
[![](https://img.shields.io/badge/Gradle-4.10.3-brightgreen.svg?colorB=469C00&style=for-the-badge&logo=gradle)](https://docs.gradle.org/4.10.3/release-notes.html)
[![](https://img.shields.io/bstats/servers/6762?label=bStats&style=for-the-badge)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![](https://badges.crowdin.net/mohist/localized.svg)](https://crowdin.com/project/mohist)

[![Mohist Stats](https://bstats.org/signatures/server-implementation/Mohist.svg)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![Mohist Stats](https://bstats.org/signatures/bukkit/Mohist.svg)](https://bstats.org/plugin/bukkit/Mohist/3939)
</div>

Progress
------

- [x] Rectify Forge (74f9ef838)
- [x] Start patch craftbukkit(dea41389) bukkit(4abf9e96) spigot(57bbdd8e)
- [x] Beta release
- [ ] The nms patch is compatible with mcp

Getting Help
------
   [**Home**](https://mohistmc.com/) [**Discord**](https://discord.gg/ZgXjHGd) [**QQ**](https://jq.qq.com/?_wv=1027&k=5YIRYnH)  
   
Download
------

You can download the latest version from [**Jenkins**](https://ci.codemc.io/job/Mohist-Community/job/debug/).

Install
------

Take a moment you will find it

Building
------
* Checkout project
  * Clone Mohist:  
  `git clone https://github.com/Mohist-Community/Mohist.git`  
  `git checkout 1.16.3`
* Build Mohist
  * Build with Linux:  
  `bash gradlew setup`  
  `bash gradlew installerJar`  
  * Build with Windows:  
  `gradlew.bat setup`  
  `gradlew.bat installerJar`

The Mohist server jar file is located at `.\projects\mohist\build\libs\*.jar`

This is the jarfile that you should run.

Thanks to the following projects
------
* [**Bukkit**](https://hub.spigotmc.org/stash/scm/spigot/bukkit.git) - plugin support.
* [**Paper**](https://github.com/PaperMC/Paper.git) - performance optimizations.
* [**CraftBukkit**](https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git) - plugin support.
* [**Spigot**](https://hub.spigotmc.org/stash/scm/spigot/spigot.git) - plugin support.
* [**MinecraftForge**](https://github.com/MinecraftForge/MinecraftForge.git) - mod support.
* [**Thermos**](https://github.com/CyberdyneCC/Thermos.git) - Partial code source.

A Special Thanks To:
-------------
<a href="https://serverjars.com/"><img src="https://serverjars.com/assets/img/logo_white.svg" width="200"></a>
<a href="https://ci.codemc.io/"><img src="https://i.loli.net/2020/03/11/YNicj3PLkU5BZJT.png" width="200"></a>

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit](http://www.yourkit.com/), makers of the outstanding java profiler, support open source projects of all kinds with their full featured [Java](https://www.yourkit.com/java/profiler/index.jsp) and [.NET](https://www.yourkit.com/.net/profiler/index.jsp) application profilers. We thank them for granting Mohist an OSS license so that we can make our software the best it can be.
