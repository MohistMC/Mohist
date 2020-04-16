<img src="https://i.loli.net/2020/02/28/vZRHJACadF7rgn5.png">

## Mohist-1.12.2 
#### [English](https://github.com/Mohist-Community/Mohist/blob/1.12.2/README.md) | [Français](https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-fr.md) 

## 强烈建议使用1.7+版本来快速解决已知和未知错误！

[![](https://ci.codemc.org/buildStatus/icon?job=Mohist-Community%2FMohist-1.12.2)](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/)
![](https://img.shields.io/github/stars/Mohist-Community/Mohist.svg?label=Stars)
![](https://img.shields.io/github/license/Mohist-Community/Mohist.svg)
[![](https://img.shields.io/badge/Forge-1.12.2--14.23.5.2847-brightgreen.svg?colorB=26303d)](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
[![](https://img.shields.io/badge/Paper-1.12.2-brightgreen.svg?colorB=DC3340)](https://papermc.io/downloads#Paper-1.12)
![](https://img.shields.io/badge/OracleJdk-8u241-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Gradle-5.5.1-brightgreen.svg?colorB=469C00)
[![](https://badges.crowdin.net/mohist/localized.svg)](https://crowdin.com/project/mohist)

### 获取帮助
   [**网站**](https://mohist.red/)
   [**bStats**](https://bstats.org/plugin/bukkit/Mohist)
   [**Discord**](https://discord.gg/ZgXjHGd)
   [**QQ**](https://jq.qq.com/?_wv=1027&k=5YIRYnH)  
   
   <img src="https://bstats.org/signatures/bukkit/Mohist.svg">

### 下载Mohist
* [**Jenkins**](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/)

### 构建
* 查看工程
  * 你可以使用集成开发环境(IDE)或是从控制台克隆一份源码:
  `git clone https://github.com/Mohist-Community/Mohist.git`
* 构建
  * 在Linux上构建Mohist:
  `./gradlew launch4j`
  * 在Windows上构建Mohist:
  `./gradlew.bat launch4j `

所有构建都将出现在 `.\Mohist\build\distributions\` 
Mohist-xxxxx-server.jar - 这是你应该运行的Jar

Plugin development
------
* Building
   * Build with Linux:
   `bash gradlew reobfToSRG`
   * Build with Windows:
   `gradlew.bat reobfToSRG`

All builds will be in `.\build\mohist_srg.jar` Is the development frontend you need

### 感谢以下项目
* [**Bukkit**](https://hub.spigotmc.org/stash/scm/spigot/bukkit.git) - 插件支持.
* [**Paper**](https://github.com/PaperMC/Paper.git) - 性能优化.
* [**CraftBukkit**](https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git) - 插件支持.
* [**Spigot**](https://hub.spigotmc.org/stash/scm/spigot/spigot.git) - 插件支持.
* [**MinecraftForge**](https://github.com/MinecraftForge/MinecraftForge.git) - 模组支持.
* [**Atom**](https://gitlab.com/divinecode/atom/Atom.git) - 部分源代码.
* [**Thermos**](https://github.com/CyberdyneCC/Thermos.git) - 部分源代码.
* [**um_bukkit**](https://github.com/TechCatOther/um_bukkit.git) - 部分源代码.
* [**ReflectionRemapper**](https://github.com/Maxqia/ReflectionRemapper.git) - 提供重映射框架。
