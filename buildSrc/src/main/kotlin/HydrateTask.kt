import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Task that replaces template placeholders in source files and paths.
 *
 * Placeholders:
 * - `$plugin_name$` - Display name of the plugin (e.g., "MyMod")
 * - `$plugin_group$` - Plugin group/author (e.g., "AmoAster")
 * - `$plugin_package$` - Package name (e.g., "com.example.mymod")
 *
 * In paths, `$plugin_package$` dots are converted to directory separators.
 *
 * Usage:
 * 1. Clone the template repository
 * 2. Update gradle.properties with your mod details
 * 3. Run `./gradlew hydrate`
 * 4. Start developing!
 */
abstract class HydrateTask : DefaultTask() {

    @get:Input
    var pluginName: String = ""

    @get:Input
    var pluginGroup: String = ""

    @get:Input
    var pluginPackage: String = ""

    init {
        group = "setup"
        description = "Replaces template placeholders in source files and paths"
    }

    @TaskAction
    fun hydrate() {
        // Validate inputs
        if (pluginName.isBlank() || pluginGroup.isBlank() || pluginPackage.isBlank()) {
            logger.error("ERROR: Missing required properties in gradle.properties")
            logger.error("  plugin_name: $pluginName")
            logger.error("  plugin_group: $pluginGroup")
            logger.error("  plugin_package: $pluginPackage")
            logger.error("")
            logger.error("Please update gradle.properties with your mod details and run again.")
            return
        }

        // Process source directories
        val srcDir = project.file("src/main/kotlin")
        if (srcDir.exists()) {
            srcDir.walkTopDown().filter { it.isFile }.forEach { file ->
                processFile(file)
            }
        }

        // Process resources directory
        val resourcesDir = project.file("src/main/resources")
        if (resourcesDir.exists()) {
            resourcesDir.walkTopDown().filter { it.isFile }.forEach { file ->
                processFile(file)
            }
        }

        // Process settings.gradle.kts
        val settingsFile = project.rootDir.resolve("settings.gradle.kts")
        if (settingsFile.exists()) {
            processFile(settingsFile)
        }

        // Process build.gradle.kts (for kytaleUi package)
        val buildFile = project.rootDir.resolve("build.gradle.kts")
        if (buildFile.exists()) {
            processFile(buildFile)
        }

        logger.lifecycle("")
        logger.lifecycle("Hydration complete! Template placeholders have been replaced.")
        logger.lifecycle("  Plugin Name: $pluginName")
        logger.lifecycle("  Plugin Group: $pluginGroup")
        logger.lifecycle("  Plugin Package: $pluginPackage")
        logger.lifecycle("")
        logger.lifecycle("Next steps:")
        logger.lifecycle("  1. Run: ./gradlew build")
        logger.lifecycle("  2. Run: ./gradlew compileUi (to generate .ui files)")
        logger.lifecycle("  3. Copy JAR from build/libs/ to your Hytale mods folder")
    }

    private fun processFile(file: java.io.File) {
        val newPath = Paths.get(applyPathReplacements(file.path))
        Files.createDirectories(newPath.parent)

        // Skip binary files
        if (file.path.endsWith(".png") || file.path.endsWith(".jar") ||
            file.path.endsWith(".class") || file.path.endsWith(".ico")) {
            if (file.toPath() != newPath) {
                Files.move(file.toPath(), newPath)
            }
        } else {
            val lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
                .map { applyFileReplacements(it) }
            Files.deleteIfExists(file.toPath())
            Files.write(newPath, lines)
        }

        // Clean up empty parent directories
        var parent = file.parentFile
        while (parent != null && parent.listFiles()?.isEmpty() == true &&
               parent.path.contains("src")) {
            Files.deleteIfExists(parent.toPath())
            parent = parent.parentFile
        }
    }

    private fun applyFileReplacements(content: String): String {
        return content
            .replace("\$plugin_name\$", pluginName)
            .replace("\$plugin_group\$", pluginGroup)
            .replace("\$plugin_package\$", pluginPackage)
    }

    private fun applyPathReplacements(path: String): String {
        return path
            .replace("\$plugin_name\$", pluginName)
            .replace("\$plugin_group\$", pluginGroup)
            .replace("\$plugin_package\$", pluginPackage.replace(".", java.io.File.separator))
    }
}
