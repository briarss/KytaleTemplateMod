plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hytale.mod)
    alias(libs.plugins.kytale.ui)
}

group = findProperty("plugin_group") as String
version = findProperty("plugin_version") as String
val javaVersion = 24

repositories {
    mavenCentral()
    maven("https://maven.pokeskies.com/releases") {
        name = "PokeSkies"
    }
    maven("https://maven.hytale-modding.info/releases") {
        name = "HytaleModdingReleases"
    }
}

dependencies {
    // Kytale - Kotlin language loader (runtime dependency, provided by Kytale mod)
    compileOnly(libs.kytale)

    // Hexweave - DSL framework for events, commands, tasks, and ECS systems
    compileOnly(libs.hexweave)

    // Compile-only annotations
    compileOnly(libs.jetbrains.annotations)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

kotlin {
    jvmToolchain(javaVersion)
}

tasks.named<ProcessResources>("processResources") {
    val replaceProperties = mapOf(
        "plugin_group" to findProperty("plugin_group"),
        "plugin_maven_group" to project.group,
        "plugin_name" to findProperty("plugin_name"),
        "plugin_version" to project.version,
        "server_version" to findProperty("server_version"),
        "plugin_description" to findProperty("plugin_description"),
        "plugin_website" to findProperty("plugin_website"),
        "plugin_main_entrypoint" to findProperty("plugin_main_entrypoint"),
        "plugin_author" to findProperty("plugin_author")
    )

    filesMatching("manifest.json") {
        expand(replaceProperties)
    }

    inputs.properties(replaceProperties)
}

hytale {

}

// Kytale UI Plugin Configuration
kytaleUi {
    // Package(s) to scan for @UiDefinition classes (faster builds)
    packages.set(listOf("com.example.template.ui"))

    // Output directory for generated .ui files (this is the default)
    // outputDir.set(file("src/main/resources/Common/UI/Custom/Pages"))
}

// =============================================================================
// Hydrate Task - Initialize project from template
// =============================================================================

tasks.register("hydrate") {
    group = "setup"
    description = "Initialize project from template with your mod details"

    doLast {
        val console = System.console() ?: run {
            println("ERROR: No console available. Run with --no-daemon or use gradle.properties directly.")
            return@doLast
        }

        println("\n=== Kytale Mod Template Setup ===\n")

        // Gather information
        val modName = console.readLine("Mod name (e.g., MyAwesomeMod): ")?.trim()
            ?: error("Mod name is required")

        val packageName = console.readLine("Package name (e.g., com.example.mymod): ")?.trim()
            ?: error("Package name is required")

        val authorName = console.readLine("Author name: ")?.trim()
            ?: error("Author name is required")

        val description = console.readLine("Description [A Kotlin Hytale mod]: ")?.trim()
            ?.ifEmpty { "A Kotlin Hytale mod" } ?: "A Kotlin Hytale mod"

        val website = console.readLine("Website URL []: ")?.trim() ?: ""

        // Derive values
        val className = modName.replace(Regex("[^a-zA-Z0-9]"), "")
        val packagePath = packageName.replace(".", "/")
        val pluginGroup = authorName.replace(Regex("[^a-zA-Z0-9]"), "")

        println("\n--- Configuration ---")
        println("Mod name: $modName")
        println("Main class: $packageName.$className")
        println("Author: $authorName")
        println("Description: $description")
        println("Website: $website")

        val confirm = console.readLine("\nProceed? [Y/n]: ")?.trim()?.lowercase()
        if (confirm == "n" || confirm == "no") {
            println("Aborted.")
            return@doLast
        }

        // Update gradle.properties
        val propsFile = file("gradle.properties")
        val propsContent = """
            |# Project
            |plugin_group=$pluginGroup
            |plugin_name=$modName
            |plugin_version=1.0.0
            |plugin_description=$description
            |plugin_author=$authorName
            |plugin_website=$website
            |plugin_main_entrypoint=$packageName.$className
            |
            |# Hytale
            |server_version=0.+
            |
            |# Gradle
            |org.gradle.jvmargs=-Xmx2G
            |org.gradle.parallel=true
        """.trimMargin()
        propsFile.writeText(propsContent)

        // Update settings.gradle.kts
        val settingsFile = file("settings.gradle.kts")
        val settingsContent = settingsFile.readText()
            .replace("template-mod", modName.lowercase().replace(Regex("[^a-z0-9]"), "-"))
        settingsFile.writeText(settingsContent)

        // Create new package directory
        val oldSrcDir = file("src/main/kotlin/com/example/template")
        val newSrcDir = file("src/main/kotlin/$packagePath")
        newSrcDir.mkdirs()

        // Create UI package
        val uiDir = file("src/main/kotlin/$packagePath/ui")
        uiDir.mkdirs()

        // Create main class
        val mainClassFile = file("src/main/kotlin/$packagePath/$className.kt")
        val mainClassContent = """
            |package $packageName
            |
            |import aster.amo.hexweave.enableHexweave
            |import aster.amo.kytale.KotlinPlugin
            |import com.hypixel.hytale.server.core.Message
            |import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent
            |import com.hypixel.hytale.server.core.plugin.JavaPluginInit
            |
            |class $className(init: JavaPluginInit) : KotlinPlugin(init) {
            |
            |    override fun setup() {
            |        super.setup()
            |
            |        enableHexweave {
            |            events {
            |                listen<PlayerConnectEvent> {
            |                    plugin.logger.atInfo().log("Player connected: ${'$'}{event.playerRef.uuid}")
            |                }
            |            }
            |
            |            commands {
            |                literal("hello", "Say hello") {
            |                    executesPlayer {
            |                        sendMessage(Message.raw("Hello from $modName!"))
            |                    }
            |                }
            |            }
            |        }
            |    }
            |
            |    override fun start() {
            |        super.start()
            |        logger.atInfo().log("$modName started!")
            |    }
            |
            |    override fun shutdown() {
            |        logger.atInfo().log("$modName shutting down...")
            |        super.shutdown()
            |    }
            |}
        """.trimMargin()
        mainClassFile.writeText(mainClassContent)

        // Create example UI definition
        val exampleUiFile = file("src/main/kotlin/$packagePath/ui/ExampleUi.kt")
        val exampleUiContent = """
            |package $packageName.ui
            |
            |import aster.amo.kytale.ui.dsl.*
            |
            |@UiDefinition("$modName/ExamplePage")
            |fun examplePage() = interactiveUiPage("example-page") {
            |    group {
            |        layoutMode = LayoutMode.Top
            |        padding = UiPadding(top = 20, left = 20, right = 20, bottom = 20)
            |
            |        label {
            |            text = "$modName UI"
            |            style = UiLabelStyle().apply {
            |                fontSize = 24
            |                textColor = "#ffffff"
            |                renderBold = true
            |            }
            |        }
            |
            |        textButton("click-me") {
            |            primaryButton("Click Me!")
            |            onClick = {
            |                // Handle button click
            |                // 'player' and 'playerRef' are available in this context
            |            }
            |        }
            |    }
            |}
        """.trimMargin()
        exampleUiFile.writeText(exampleUiContent)

        // Clean up template files
        if (oldSrcDir.exists() && oldSrcDir.path != newSrcDir.path) {
            oldSrcDir.deleteRecursively()
            // Clean up empty parent directories
            var parent = oldSrcDir.parentFile
            while (parent != null && parent.path.contains("src/main/kotlin") && parent.list()?.isEmpty() == true) {
                parent.delete()
                parent = parent.parentFile
            }
        }

        // Update kytaleUi packages in build.gradle.kts
        val buildFile = file("build.gradle.kts")
        val buildContent = buildFile.readText()
            .replace("com.example.template.ui", "$packageName.ui")
        buildFile.writeText(buildContent)

        println("\n✓ Project hydrated successfully!")
        println("\nNext steps:")
        println("  1. Run: ./gradlew build")
        println("  2. Run: ./gradlew compileUi (to generate .ui files)")
        println("  3. Copy JAR from build/libs/ to your Hytale mods folder")
    }
}
