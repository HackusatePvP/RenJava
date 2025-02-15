# RenJava
Visual novel game engine based off of [RenPy](https://www.renpy.org/) built with [JavaFX](https://openjfx.io/).

This is a passion project, which means I'm learning as I go. There are a lot of mistakes and bugs which will be addressed eventually. This is not meant for production.

**Community**
- [Website]() (Not yet created), [Discord]() (Not yet created), [YouTube]()
- [RSDK-GUI](https://github.com/HackusatePvP/rsdk-gui) All-in-one utility for creating RenJava projects.
- [renjava-template](https://github.com/HackusatePvP/renjava-template) This is the maven template to get started.

## Disclaimer
RenJava does not claim ownership over any copyrighted material such as music, video, or assets. RenJava is only the framework not the game made by an author.
Any copyright claims must be filed against the author. RenJava does not own or distribute any game(s). It would be similar to suing YouTube because someone else uploaded an entire move without permission.

As an author you must have expressed consent to use any copyrighted material like music. Alternatively, you could look for royalty free options.

## Project Requirements
- [Java-21](https://www.oracle.com/java/technologies/downloads/#java21) (You can typically download this within your IDE)
- [intellij](https://www.jetbrains.com/idea/download/?section=windows) or [Eclipse](https://www.eclipse.org/downloads/)
- [Maven](https://maven.apache.org/download.cgi) (Might come with your IDE)
- [RSDK](https://github.com/HackusatePvP/RenJavaSDK/releases) (Download the .jar file in the latest release. This is a hard requirement.)

## Application Requirements
- The OS has to support Java 21 JDK.
- The OS is supported.
- 4GB RAM
- 512MB VRAM
- Dual Core CPU

## Compile from source
There are different branches you can clone from; Master, Experimental. Master is the stable branch, this is the recommended branch for cloning.

Experimental is the branch that has the latest untested features and bug fixes. Once the development branch is tested, it is merged with Master and a new build is released.
- Download RenJava via clone or file.
- Extract file and open RenJava with any IDE that supports Maven.
- Run `mvn clean install` from the IDE or command line inside the root directory.

## Getting started
Follow this [guide](https://github.com/HackusatePvP/RenJava/wiki/Getting-Started) to get an easy start. You can use the [renjava-template](https://github.com/HackusatePvP/renjava-template) to get started or use [HeroAdventure](https://github.com/HackusatePvP/HeroAdventure). Also explore our [wiki](https://github.com/HackusatePvP/RenJava/wiki) to learn more about the framework and all of its features.

## Notice
As of build 0.1.x, the project is just a demo. You will not be able to create a fully functional game with the current state of the framework.
I recommend using the experimental branch over the master branch when testing and demoing the framework. Thank you for using and exploring RenJava.

### Limitations
The framework only supports imagery. You will not be able to add sprites or other forms of 2D animation/modeling. ~~Video scenes are currently in the works, and you will soon be able to add pre-rendered animations in a form of a video file.~~ This limitation is not permanent but there are no future plans to currently add support for integrated animations.
The framework will never support 3D games. 

### Linux
Linux has entered testing and as of now the only distro that will see support is Debian. If you are wanting to use or support other distros, the framework should still work. Similar to mac, you will need to manually download the proper java version and use the java command.
Linux will have two installation methods `install_linux_64.sh` and `install_linux_local_64`. The `start_linux.sh` will automatically install java if it's not already installed, and launch the game.

When running RSDK the linux tar file will no longer be downloaded, instead the .deb file will be.
To install the .deb file manually use the following commands. You will have to run the newest version of RSDK first to download the .deb file. You can also download it from [amazon](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)
Keep in mind, the above scripts will automatically do this.
```bash
sudo apt install ./jdk/linux/amazon-corretto-21-x64-linux-jdk.deb
```

### Other/Un-supported OS
You can use the framework on most systems, as long as that system can render a desktop. The system will also have to support Java 21.
You can view all supported systems on [amazon-corretto](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html) which is the JDK I use.

Warning: RenJava is primarily tested on Windows/Linux (Debian). Just because the system can support the framework, does not mean the framework is optimized or functional on that system.
If you have issues you will have to figure it out on your own. All of this is subjected to change and more os's may get full support later.

## Updating Framework
When updating to a newer version of RenJava it is recommended to delete default 'settings.properties' and 'build.info'.
These files are located in the 'renjava' folder for your game directory. These files need to be reset before you distribute the game.
RenJava will automatically re-create these files with newer settings and info.

```
Hero-Adventure-1.0
  addons
  game
  jdk
  logs
  renjava
  HeroAdventure-1.0-SNAPSHOT.jar
  start.bat
  start.sh
```

## Developers
- View our api changes [here]().
- Documentation can be found [here](https://github.com/HackusatePvP/RenJava/wiki).
- If you are struggling with anything at all join our discord, and we will help.