# RuneLite Plugin Setup

## Prerequisites
- Java 11 (Installed at `C:\Program Files\Eclipse Adoptium\jdk-11.0.29.7-hotspot`)
- Git

## Building the Plugin
Open a terminal in this directory and run:

```powershell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-11.0.29.7-hotspot"; .\gradlew.bat build
```

## Running the Plugin
To run the client with your plugin loaded, you can run the `ExamplePluginTest` class in your IDE, or use the following command (if you configure a run task, otherwise sticking to IDE is easiest for RuneLite plugins):

```powershell
# Note: Running via command line requires more setup usually handled by the IDE (IntelliJ recommended).
```

## Project Structure
- `src/main/java/com/example/ExamplePlugin.java`: Main plugin logic.
- `src/main/java/com/example/ExampleConfig.java`: Configuration settings.
