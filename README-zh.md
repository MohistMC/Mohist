<img src="https://i.loli.net/2020/02/28/vZRHJACadF7rgn5.png">

<div align="center">
  <h1>Mohist 1.12.2</h1>

### 基于Forge并融合Paper/Spigot/Bukkit的高性能Minecraft服务器软件(服务端)

[![](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.codemc.io%2Fjob%2FMohist-Community%2Fjob%2FMohist-1.12.2&style=for-the-badge)](https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2)
[![](https://img.shields.io/github/stars/Mohist-Community/Mohist.svg?label=Stars&style=for-the-badge&logo=github)](https://github.com/Mohist-Community/Mohist/stargazers)
[![](https://img.shields.io/github/license/Mohist-Community/Mohist?style=for-the-badge)](https://github.com/Mohist-Community/Mohist/blob/1.12.2/LICENSE)
[![](https://img.shields.io/badge/Forge-1.12.2--14.23.5.2854-brightgreen.svg?colorB=26303d&style=for-the-badge&logo=Conda-Forge)](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
[![](https://img.shields.io/badge/Paper-1.12.2-brightgreen.svg?colorB=DC3340&style=for-the-badge)](https://papermc.io/downloads#Paper-1.12)
[![](https://img.shields.io/badge/AdoptOpenJDK-8u252-brightgreen.svg?colorB=469C00&style=for-the-badge&logo=java)](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot)
[![](https://img.shields.io/badge/Gradle-5.6.4-brightgreen.svg?colorB=469C00&style=for-the-badge&logo=gradle)](https://docs.gradle.org/5.6.4/release-notes.html)
[![](https://img.shields.io/bstats/servers/6762?label=bStats&style=for-the-badge)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![](https://badges.crowdin.net/mohist/localized.svg)](https://crowdin.com/project/mohist)

<a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README.md">English</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-fr.md">Français</a>

[![Mohist Stats](https://bstats.org/signatures/server-implementation/Mohist.svg)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![Mohist Stats](https://bstats.org/signatures/bukkit/Mohist.svg)](https://bstats.org/plugin/bukkit/Mohist/3939)
</div>

注意
------
Mohist服务端原所有者和开发者因个人原因暂时停止参与Mohist的开发  
现在Mohist新开发组重组近乎完成  ,开发与维护工作仍然与往常一样进行，我们并没有停更！  
如果你有Forge+Bukkit服务端开发经验或是了解BukkitAPI或FML，并愿意参与到开发当中，欢迎联系我  
希望大家有能力的话可以贡献出自己的力量，不只是贡献代码，做你喜欢的事都可以！  
本项目为用爱发电，我们每个人都是因对Miencraft的热爱而参与这个项目的开发。如果你参与开发希望获取报酬的话，可联系我。
我的邮箱 QQ:3293876954@qq.com / Carierx_Max@outlook.com

我们将会努力变好，无论历史将Mohist导向何处,我们选择继续前行，与诸君共勉，让我们一起共同进步！

获得帮助
------
   [**网站**](https://mohist.red/)
   
   [**DC群**](https://discord.gg/ZgXjHGd)
   
   [**QQ群**](https://jq.qq.com/?_wv=1027&k=5YIRYnH)  
   现在QQ群暂不可用，有issue请直接在github页面拉取即可。
   
下载
------

你可以从詹金森获取到最新的开发版本
 [**Jenkins**](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/).

安装
------
本服务端需要 JRE 1.8.0作为前置及以后版本才可作为生产环境进行运行（推荐Oracle的JRE，第三方可能存在问题）

[下载Mohist](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/) 然后把服务端JAR文件放入一个空目录当中.

启动它需要如下代码（Demo）

```bash
java -jar yourJar.jar
```

构建代码
------
* 下载Mohist源码
  * 克隆 Mohist:
  `git clone https://github.com/Mohist-Community/Mohist.git`
* 构建Mohist源码
  * Linux下构建:
  `bash gradlew launch4j`
  * Windows下构建:
  `gradlew.bat launch4j`

所有构建出的版本都会该目录 `.\build\distributions\`

Mohist服务端核心文件应在这里 `.\build\distributions\Mohist-xxxxx-server.jar`

这就是你可以运行的服务端核心本体.

插件开发
------
* 构建
   * Linux下构建:
   `bash gradlew reobfToSRG`
   * Windows下构建:
   `gradlew.bat reobfToSRG`

`.\build\mohist_srg.jar` 是你进行开发所需的前置

感谢以下开源项目
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

特别感谢:
-------------
<a href="https://serverjars.com/"><img src="https://serverjars.com/assets/img/logo_white.svg" width="200"></a>
<a href="https://ci.codemc.io/"><img src="https://i.loli.net/2020/03/11/YNicj3PLkU5BZJT.png" width="200"></a>

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit]（http://www.yourkit.com/）是出色的Java事件探查器的制造商，它们以其功能齐全的[Java]支持各种开源项目。（https://www.yourkit.com/java/ profiler / index.jsp）和[.NET]（https://www.yourkit.com/.net/profiler/index.jsp）应用程序分析器。 我们感谢他们为Mohist授予OSS许可证，以便使我们的软件达到最佳状态。