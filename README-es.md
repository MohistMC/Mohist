<img src="https://i.loli.net/2020/09/06/lQscneqbV8Hptxz.png">

<div align="center">
  <h1>Mohist 1.12.2</h1>

### Minecraft Forge Hybrid server implementing the Paper/Spigot/Bukkit API(1.12.2/1.16), formerly known as Thermos/Cauldron/MCPC+

[![](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.codemc.io%2Fjob%2FMohist-Community%2Fjob%2FMohist-1.12.2)](https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2)
[![](https://img.shields.io/github/stars/Mohist-Community/Mohist.svg?label=Stars&logo=github)](https://github.com/Mohist-Community/Mohist/stargazers)
[![](https://img.shields.io/github/license/Mohist-Community/Mohist?)](https://github.com/Mohist-Community/Mohist/blob/1.12.2/LICENSE)
[![](https://img.shields.io/badge/Forge-1.12.2--14.23.5.2854-brightgreen.svg?colorB=26303d&logo=Conda-Forge)](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
[![](https://img.shields.io/badge/Paper-1.12.2-brightgreen.svg?colorB=DC3340)](https://papermc.io/downloads#Paper-1.12)
[![](https://img.shields.io/badge/AdoptOpenJDK-8u252-brightgreen.svg?colorB=469C00&logo=java)](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot)
[![](https://img.shields.io/badge/Gradle-4.9-brightgreen.svg?colorB=469C00&logo=gradle)](https://docs.gradle.org/4.9/release-notes.html)
[![](https://img.shields.io/bstats/servers/6762?label=bStats)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![](https://badges.crowdin.net/mohist/localized.svg)](https://crowdin.com/project/mohist)
[![](https://img.shields.io/discord/311256119005937665.svg?color=%237289da&label=Discord&logo=discord&logoColor=%237289da)](https://discord.gg/ZgXjHGd)
[![](https://img.shields.io/badge/Patreon-Support-orange.svg?logo=Patreon)](https://www.patreon.com/mohist)

<a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README.md">English</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-zh.md">中文</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-fr.md">Français</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-de.md">German</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-in.md">Indonesian</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-jp.md">Japanese</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-pt-BR.md">Portuguese, Brazilian</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-ru.md">Russian</a>

[![Mohist Stats](https://bstats.org/signatures/server-implementation/Mohist.svg)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![Mohist Stats](https://bstats.org/signatures/bukkit/Mohist.svg)](https://bstats.org/plugin/bukkit/Mohist/3939)
</div>

| Version  | Support |
| ------------- | ------------- |
| 1.16.x  | Active  |
| 1.12.2  | Active  |
      
Obtener Ayuda
------
   [**Inicio**](https://mohist.red/)
   
   [**Discord**](https://discord.gg/ZgXjHGd)
   
   [**QQ**](https://jq.qq.com/?_wv=1027&k=5YIRYnH)  
   
Descargar
------

You can download the latest version from [**Jenkins**](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/).

Instalar
------
Este software requiere Java 8.

Descarga [Mohist](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/) y coloca el archivo .jar del servidor en un directorio vacío. Ahora inicia usando el comando `java`:

```bash
java -jar yourJar.jar
```

Compilación
------
* Revisa el proyecto
  * Clone Mohist:
  `git clone https://github.com/Mohist-Community/Mohist.git`
* Construir Mohist
  * Construir en Linux:
  `bash gradlew setup installerJar`
  * Construir en Windows:
  `gradlew.bat setup installerJar`

The Mohist server jar file is located at `.\projects\mohist\build\libs\mohist-xxxxx-server.jar`

mohist-xxxxx-server.jar es el archivo del servidor que deberías ejecutar

Gracias a los siguientes proyectos
------
* [**Bukkit**](https://hub.spigotmc.org/stash/scm/spigot/bukkit.git) - soporte al plugin.
* [**Paper**](https://github.com/PaperMC/Paper.git) - optimización del rendimiento.
* [**CraftBukkit**](https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git) - soporte al plugin.
* [**Spigot**](https://hub.spigotmc.org/stash/scm/spigot/spigot.git) - soporte al plugin.
* [**MinecraftForge**](https://github.com/MinecraftForge/MinecraftForge.git) - soporte de Mods.
* [**Atom**](https://gitlab.com/divinecode/atom/Atom.git) - Fuente de código parcial.
* [**Thermos**](https://github.com/CyberdyneCC/Thermos.git) - Fuente de código parcial.
* [**um_bukkit**](https://github.com/TechCatOther/um_bukkit.git) - Fuente de código parcial.

Agradecimientos especiales a:
-------------
<a href="https://serverjars.com/"><img src="https://serverjars.com/assets/img/logo_white.svg" width="200"></a>
<a href="https://ci.codemc.io/"><img src="https://i.loli.net/2020/03/11/YNicj3PLkU5BZJT.png" width="200"></a>

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit](http://www.yourkit.com/), creadores del excelente perfilador (profiler) de Java, soportan proyectos de código abierto de todo tipo de perfiladores (profilers) con sus características completas en [Java](https://www.yourkit.com/java/profiler/index.jsp) y en [.NET](https://www.yourkit.com/.net/profiler/index.jsp). Les agradecemos que hayan concedido a Mohist una licencia OSS para que podamos hacer de nuestro software lo mejor que puede ser.