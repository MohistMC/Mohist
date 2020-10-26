<img src="https://i.loli.net/2020/09/06/lQscneqbV8Hptxz.png">

<div align="center">
  <h1>Mohist 1.12.2</h1>

### Minecraft Forge Hybrid server implementing the Paper/Spigot/Bukkit API(1.12.2/1.16), formerly known as Thermos/Cauldron/MCPC+

[![](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.codemc.io%2Fjob%2FMohist-Community%2Fjob%2FMohist-1.12.2&style=for-the-badge)](https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2)
[![](https://img.shields.io/github/stars/Mohist-Community/Mohist.svg?label=Stars&style=for-the-badge&logo=github)](https://github.com/Mohist-Community/Mohist/stargazers)
[![](https://img.shields.io/github/license/Mohist-Community/Mohist?style=for-the-badge)](https://github.com/Mohist-Community/Mohist/blob/1.12.2/LICENSE)
[![](https://img.shields.io/badge/Forge-1.12.2--14.23.5.2854-brightgreen.svg?colorB=26303d&style=for-the-badge&logo=Conda-Forge)](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
[![](https://img.shields.io/badge/Paper-1.12.2-brightgreen.svg?colorB=DC3340&style=for-the-badge)](https://papermc.io/downloads#Paper-1.12)
[![](https://img.shields.io/badge/AdoptOpenJDK-8u252-brightgreen.svg?colorB=469C00&style=for-the-badge&logo=java)](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot)
[![](https://img.shields.io/badge/Gradle-4.9-brightgreen.svg?colorB=469C00&style=for-the-badge&logo=gradle)](https://docs.gradle.org/4.9/release-notes.html)
[![](https://img.shields.io/bstats/servers/6762?label=bStats&style=for-the-badge)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![](https://badges.crowdin.net/mohist/localized.svg)](https://crowdin.com/project/mohist)

<a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README.md">English</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-zh.md">中文</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-fr.md">Français</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-es.md">Spanish</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-in.md">Indonesian</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-jp.md">Japanese</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-pt-BR.md">Portuguese, Brazilian</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-ru.md">Russian</a>

[![Mohist Stats](https://bstats.org/signatures/server-implementation/Mohist.svg)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![Mohist Stats](https://bstats.org/signatures/bukkit/Mohist.svg)](https://bstats.org/plugin/bukkit/Mohist/3939)
</div>
      
Hilfe
------
   [**Startseite**](https://mohist.red/)
   
   [**Discord**](https://discord.gg/ZgXjHGd)
   
   [**QQ**](https://jq.qq.com/?_wv=1027&k=5YIRYnH)  
   
Download
------

You can download the latest version from [**Jenkins**](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/).

Install
------
Dieses Programm benötigt Java 8.

Lade den [Mohist](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/) herunter und platziere ihn in einen leeren Ordner. Führen sie ihn nun mit dem 'java' Befehl aus:

```bash
java -jar yourJar.jar
```

Kompilieren
------
* Projekt auschecken
  * Clone Mohist:
  `git clone https://github.com/Mohist-Community/Mohist.git`
* Build Mohist
  * Kompilieren mit Linux:
  `bash gradlew setup installerJar`
  * Kompilieren mit Windows:
  `gradlew.bat setup installerJar`

Alle Builds werden in `.\projects\mohist\build\libs\mohist-xxxxx-server.jar` sein

mohist-xxxxx-server.jar ist die Server-Datei, die Sie ausführen sollten

Danke für das folgende Projekt
------
* [**Bukkit**](https://hub.spigotmc.org/stash/scm/spigot/bukkit.git) - plugin-Unterstützung.
* [**Paper**](https://github.com/PaperMC/Paper.git) - Leistungsoptimierungen.
* [**CraftBukkit**](https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git) - plugin-Unterstützung.
* [**Spigot**](https://hub.spigotmc.org/stash/scm/spigot/spigot.git) - plugin-Unterstützung.
* [**MinecraftForge**](https://github.com/MinecraftForge/MinecraftForge.git) - mod-Unterstützung.
* [**Atom**](https://gitlab.com/divinecode/atom/Atom.git) - Teilcode Quelle.
* [**Thermos**](https://github.com/CyberdyneCC/Thermos.git) - Teilcode Quelle.
* [**um_bukkit**](https://github.com/TechCatOther/um_bukkit.git) - Teilcode Quelle.

Besonderer Dank an:
-------------
<a href="https://serverjars.com/"><img src="https://serverjars.com/assets/img/logo_white.svg" width="200"></a>
<a href="https://ci.codemc.io/"><img src="https://i.loli.net/2020/03/11/YNicj3PLkU5BZJT.png" width="200"></a>

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit](http://www.yourkit.com/), Entwickler des herausragenden Java Profilers, Unterstützung von Open-Source Projekten aller Art und ihrem Umfangreichen [Java](https://www.yourkit.com/java/profiler/index.jsp) und [.NET](https://www.yourkit.com/.net/profiler/index.jsp) Anwendungs-Profiler. Ein dank gilt ihnen, für das Gewähren einer OSS Lizenz, damit wir unsere Software so gut wie möglich machen können.