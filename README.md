# update: now SOLVED!
The actual problem was that one of the Android Studio plugins we have on our machines was failing to start up correctly and cause this issue. Once we disabled that (unrelated) plugin, this scenario worked just fine.


# android_studio_failed_UI_exec - Android Studio 3.1-beta1
A bug in Android Studio when defining project builder in a plugin

#Project description
Using a Gradle build-script plugin that adds a `compile` and `package` tasks to projects that it was applied onto.
This demos that when using an external builder mechanism to build artifacts from modules to be used in the other modules.

# Issue
Using Android Studio 3.1 beta1 or alpha9 (didn't check with other versions of 3.1). It will build from command-line, but will
fail to even start building when running from within Android Studio.

* Import this project into Android Studio.
* Assemble the project from command line: `./gradlew :app:assemble`
  * It should compile with no issues.
* In Android Studio, click the `:app:assemble` task in the Gradle side-menu
  * this should fail with `Error running 'externalbuilderplugin:app [assemble]': com.intellij.openapi.externalSystem.service.execution.ExternalSystemRunConfiguration cannot be cast to com.intellij.execution.configurations.ModuleBasedConfiguration` 

To be clear, this same project DOES NOT fail when trying to reproduce with latest IntelliJ.

# Initial sync
At first import, you might get `Error:The modules ['externalbuilderplugin', 'buildSrc'] point to same directory in the file system.
Each module has to have a unique path.`. This is unrelated, I think I included in this repo an _idea_ file I shouldn't. 
To fix this, right-click `externalbuilderplugin` in the Gradle side-menu and click `Refresh Gradle project`.

# Where does it work?
* Android Studio 3.0.1 does not show this error.
* Latest IntelliJ (2017.3.4) also does not show this error.
