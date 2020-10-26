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

<a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README.md">English</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-zh.md">中文</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-fr.md">Français</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-de.md">German</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-es.md">Spanish</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-in.md">Indonesian</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-pt-BR.md">Portuguese, Brazilian</a> | <a href="https://github.com/Mohist-Community/Mohist/blob/1.12.2/README-ru.md">Russian</a>

[![Mohist Stats](https://bstats.org/signatures/server-implementation/Mohist.svg)](https://bstats.org/plugin/server-implementation/Mohist/6762)
[![Mohist Stats](https://bstats.org/signatures/bukkit/Mohist.svg)](https://bstats.org/plugin/bukkit/Mohist/3939)
</div>
      
ヘルプの参照
------
   [**ホーム**](https://mohist.red/)
   
   [**Discord**](https://discord.gg/ZgXjHGd)
   
   [**QQ**](https://jq.qq.com/?_wv=1027&k=5YIRYnH)  
   
ダウンロード
------

You can download the latest version from [**Jenkins**](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/).

インストール
------
このソフトウェアにはJava 8が必要です。

[Mohist](https://ci.codemc.org/job/Mohist-Community/job/Mohist-1.12.2/) をダウンロードし、空のディレクトリにサーバーのjarファイルを置いて下さい。 `java` コマンドを使用して起動します :

```bash
java -jar yourJar.jar
```

ビルド
------
* プロジェクトの確認
  * クローン Mohist:
  `git clone https://github.com/Mohist-Community/Mohist.git`
* Build Mohist
  * Linuxでのビルド:
  `bash gradlew setup installerJar`
  * Windowsでのビルド:
  `gradlew.bat setup installerJar`

すべてのビルドは `.\projects\mohist\build\libs\mohist-xxxxx-server.jar` になります

mohist-xxxxx-server.jar は実行すべきサーバーファイルです

以下のプロジェクトに感謝します。
------
* [**Bukkit**](https://hub.spigotmc.org/stash/scm/spigot/bukkit.git) - プラグインのサポート.
* [**Paper**](https://github.com/PaperMC/Paper.git) - パフォーマンスの最適化.
* [**CraftBukkit**](https://hub.spigotmc.org/stash/scm/spigot/craftbukkit.git) - プラグインのサポート.
* [**Spigot**](https://hub.spigotmc.org/stash/scm/spigot/spigot.git) - プラグインのサポート.
* [**MinecraftForge**](https://github.com/MinecraftForge/MinecraftForge.git) - MODのサポート.
* [**Atom**](https://gitlab.com/divinecode/atom/Atom.git) - 部分的なコードソース.
* [**Thermos**](https://github.com/CyberdyneCC/Thermos.git) - 部分的なコードソース.
* [**um_bukkit**](https://github.com/TechCatOther/um_bukkit.git) - 部分的なコードソース.

特別に感謝します:
-------------
<a href="https://serverjars.com/"><img src="https://serverjars.com/assets/img/logo_white.svg" width="200"></a>
<a href="https://ci.codemc.io/"><img src="https://i.loli.net/2020/03/11/YNicj3PLkU5BZJT.png" width="200"></a>

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

あなたのキット [YourKit](http://www.yourkit.com/) は優れたJavaプロファイラーのメーカーであり、あらゆる機能のオープンソースプロジェクトを、フル機能の[Java](https://www.yourkit.com/java/profiler/index.jsp) と[.NET](https://www.yourkit.com/.net/profiler/index.jsp) アプリケーションプロファイラーでサポートしています。 私たちのソフトウェアを最高のものにできるように、Mohist に OSS ライセンスを付与していただいた彼らに感謝します。