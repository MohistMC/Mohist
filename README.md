<img src="https://i.loli.net/2020/09/06/lQscneqbV8Hptxz.png">

## Mohist-1.7.10

[![](https://ci.codemc.org/buildStatus/icon?job=Mohist-Community%2FMohist-1.7.10)](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.7.10/)
![](https://img.shields.io/github/stars/Mohist-Community/Mohist.svg?label=Stars)
![](https://img.shields.io/github/license/Mohist-Community/Mohist.svg)
[![](https://img.shields.io/badge/Forge-1.7.10--10.13.4.1614-brightgreen.svg?colorB=26303d)](http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.12.2.html)
[![](https://img.shields.io/badge/Spigot-1.7.10-brightgreen.svg?colorB=DC3340)](https://papermc.io/downloads#Paper-1.12)
![](https://img.shields.io/badge/OracleJdk-8u241-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/Gradle-2.8-brightgreen.svg?colorB=469C00)
![](https://img.shields.io/badge/ideaIU-jbr8-brightgreen.svg?colorB=469C00)

### Getting Help
   [**Home**](https://mohistmc.com//)
   [**bStats**](https://bstats.org/plugin/bukkit/Mohist)
   [**Discord**](https://discord.gg/ZgXjHGd)
   [**QQ**](https://jq.qq.com/?_wv=1027&k=5YIRYnH)  
   
   <img src="https://bstats.org/signatures/bukkit/Mohist.svg">

### Download
* [**Jenkins**](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.7.10/)

### Building
* Checkout project
  * You can use IDE or clone from console:
  `git clone -b 1.7.10 https://github.com/Mohist-Community/Mohist.git`
* Setup
  * Setting up submodules:
  `git submodule update --init --recursive`
* Building
  * Build the project for Linux:
  `./gradlew setupCauldron` 
  `./gradlew launch4j`
  * or for Windows:
  `./gradlew.bat setupCauldron ` 
  `./gradlew.bat launch4j `

All builds will be in `.\Mohist\build\distributions\`

Mohist-xxxxx-server.jar - is the server we should run it
