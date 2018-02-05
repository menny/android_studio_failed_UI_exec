package net.evendanan.externalbuilderplugin;

import java.io.File;
import java.util.Collections;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.tasks.Exec;

public class ExternalBuilderPlugin implements Plugin<Project> {

  public void apply(final Project project) {
    project.afterEvaluate(ExternalBuilderPlugin::configurePlugin);
  }

  private static void configurePlugin(Project project) {
    final Project rootProject = project.getRootProject();

    final Exec compileTask = (Exec) project.task(Collections.singletonMap("type", Exec.class), "compile");
    compileTask.workingDir(project.getProjectDir());
    final File outputFolder = new File(project.getBuildDir(), "classes/");
    if (!outputFolder.exists() && !outputFolder.mkdirs()) {
      throw new RuntimeException("Failed to create output folder " + outputFolder);
    }
    compileTask.setCommandLine("javac", "-d", outputFolder.getAbsolutePath(), project.getProjectDir().getAbsolutePath() + "/src/Lib.java");

    final Exec packageTask = (Exec) project.task(Collections.singletonMap("type", Exec.class), "package");
    packageTask.workingDir(project.getProjectDir());
    final File outputJarFile = new File(project.getBuildDir(), project.getName() + ".jar");
    packageTask.setCommandLine("jar", "cf", outputJarFile.getAbsolutePath(), "-C", outputFolder.getAbsolutePath(), ".");
    packageTask.dependsOn(compileTask);
    //   Craps *.class
    /*
     * Adding build configurations
     */
    final Configuration defaultConfiguration = project.getConfigurations().create(Dependency.DEFAULT_CONFIGURATION);
    defaultConfiguration.setCanBeConsumed(true);
    defaultConfiguration.setCanBeResolved(true);
    defaultConfiguration.getOutgoing().getArtifacts().add(new BuilderPublishArtifact(packageTask, outputJarFile));

//
//        /*
//         * Applying IDEA plugin, so IntelliJ will index the source files
//         */
//        IdeaPlugin ideaPlugin = (IdeaPlugin) project.getPlugins().apply("idea");
//        final IdeaModule ideaModule = ideaPlugin.getModel().getModule();
//        ideaModule.setSourceDirs(getSourceFoldersFromBazelAspect(rootProject, aspectRunner, config.targetName));
//
//        /*
//         * Creating a CLEAN task in the root project
//         */
//        if (rootProject.getTasksByName("externalClean", falseonly search the root project).isEmpty()) {
//            final BazelCleanTask bazelCleanTask = (BazelCleanTask) rootProject.task(Collections.singletonMap("type", BazelCleanTask.class), "bazelClean");
//            bazelCleanTask.setBazelConfig(config);
//            rootProject.getTasks().findByPath(":clean").dependsOn(bazelCleanTask);
//        }
  }
}
