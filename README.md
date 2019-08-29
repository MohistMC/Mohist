## Mohist

## We have not left, except here, all other places are fake! Please don't be fooled!

[![](https://ci.codemc.org/buildStatus/icon?job=Mohist-Community%2FMohist)](https://ci.codemc.org/job/Mohist-Community/job/Mohist/)
![](https://img.shields.io/github/stars/Mohist-Community/Mohist.svg?label=Stars)
![](https://img.shields.io/github/license/Mohist-Community/Mohist.svg)
[![](https://img.shields.io/badge/Forge-1.12.2--14.23.5.2844-brightgreen.svg?colorB=26303d)](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
[![](https://img.shields.io/badge/Paper-1.12.2-brightgreen.svg?colorB=DC3340)](https://papermc.io/downloads#Paper-1.12)
![](https://img.shields.io/badge/Java-8u221-brightgreen.svg?colorB=469C00)

Since CraftBukkit has modified a lot of underlying layers, some of the core mods are not available, we are working hard to improve.

### Getting Help
   [**Discord**](https://discord.gg/HNmmrCV)
   [**Home**](https://www.mohist.red/)
   [**bStats**](https://bstats.org/plugin/bukkit/Mohist)

### Download
**Mohist is still in beta and you may encounter issues in using it with your server. You have been warned!**
* [**Jenkins**](https://ci.codemc.org/job/Mohist-Community/job/Mohist/)

### Building
* Checkout project
  * You can use IDE or clone from console:
  `git clone https://github.com/Mohist-Community/Mohist.git`
* Setup
  * Setting up submodules:
  `git submodule update --init --recursive`
* Building
  * Build the project for Linux:
  `./gradlew launch4j`
  * or for Windows:
  `./gradlew.bat launch4j `

All builds will be in `.\Mohist\build\distributions\`

If you are a plugin developer, you can add the following files to your library file `.\Mohist\build\localCache\Mohist\recompiled.jar`

Mohist-xxxxx-server.jar - is the server we should run it

### Installation
* Read the wiki in detail [**WIKI**](https://github.com/Mohist-Community/Mohist/wiki/Install-Mohist)

## How to install Mohsit: For those wishing to work on Mohsit itself

If you wish to actually inspect Mohsit, submit PRs or otherwise work
 with Mohsit itself, you're in the right place!
 
 [See the guide to setting up a Forge workspace](http://mcforge.readthedocs.io/en/latest/forgedev/).

## Pull requests

[See the "Making Changes and Pull Requests" section in the Forge documentation](http://mcforge.readthedocs.io/en/latest/forgedev/#making-changes-and-pull-requests).


### Credits
* [**Bukkit**](https://hub.spigotmc.org/stash/scm/spigot/bukkit.git) - plugin support.
* [**Paper**](https://github.com/PaperMC/Paper.git) - performance optimizations.
* [**CraftBukkit**](https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git) - plugin support.
* [**Spigot**](https://hub.spigotmc.org/stash/scm/spigot/spigot.git) - plugin support.
* [**MinecraftForge**](https://github.com/MinecraftForge/MinecraftForge.git) - mod support.
* [**Atom**](https://gitlab.com/divinecode/atom/Atom.git) - Partial code source.
* [**Thermos**](https://github.com/CyberdyneCC/Thermos.git) - Partial code source.
* [**um_bukkit**](https://github.com/TechCatOther/um_bukkit.git) - Partial code source.
